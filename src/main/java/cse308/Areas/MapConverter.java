package cse308.Areas;

import cse308.Data.DistrictRepository;
import cse308.Data.PrecinctRepository;
import cse308.Data.StateRepository;
import cse308.Simulation.Move;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.swing.plaf.nimbus.State;


@Component
@Converter
@Configurable
public class MapConverter implements AttributeConverter<Map, String> {

    private static DistrictRepository distRepo;
    private static PrecinctRepository precRepo;
    private static StateRepository stateRepo;

    @Autowired
    public void initStateRepository(StateRepository r){ stateRepo = r; }
    @Autowired
    public void initDistrictRepository(DistrictRepository r){ distRepo = r; }
    @Autowired
    public void initPrecinctRepository(PrecinctRepository r){ precRepo = r; }

    @Override
    public String convertToDatabaseColumn(Map map) {
        //TODO: add state column
        JSONObject json = new JSONObject();
        json.put("state", map.getState().getId());

        json.put("data", map.toJSON());

        return json.toString();
    }

    @Override
    public Map convertToEntityAttribute(String s) {

        JSONObject json = new JSONObject(s);

        String stateId = json.getString("state");
        MasterState state = stateRepo.findById(stateId).get();

        System.out.println("MapConverter: Loading map for " + stateId);

        Map map = new Map(state);

        JSONObject data = json.getJSONObject("data");
        for(String distId : data.keySet()) {
            MasterDistrict dist = distRepo.findById(distId).get();


            JSONArray precincts = data.getJSONArray(distId);

            for(int i = 0; i < precincts.length(); i++) {
                String precId = precincts.getString(i);
                MasterPrecinct precinct = precRepo.findById(precId).get();

                map.apply(new Move(map.getPrecinct(precinct), map.getDistrict(dist)));
            }
        }
        return map;
    }
}
