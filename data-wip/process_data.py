from matplotlib import pyplot as plt

import sys

import pandas as pd
import geopandas as gp
import re
import shapely
import json


#adds AUTOGEN_CT_DISTRICT column to table using TOWN column
from input_data.ct_towns_to_districts import towns_to_districts
def ct_add_districts(df_raw):
    df_raw['AUTOGEN_CT_DISTRICT'] = df_raw.TOWN.apply(
            lambda x : towns_to_districts.get(x,None))

    return df_raw

#nebraska dataset has districts as numbers, change them to be NEnum
def ne_rename_districts(df_raw):
    df_raw['CD_06'] = df_raw['CD_06'].apply(
            lambda x: "NE0{}".format(x) if x != 0 else None)
    return df_raw


# ============ COMMON PROPERTY NAMES
# These are the properties we want to have across all states that we support
#
# uid
# district
# precinct_name town_name county_name
# population voting_pop total_votes dem_vote_fraction
# geometry
#
# TODO: we might also want lat/lon?
        #"INTPTLAT10": "lat",
        #"INTPTLON10": "lon",

ALL_CONFIGS = {
"NJ": {
    "state": "NJ",
    "infile": './input_data/NJ/shapefiles/nj_final.shp',
    "outfile": './final_data/nj_out.csv',
    "out_neighbors": './final_data/nj_neighbors.csv',
    "properties_map": {
        "GEOID10": "id",

        "NAME10": "name",
        #"TOWN_NAME": "town_name",
        #"COUNTY_NAM": "county_name",

        "US_HOUSE": "district_id",

        "POP100": "population",
        "VAP": "voting_population",
        "VOTES": "total_votes",
        "AV": "average_democrat_votes",

        "geometry": "geometry",
        "state": "state_id",
        },
    },
"CT": {
    "state": "CT",
    "infile": './input_data/CT/ct_final.shp',
    "outfile": './final_data/ct_out.csv',
    "out_neighbors": './final_data/ct_neighbors.csv',
    'process_raw': ct_add_districts, #is run at prepoc time
    "properties_map": {
        "GEOID10": "id",

        "NAME10": "name",
        #"TOWN": "town_name",
        #"COUNTY_NUM": "county_name", #not actually a name

        # dataset didn't actually have districts, so we add them
        # in the ct_add_districts
        "AUTOGEN_CT_DISTRICT": "district_id",

        "POP100": "population",
        "VAP": "voting_population",
        "TOTAL": "total_votes", 
        "DEM_PCT": "average_democrat_votes",

        "geometry": "geometry",
        "state": "state_id",
        },
    },
"NE": {
    "state": "NE",
    "infile": './input_data/NE/ne_final.shp',
    "outfile": './final_data/ne_out.csv',
    "out_neighbors": './final_data/ne_neighbors.csv',
    "process_raw": ne_rename_districts,
    "properties_map": {
        "GEOID10": "id",

        "NAMELSAD10": "name",
        #"TOWN_NAME": "town_name",
        #"COUNTY_NAM": "county_name",

        "CD_06": "district_id",

        "POP100": "population",
        "VAP": "voting_population",
        "USHTOT08": "total_votes",
        "AV_0608": "average_democrat_votes",

        "geometry": "geometry",
        "state": "state_id",
        },
    }
}



# ================== CHANGE THIS TO CHANGE WHICH IT GENERATES
# TODO, maybe just loop through all of em
args = sys.argv[1:]
if(len(args) == 0):
    print("Please enter a state: " + str(list(ALL_CONFIGS.keys())))
    exit(-1)
elif(len(args) > 1):
    print("Please enter only 1 state: " + str(list(ALL_CONFIGS.keys())))
    exit(-1)
else:
    statecode = args[0].upper()
    if(statecode not in ALL_CONFIGS):
        print("Unrecognized statecode '{}'".format(statecode))
        print("Please enter a state: " + str(list(ALL_CONFIGS.keys())))
        exit(-1)

CURR_CONFIG = ALL_CONFIGS[statecode]




# CODE IS HAPPENING BELOW HERE, LEAVE CONFIG STUFF ABOVE HERE
# ====================================================================================
# ====================================================================================
# ====================================================================================
# ====================================================================================

print("Loading from {}".format(CURR_CONFIG['infile']), flush=True)
df_raw = gp.read_file(CURR_CONFIG['infile'])

#TODO TEMP DEBUG
# clips to only use polygons in supplied coords (for faster testing)
#df_raw = df_raw.cx[-74.5:-74.2, 39.4:39.8]
#df_raw = df_raw.cx[-74.4:-74.3, 39.5:39.63]

