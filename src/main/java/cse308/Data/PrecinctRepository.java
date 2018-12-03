package cse308.Data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import cse308.Areas.MasterPrecinct;

public interface PrecinctRepository extends CrudRepository<MasterPrecinct, Integer>{
	
	public Set<MasterPrecinct> findByStateId(int stateId);
	
}
