package cse308.Data;//package cse308.Data;
//
//import org.springframework.data.repository.CrudRepository;
//
//import cse308.Areas.MasterDistrict;
//
//public interface DistrictRepository extends CrudRepository<MasterDistrict, Integer>{
////
//}

import cse308.Areas.MasterDistrict;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DistrictRepository extends CrudRepository<MasterDistrict, String> {
    //	Set<MasterPrecinct> findByStateId(int stateId);
    //Set<MasterDistrict> findByDefaultDistrictId(String districtId);

}
