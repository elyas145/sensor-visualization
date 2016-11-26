package Elyas.model.expirement;

import java.util.HashMap;
import java.util.Map;

public class Expirement {

	private String x;
	private String y;
	private int runs;
	private Map<Integer, Integer> moves;
	private String fn;

	public Expirement(String fn, String x, String y, int runsPerValue) {
		this.x = x;
		this.y = y;
		this.runs = runsPerValue;
		moves = new HashMap<>();
		this.fn = fn;
	}

	public void addMove(Integer numberOfSensors,Integer numberOfMoves, Integer currentRun) {
		if (this.moves.containsKey(numberOfSensors)) {
			int m = this.moves.get(numberOfSensors) + numberOfMoves;
			this.moves.put(numberOfSensors, m);
		} else {
			this.moves.put(numberOfSensors, numberOfMoves);
		}
		if (currentRun >= runs) {
			int m = this.moves.get(numberOfSensors) / runs;
			this.moves.put(numberOfSensors, m);
		}

	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public Map<Integer, Integer> getMoves() {
		return moves;
	}

	public void setMoves(Map<Integer, Integer> moves) {
		this.moves = moves;
	}

	public String getFN() {
		return fn;
	}

	
}
