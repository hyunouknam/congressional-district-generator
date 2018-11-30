package Data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import Areas.MasterPrecinct;

public interface PrecinctRepository extends CrudRepository<MasterPrecinct, Integer>{
	
	public Set<MasterPrecinct> findByStateId(int stateId);
	
	
}
