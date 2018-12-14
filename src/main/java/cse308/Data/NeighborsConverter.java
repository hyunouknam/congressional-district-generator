package cse308.Data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import cse308.Areas.MasterPrecinct;

public class NeighborsConverter implements AttributeConverter<Set<MasterPrecinct>, String>{
	
	@Autowired
	private PrecinctRepository prc;
	

	@Override
	public String convertToDatabaseColumn(Set<MasterPrecinct> attribute) {
		// TODO Auto-generated method stub
		throw new NotImplementedException("Not Implemented");
	}

	@Override
	public Set<MasterPrecinct> convertToEntityAttribute(String dbData) {
		System.out.println(dbData);
		Set<MasterPrecinct> neighbors = new HashSet<>();
		String[] nb = dbData.split(",");
		for(int a=0; a<nb.length; a++) {
			Optional<MasterPrecinct> prec = prc.findById(nb[a]);
			neighbors.add(prec.orElseThrow());
		}
		return neighbors;
	}

}
