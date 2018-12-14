package cse308.Data;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

@Converter(autoApply = true)
public class GeometryConverter implements AttributeConverter<Geometry, String>{

	@Override
	public String convertToDatabaseColumn(Geometry attribute) {
		GeoJSONWriter w = new GeoJSONWriter();
		GeoJSON j = w.write(attribute);
		return j.toString();
	}

	@Override
	public Geometry convertToEntityAttribute(String dbData) {
		GeoJSONReader reader = new GeoJSONReader();
		return reader.read(dbData);
	}
	

}
