package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Areas.MapConverter;
import cse308.Users.UserAccount;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name="sim")
public class SavedSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    //@ManyToOne
    protected String user;

    @Convert(converter = SimParamsConverter.class)
    @Column(columnDefinition="TEXT")
    protected SimulationParams params;

    protected double currentGoodness; // goodness of the current map
    protected float progress = 0;

    //@Transient
    //boolean isPaused = false;
    //@Transient
    //boolean savable=false;


    @Convert(converter = MapConverter.class)
    @Column(columnDefinition="TEXT")
    protected Map currentMap; // startMap+all moves so far


    public SavedSimulation() {}

    public SavedSimulation(UserAccount user, SimulationParams params, Map startingMap) {
        this.params = params;
        this.user = user.getEmail();
        this.currentMap = startingMap;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public SimulationParams getParams() { return params; }
    public void setParams(SimulationParams params) { this.params = params; }

    public double getCurrentGoodness() { return currentGoodness; }
    public void setCurrentGoodness(double currentGoodness) { this.currentGoodness = currentGoodness; }

    public float getProgress() { return progress; }
    public void setProgress(float progress) { this.progress = progress; }

    //public boolean isPaused() { return isPaused; }
    //public void setPaused(boolean paused) { isPaused = paused; }

    public Map getCurrentMap() { return currentMap; }
    public void setCurrentMap(Map currentMap) { this.currentMap = currentMap; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("params", params.toJSON());

        JSONArray maps = new JSONArray();
        maps.put(this.currentMap.toJSON());
        json.put("data", maps);
        return json;
    }
}
