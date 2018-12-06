package cse308;

import java.util.ArrayList;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterState;

@RestController
public class StateController {

	@RequestMapping(value = "/api/state", method = RequestMethod.GET, produces = "application/json")
	public String getDistrictsForState() {
		MasterDistrict a = new MasterDistrict("District 1 of NJ");
		a.setID(22);
		return a.jsonFormat();
	}
	
//	@RequestMapping(value = "/api/run", method = RequestMethod.GET)
//	public Geometry getDistrictBoundary() {
//    	GeometryFactory geometryFactory = new GeometryFactory();
//    	Coordinate a = new Coordinate(-83.86962890625,40.94671366508002);
//    	Coordinate b = new Coordinate(-84.24316406249999,40.12849105685408);
//    	Coordinate c = new Coordinate(-83.75976562499999, 39.825413103424786);
//    	Coordinate d = new Coordinate(-83.07861328125,40.06125658140474);
//    	Coordinate e = new Coordinate(-83.86962890625,40.94671366508002);
//    	Coordinate[] coord = new Coordinate[5];
//    	coord[0]=a;
//    	coord[1]=b;
//    	coord[2]=c;
//    	coord[3]=d;
//    	coord[4]=e;
//    	Polygon pol1 = geometryFactory.createPolygon(coord);
//    	
//    	Coordinate f = new Coordinate(-83.86962890625,40.94671366508002);
//    	Coordinate g = new Coordinate(-83.07861328125,40.094882122321145);
//        Coordinate h = new Coordinate(-82.705078125,40.329795743702064);
//    	Coordinate i = new Coordinate(-82.55126953124999,40.730608477796636);
//    	Coordinate j = new Coordinate(-82.5732421875,41.11246878918088);
//    	Coordinate k = new Coordinate(-83.86962890625,40.94671366508002);
//    	Coordinate[] coord1 = new Coordinate[5];
//    	coord1[0]=f;
//    	coord1[1]=g;
//    	coord1[2]=h;
//    	coord1[3]=i;
//    	coord1[4]=j;
//    	coord1[5]=k;
//    	Polygon pol2 = geometryFactory.createPolygon(coord1);
//    	
////    	Polygon[] polArray = new Polygon[2];
////    	polArray[0]=pol1;
////    	polArray[1]=pol2;
//    	
//    	ArrayList<Polygon> polArray = new ArrayList<>();
//    	polArray.add(pol1);
//    	polArray.add(pol2);
//    	
//    	CascadedPolygonUnion polUnion = new CascadedPolygonUnion(polArray);
//    	return null;
//    }
}