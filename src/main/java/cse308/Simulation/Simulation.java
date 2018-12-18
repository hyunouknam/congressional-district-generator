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

public abstract class Simulation {
	protected int id;


	protected SimulationParams params;

	protected double currentGoodness; // goodness of the current map
	protected float progress = 0;

	boolean isPaused = false;
    boolean savable=false;

	protected Stack<Move> moves;
	protected Map startingMap;


	protected Map currentMap; // startMap+all moves so far

	protected SavedSimulation savedSim;

	public Simulation() {
		moves = new Stack();
	}



	public Simulation(SimulationParams s) {
            params = s;
            moves = new Stack<>();
	}

	public void setId () { this.id = id; }
	public int getId () {return this.id;}

	public float getProgress() {
            return progress;
	}

	//Set where to push updates to
	public void setSavedSim(SavedSimulation savedSim){ this.savedSim = savedSim;}

	public abstract void updateProgress();

	public abstract boolean isDone();

	public abstract void doStep();

	public abstract void pickMove();

	public void updateGUI() {
	    if(this.savedSim != null){ this.savedSim.setCurrentMap(this.currentMap); }
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

}