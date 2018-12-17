package cse308.Data;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.json.Json;

public class Util {
    private Util(){}

    public static JSONObject geoRegionToJson(GeoRegion g) {
        JSONObject j = new JSONObject();
        j.put("population", g.getPopulation());
        j.put("voting_population", g.getVotingPopulation());
        j.put("total_votes", g.getTotalVotes());
        j.put("average_democrat_votes", g.getPercentDemocrat());

        Geometry geom = g.getGeometry(); //THIS IS VERY SLOW WHEN UNIONING PRECINCT BOUNDARIES TO DISTRICT
        j.put("geometry", geometryToJsonObj(geom));
        return j;
    }

    // I don't think we can make JSONObject out of Geometry directly

    public static JSONObject geometryToJsonObj(Geometry g) {

        GeoJSONWriter w = new GeoJSONWriter();
        GeoJSON json = w.write(g);
        //System.out.println(json.toString());
        //System.out.println("=========================================");
        JSONObject j = new JSONObject(json.toString());


        return j;
    }

}
