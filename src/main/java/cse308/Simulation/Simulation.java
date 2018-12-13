package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Users.UserAccount;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Simulation {
	protected static final AtomicInteger count = new AtomicInteger(0);
	protected int id;
	protected UserAccount user;
	protected SimulationParams params;	
	protected Map startingMap;
	protected Stack<Move> moves;
	protected Map currentMap; // startMap+all moves so far
	protected double currentGoodness; // goodness of the current map
	protected float progress = 0;
	boolean isPaused = false;
        boolean savable=false;

	public Simulation( UserAccount u,SimulationParams s) {
            id = count.incrementAndGet();
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

	public boolean isDone() {
            return getProgress() == 1;
	}

	public abstract void doStep();

	public abstract void pickMove();

	public void updateGUI() {
            // send progress to client
            //Objective function for each relevant district
	}

	public void queueForWork() {
            SimulationManager.getSimWorker().addToRunQueue(this);
	}

	public void removeFromQueue() {
            SimulationManager.getSimWorker().removeFromRunQueue(this);
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
}