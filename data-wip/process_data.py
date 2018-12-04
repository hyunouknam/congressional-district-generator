from matplotlib import pyplot as plt


import pandas as pd
import geopandas as gp
import re
import shapely
import json

from ct_towns_to_districts import towns_to_districts

#adds AUTOGEN_CT_DISTRICT column to table using TOWN column
def ct_add_districts(df):
    df_raw['AUTOGEN_CT_DISTRICT'] = df_raw.TOWN.apply(
            lambda x : towns_to_districts.get(x,None))

    return df

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
    "infile": './NJ/shapefiles/nj_final.shp',
    "outfile": './nj_out.csv',
    "properties_map": {
        "GEOID10": "uid",

        "NAME10": "precinct_name",
        "TOWN_NAME": "town_name",
        "COUNTY_NAM": "county_name",

        "US_HOUSE": "district",

        "POP100": "population",
        "VAP": "voting_pop",
        "VOTES": "total_votes",
        "AV": "dem_vote_fraction",

        "geometry": "geometry",
        },
    },
"CT": {
    "infile": './CT/ct_final.shp',
    "outfile": './ct_out.csv',
    'process_raw': ct_add_districts, #is run at prepoc time
    "properties_map": {
        "GEOID10": "uid",

        "NAME10": "precinct_name",
        "TOWN": "town_name",
        "COUNTY_NUM": "county_name", #not actually a name

        # dataset didn't actually have districts, so we add them
        # in the ct_add_districts
        "AUTOGEN_CT_DISTRICT": "district",

        "POP100": "population",
        "VAP": "voting_pop",
        "VOTES": "total_votes",
        "AV": "dem_vote_fraction",
        "DEM_PCT": "fraction_votes_dem",

        "geometry": "geometry",
        },
    },
}



# ================== CHANGE THIS TO CHANGE WHICH IT GENERATES
# TODO, maybe just loop through all of em
CURR_CONFIG = ALL_CONFIGS["CT"]




# CODE IS HAPPENING BELOW HERE, LEAVE CONFIG STUFF ABOVE HERE
# ====================================================================================
# ====================================================================================
# ====================================================================================
# ====================================================================================

df_raw = gp.read_file(CURR_CONFIG['infile'])

#TODO TEMP DEBUG
# clips to only use polygons in supplied coords (for faster testing)
#df_raw = df_raw.cx[-74.5:-74.2, 39.4:39.8]
#df_raw = df_raw.cx[-74.4:-74.3, 39.5:39.63]

# =============== get rid of weird empty districts =============
# There's a handful of districts with no population, no voting district id
# and theyre big and empty and over water
undef_dists = df_raw['NAME10'].str.contains(
                'voting districts not defined', 
                flags=re.I)
df_raw = df_raw[~ undef_dists]


# ============= discard extra columns, rename  =================

def noop(x): return x
preproc_func = CURR_CONFIG.get("process_raw", noop)
df_raw = preproc_func(df_raw)

prop_map = CURR_CONFIG["properties_map"]
df = df_raw.reindex(columns=prop_map.keys())
df = df.rename(columns=prop_map)
df = df.set_index('uid')


# ================= Add neighbors column ==========================
geoms = df[['geometry']]

#join geoms to the geoms they're intersecting
#index will contain duplicate entries
#columns will be: geometry, index_right
t_joined = gp.sjoin(geoms, geoms, how='left')


#group duplicate entries, 
#filter out self
t_groups = t_joined.groupby(t_joined.index)


#result is series of index: 'neighb,neighb,...'
neighbors = t_groups.apply(lambda subf: ','.join(subf[subf['index_right']!=subf.index]['index_right']))

#alternatively:             ['neighb','neighb',...]
#neighbors = t_groups.apply(lambda subf: list(subf[subf['index_right']!=subf.index]['index_right']))

#Got the neighbors
df['neighbors'] = neighbors



#TODO
# ===========  WEIGHTED AVERAGES? PREPROCESS DISTRICTS ===========
#df_tmp = df.copy(deep=False)
#df_tmp['dem_votes'] = df['dem_vote_fraction'] * df['total_votes']
#
#df2 = df[['district', 'dem_votes','total_votes','geometry']]
#districts = df[['US_HOUSE','AV','geometry']].dissolve(by="US_HOUSE", aggfunc='sum' )
#districts['dem_vote_fraction']


# ============ SIMPLIFY POLYGONS =============
# we can cut down the number of points needed dramatically while still
# having accurate enough shapes


def count_pts(d):
    if(d.geom_type == 'MultiPolygon'):
        return sum(count_pts(g) for g in d.geoms)
    else:
        return len(d.exterior.coords)
def total_pts(df):
    return sum(df.geometry.apply(count_pts))


df_simple = df.copy()
df_simple.geometry = df.simplify(tolerance=1E-4)
print("Points reduced from {} to {}".format(total_pts(df), total_pts(df_simple)))

df = df_simple

# results from testing:
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

out_df.to_csv(CURR_CONFIG["outfile"], sep='|')
