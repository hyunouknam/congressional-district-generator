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
	protected float currentGoodness; // goodness of the current map
	protected float progress = 0;
	boolean isPaused = false;

	public Simulation( UserAccount u, SimulationParams s) {
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

	public abstract void doStep() throws CloneNotSupportedException;

	public abstract void pickMove() throws CloneNotSupportedException;

	public void postUpdate() {
		// send progress
	}

	public void queueForWork() {
		SimulationWorker.addToRunQueue(this);
	}

	public void removeFromQueue() {
		SimulationWorker.removeFromRunQueue(this);
	}

	public float getGoodness() {
		return currentMap.getGoodness();
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