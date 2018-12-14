package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Users.UserAccount;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public abstract class Simulation {
	
	@Id
	protected int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	protected UserAccount user;
	
	@Type(type="serializable")
	protected SimulationParams params;
	
	@Type(type="serializable")
	protected Map startingMap;
	
	@Type(type="serializable")
	protected Stack<Move> moves;
	
	@Type(type="serializable")
	protected Map currentMap; // startMap+all moves so far
	protected float currentGoodness; // goodness of the current map
	protected float progress = 0;
	boolean isPaused = false;

	public Simulation( UserAccount u, SimulationParams s) {
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