import numpy as np
import pandas as pd
from pprint import pprint
import json
from json import encoder
# We end up reencoding coordinate floats
# let's only use 6 decimals of precision, as the gives us 10cm accuracy
encoder.FLOAT_REPR = lambda x: format(x, '.6f')

"""
INFILE = "./NJ/nj_final.json"
FULL_CSV_OUT = "./nj_full.csv"
PRUNED_JSON_OUT = "./nj_clean.json"

CLEAN_GEOJSON_OUT = "./nj_clean.geo.json"
"""

INFILE = "./CT/ct_final.json"
FULL_CSV_OUT = "./ct_full.csv"
PRUNED_JSON_OUT = "./ct_clean.json"

CLEAN_GEOJSON_OUT = "./ct_clean.geo.json"

"""
Data column notes:

{features: [
    {geometry: {...},
     properties:{ "all the data we're interested in is here" }
     },...
]}

useful properties:
    NAME10 #best precinct name
    TOWN_NAME
    COUNTY_NAM
    MARKET_NAM

    INTPTLAT10 #location
    INTPTLON10

    GEOID10 # UNIQUE ID geographic id from 2010 census
    CTYVTD #county code + census voting district code (not quite unique)

    #STATE_SENA #nj senate district
    #STATE_HOUS #nj house distrcit

    POP100 #total pop from census
    VAP #voting age population from census
    VOTES #total votes in 2008

    # accumulated across 10 elections
    AV #average vote percentage going to democrats (i.e. 0.6 = 60% Dem) 
    NRV #avg number of votes R
    NDV #avg votes D
    
"""

#index by GEOID10
#List of columns we want to rename and keep for our final data
"""
PROP_MAP = {
    "GEOID10": "uid",
    "NAME10": "precinct_name",
    "TOWN_NAME": "town_name",
    "COUNTY_NAM": "county_name",

    "INTPTLAT10": "lat",
    "INTPTLON10": "lon",
    "geometry": "geometry",

    "POP100": "population",
    "VAP": "voting_population",
    "VOTES": "total_votes",
    "AV": "fraction_votes_dem",
}
"""
PROP_MAP = {
    "GEOID10": "uid",
    "NAME10": "precinct_name",
    "TOWN": "town_name",
    "COUNTY_NUM": "county_name",

    "INTPTLAT10": "lat",
    "INTPTLON10": "lon",
    "geometry": "geometry",

    "POP100": "population",
    "VAP": "voting_population",
    "TOTAL": "total_votes",
    "DEM_PCT": "fraction_votes_dem",
}

#We only need 6 digits of precision on our floats, this will save space
#recursively go through json obj, rounad all floats
def roundFloats(obj):
    if isinstance(obj, list):
        return [roundFloats(e) for e in obj]
    elif isinstance(obj, dict):
        return {key: roundFloats(val) for key,val in obj.items()}
    elif isinstance(obj, tuple):
        return (roundFloats(e) for e in obj)
    elif isinstance(obj, float):
        return round(obj, 6)
    else:
        return obj
        

with open(INFILE) as f:
    j = json.load(f);

    #sj = schemify(j)
    #pprint(sj)

    assert(len(j.keys()) == 2)
    features = j["features"]

    #flatten features, include str(geometry) with properties

    #geometry gets pulled out of its own property, rounded
    extract_geometry = lambda f: roundFloats(f["geometry"])
    flattened_features = [ {"geometry": roundFloats(f["geometry"]), **f["properties"]} 
                            for f in features]

    df = pd.DataFrame.from_records(flattened_features, index="GEOID10")
    assert len(df) > 10, "Failed to load records (theres <10 of them) "


    #def pruneCoords(geom):
    #    coords = geom["coordinates"][0]
    #    if len(coords) >= 4:
    #        coords[:] = coords[:2] + ["..."]
    #    return geom
    #df["geometry"] = df["geometry"].apply(pruneCoords)


    #take only desired properties (exclude index)
    desired_props = [k for k in PROP_MAP.keys() if k != df.index.name]


    # Rename all columns, also index according to PROP_MAP
    renamer = lambda x: PROP_MAP.get(x,x) #rename columns if applicable, else do nothing
    df_pruned = df[desired_props].rename(index=renamer, columns=renamer)
    df_pruned.index.rename(renamer(df_pruned.index.name), inplace=True)




    # ========= OUTPUT AS FLAT FORMAT
    # { "PRECINCT_ID" : { "prop1": "val", "prop2": "val", "geometry": [[1,1],[1,0]...] } }
            #df.to_csv(FULL_CSV_OUT)
            #df_pruned.to_csv(PRUNED_CSV_OUT, sep='|')
    #df_pruned.to_json(PRUNED_JSON_OUT, orient="index")

    # ======== OUTPUT AS GEOJSON
    def rowToGeoJSON(row): #row is of type Series
        geomStr = json.dumps(row['geometry']);

        #get all properties out of dataframe (ignore geometry), also id
        props = row.drop('geometry').to_dict()
        props['id'] = row.name

        # numbers get encoded as numpy datatypes which cant be serialized, so we convert them
        # to basic python types
        for key,val in props.items():
            if(isinstance(val, np.number)):
                props[key] = val.item() #convert numpy number to python number

        propsStr = json.dumps(props)

        featureStr = '{"type":"Feature","geometry":'+geomStr+',"properties":'+propsStr+'}'
        return featureStr
    

    def writeGeoJson():

        with open(CLEAN_GEOJSON_OUT, 'w') as outf:
            outf.write('{"type":"FeatureCollection", "features": [\n')

            firstRow=True
            for index, row in df_pruned.iterrows():
                if not firstRow:
                    outf.write(",");
                outf.write("\n");
                outJson = rowToGeoJSON(row)
                outf.write(outJson)
                firstRow = False;
            
            outf.write("\n");
            outf.write("]}");

    writeGeoJson()

