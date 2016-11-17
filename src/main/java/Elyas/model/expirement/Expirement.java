package Elyas.model.expirement;

import java.util.HashMap;
import java.util.Map;

public class Expirement {

	private String x;
	private String y;
	private int runs;
	private Map<Integer, Integer> moves;

	public Expirement(String x, String y, int runsPerValue) {
		this.x = x;
		this.y = y;
		this.runs = runsPerValue;
		moves = new HashMap<>();
	}

	public void addValue(Integer numberOfSensors, Integer moves, Integer currentRun) {
		if (this.moves.containsKey(numberOfSensors)) {
			int m = this.moves.get(numberOfSensors) + moves;
			this.moves.put(numberOfSensors, m);
		} else {
			this.moves.put(numberOfSensors, moves);
		}
		if (currentRun >= runs) {
			int m = this.moves.get(numberOfSensors) / runs;
			this.moves.put(numberOfSensors, m);
		}

	}
}
