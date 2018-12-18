package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Areas.MapConverter;
import cse308.Users.UserAccount;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name="sims")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Simulation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;

	@ManyToOne
	protected UserAccount user;

	@Convert(converter = SimParamsConverter.class)
	protected SimulationParams params;

	protected double currentGoodness; // goodness of the current map
	protected float progress = 0;

	@Transient
	boolean isPaused = false;
	@Transient
    boolean savable=false;

	@Transient
	protected Stack<Move> moves;
    @Transient
	protected Map startingMap;


	@Convert(converter = MapConverter.class)
	protected Map currentMap; // startMap+all moves so far

	public Simulation( UserAccount u,SimulationParams s) {
            params = s;
            user = u;
            moves = new Stack<>();
	}
	
	public void setID(int id) {
            this.id = id;
	}

	public float getProgress() {
            return progress;
	}

	public abstract void updateProgress();

	public abstract boolean isDone();

	public abstract void doStep();

	public abstract void pickMove();

	public void updateGUI() {
		throw new NotImplementedException("Not Implemented Update GUI");
	}

	public void queueForWork() {
            SimulationManager.getInstance().getSimWorker().addToRunQueue(this);
	}

	public void removeFromQueue() {
            SimulationManager.getInstance().getSimWorker().removeFromRunQueue(this);
	}

	public double getGoodness() {
            currentGoodness=ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights,currentMap);
            return currentGoodness;
	}

	public String getJSON() {
		String s = "{";
		s = s + "\"id\":";
		s = s + "\"" + id + "\"";
		s = s + ", \"params\":";
		s = s + params.getJSON();
		s = s + "}";
		return s;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("params", params.toString()); //TODO TEMP

		JSONArray maps = new JSONArray();
		maps.put(this.currentMap.toJSON());
		json.put("data", maps);
		return json;
	}
}