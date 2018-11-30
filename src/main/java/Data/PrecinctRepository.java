package Data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import Areas.MasterPrecinct;

public interface PrecinctRepository extends CrudRepository<MasterPrecinct, Integer>{
<<<<<<< HEAD
	
	public Set<MasterPrecinct> findByStateId(int stateId);
	
	
=======
//
>>>>>>> cb1d8834fd6801dd95b43cbe5c2f557875a412ff
}
