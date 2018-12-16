package cse308.Data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import cse308.Areas.MasterPrecinct;

public interface PrecinctRepository extends CrudRepository<MasterPrecinct, String>{
//	Set<MasterPrecinct> findByStateId(int stateId);
	Set<MasterPrecinct> findByDefaultDistrictId(String districtId);
	
}
