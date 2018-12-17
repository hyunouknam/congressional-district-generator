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

#TOPO STUFF:
#get everything ready
#then output df to build/NJ_clean_geojson.json
#then run shell commands to make build/NJ_simplified_topo.json
#then run shell commands to make build/NJ_simplified_geojson.json
#then load build.NJ_simplified_geojson.json, output as csv
#output neighbors csv
#copy NJ_simplified_topo.json to out folder

OUTFOLDER='./final_data/'
BUILD_FOLDER='./tmp/'

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


# ======================== PRETTY MUCH DONE
#NOW:
#do topological simplification with topojson scripts (shell commands)
#1: store a copy of the topojson in the out_dir
#2: store a temp copy of the simplified geojson
#3: read that in, and output as geopandas csv

from subprocess import call
from shutil import copyfile
import os
os.makedirs(BUILD_FOLDER, exist_ok=True) #make sure tmp exists

TEMP_CLEAN_GEOJSON_FILE = f'{BUILD_FOLDER}/{CURR_CONFIG["state"]}_clean_geojson.json'
TEMP_CLEAN_TOPO_FILE = f'{BUILD_FOLDER}/{CURR_CONFIG["state"]}_clean_topo.json'
TEMP_SIMPLIFIED_TOPO_FILE = f'{BUILD_FOLDER}/{CURR_CONFIG["state"]}_simplified_topo.json'
TEMP_SIMPLIFIED_GEOJSON_FILE = f'{BUILD_FOLDER}/{CURR_CONFIG["state"]}_simplified_geojson.json'

OUT_TOPO_FILE= f'{OUTFOLDER}/{CURR_CONFIG["state"]}_topo.json'

#Output df as geojson to tmp
with open(TEMP_CLEAN_GEOJSON_FILE, 'w') as temp_outf:
    temp_outf.write(df.to_json())


#convert geojson to topojson
print("Converting to topology")
call(['npx','geo2topo', '-q', '1e8', 
    f'precincts={TEMP_CLEAN_GEOJSON_FILE}',
    '-o', TEMP_CLEAN_TOPO_FILE])
#npx geo2topo -q 1e8 nj=NJ_geo.json -o NJ_topo.json


#Simplify topojson
print("Simplifying topology")
#planar-quantile decides what fraction to keep
#i.e. 0.1 means keep 10% of the points
#it will always remove the easiest ones first
#0.5 looks flawless unless you zoom in reaaaal hard
#0.35 is fine
#0.2 is getting chunky in places but still looks acceptable
call(['npx', 'toposimplify', '--planar-quantile', '0.2',  
    TEMP_CLEAN_TOPO_FILE, 
    '-o', TEMP_SIMPLIFIED_TOPO_FILE])


print("Converting back to geojson")
#npx topo2geo nj=NJ_geo2.json < NJ_topo2.json
call(['npx', 'topo2geo', 
    '--in', TEMP_SIMPLIFIED_TOPO_FILE,
    f'precincts={TEMP_SIMPLIFIED_GEOJSON_FILE}'])


#### We've created our files now just copy the topo file to outdir
print("Copying topo file to output")
copyfile(TEMP_SIMPLIFIED_TOPO_FILE, OUT_TOPO_FILE)


# ================= FINAL STAGE, OUTPUT GEOJSON AS CSV FOR DB
# load TEMP_SIMPLIFIED_GEOJSON_FILE into geopandas, output as csv

print("Loading simplified json {}".format(TEMP_SIMPLIFIED_GEOJSON_FILE), flush=True)
df_simple = gp.read_file(TEMP_SIMPLIFIED_GEOJSON_FILE)
df_simple = df_simple.set_index('id')


# ============== OUTPUT ===============

# convert all geometries to json
def geom_to_json(geom):
    return json.dumps(shapely.geometry.mapping(geom))

df_simple['geometry'] = df_simple['geometry'].apply(geom_to_json)

#df is still a geodatafram, but there's nothing geographic about it
out_df = pd.DataFrame(df_simple)

outfile = CURR_CONFIG["outfile"]
print("Outputting |-delimited csv to {}".format(outfile))
out_df.to_csv(outfile, sep='|')
