package cse308.Simulation;

import cse308.Areas.MasterState;
import cse308.Data.StateRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Component
@Converter
@Configurable
public class SimParamsConverter implements AttributeConverter<SimulationParams, String> {


    private static StateRepository stateRepo;
    @Autowired
    public void initStateRepo(StateRepository r){stateRepo = r;}

    @Override
    public String convertToDatabaseColumn(SimulationParams simulationParams) {
        return simulationParams.toJSON().toString();
    }

    @Override
    public SimulationParams convertToEntityAttribute(String s) {
        JSONObject json = new JSONObject(s);
        JSONObject b = json.getJSONObject("functionWeights");


        FunctionWeights functionWeights = new FunctionWeights(
                b.getFloat("wCompactness"),
                b.getFloat("wPopulationEquality"),
                b.getFloat("wPartisanFairness"));


        String stateId = json.getString("state");
        MasterState state = stateRepo.findById(stateId).get();


        String algorithm = json.getString("algorithm");
        SimulationParams simulationParams;
        if(algorithm.equals("REGION_GROWING")){
            simulationParams = new RegionGrowingParams(
                    functionWeights,
                    state,
                    algorithm
            );
        }
        else{
            simulationParams = new SimulatedAnnealingParams(
                    functionWeights,
                    state,
                    algorithm,
                    new cse308.Areas.Map(state)
            );
        }

        return simulationParams;
    }
}