df_raw['state'] = CURR_CONFIG['state']

# =============== get rid of weird empty districts =============
# There's a handful of districts with no population, no voting district id
# and theyre big and empty and over water
undef_dists = df_raw['NAME10'].str.contains(
                'voting districts not defined', 
                flags=re.I)
df_raw = df_raw[~ undef_dists]



# ============= discard extra columns, rename  =================
print("Selecting/renaming columns", flush=True)

def noop(x): return x
preproc_func = CURR_CONFIG.get("process_raw", noop)
df_raw = preproc_func(df_raw)

prop_map = CURR_CONFIG["properties_map"]
df = df_raw.reindex(columns=prop_map.keys())
df = df.rename(columns=prop_map)
df = df.set_index('id')

# ================ assign districts = None to null district for state ===
df.loc[df.district_id.isnull(), 'district_id'] = CURR_CONFIG["state"] + "_NULL"


# ================= Add neighbors column ==========================
print("Calculating neighbors", flush=True)

geoms = df[['geometry']]

#join geoms to the geoms they're intersecting
#index will contain duplicate entries
#columns will be: geometry, index_right
t_joined = gp.sjoin(geoms, geoms, how='left')

# take just index column, then filter out self neightbors
t_indices = t_joined["index_right"]
t_precinct_to_precinct = t_indices[t_indices != t_indices.index]


# ============ Put neighbors into a comma-delimited neighbors column

#group duplicate entries, 
t_groups = t_precinct_to_precinct.groupby(t_precinct_to_precinct.index)

#result is series of index: 'neighb,neighb,...'
#filter out (entries == self)
neighbors = t_groups.apply(lambda subf: ','.join(subf))

#alternatively:             ['neighb','neighb',...]
#neighbors = t_groups.apply(lambda subf: list(subf[subf['index_right']!=subf.index]['index_right']))

#Got the neighbors
df['neighbors'] = neighbors


# ============== output neighbors in separate neighbors table (precinct_to_precinct)

#t_joined
neighbors_outfile = CURR_CONFIG["out_neighbors"]
print("Outputting neighbor data to {}".format(neighbors_outfile))
t_precinct_to_precinct.to_csv(neighbors_outfile);


#TODO TEMP :Lets disable simplyfying polygons, as we might be able to do it better if
#theyre meshy
# ============ SIMPLIFY POLYGONS =============
#print("Simplifying polygons... ", end='', flush=True)
## we can cut down the number of points needed dramatically while still
## having accurate enough shapes
#
#
#def count_pts(d):
#    if(d.geom_type == 'MultiPolygon'):
#        return sum(count_pts(g) for g in d.geoms)
#    else:
#        return len(d.exterior.coords)
#def total_pts(df):
#    return sum(df.geometry.apply(count_pts))
#
#
#df_simple = df.copy()
#df_simple.geometry = df.simplify(tolerance=1E-4)
#print("done")
#print("Points reduced from {} to {}".format(total_pts(df), total_pts(df_simple)), flush=True)
#
#df = df_simple

##### RESULTS FROM TESTING: how much to simplify
# 1E-5 reduces to ~60% for basically no quality reduction
# 3E-5 is about 40%, and is also pretty good
# 1E-4 reduces to ~25%, but has some gaps when you zoom in
# anything  >1E-4 is ridiculously unsuable

# ======= TEST CODE FOR TESTING TOLERANCES
#def show(d):
#    d.plot(column="dem_vote_fraction")
#    plt.show()
#testf = pd.DataFrame({'tol':[ 0, 0.00001, 0.00004, 0.0001, 0.00035, 0.001 ]})
#
#fig = plt.figure()
#for index, row in testf.iterrows():
#    tol = row['tol']
#
#    d_new = df.copy()
#    d_new.geometry = df.simplify(tolerance = tol)
#    pts = total_pts(d_new)
#
#    ax = fig.add_subplot(2,4, index + 1)
#    ax.margins(-0.3)
#    d_new.plot(column="dem_vote_fraction", ax=ax)
#    ax.set_title("tol: {}; pts:{}".format(tol, pts))
#
#    row['pts'] = pts
#
#plt.show()

#show(df)
#show(df2)



# ============== OUTPUT ===============

# convert all geometries to json
def geom_to_json(geom):
    return json.dumps(shapely.geometry.mapping(geom))

df['geometry'] = df['geometry'].apply(geom_to_json)

#df is still a geodatafram, but there's nothing geographic about it
out_df = pd.DataFrame(df)

outfile = CURR_CONFIG["outfile"]
out_df.to_csv(outfile, sep='|')
print("Outputting |-delimited csv to {}".format(outfile))
