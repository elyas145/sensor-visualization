package Elyas.model.expirement;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Elyas Syoufi
 *         <p>
 *         keeps track of an experiment, and does the appropriate calculations
 *         for an accurate result. note, this class has nothing to do with an
 *         algorithm.
 *         </p>
 */
public class Experiment {

	private String x; // label for the x axis
	private String y; // label for the y axis
	private int runs; // number of times an algorithm is run on a number of
						// sensors
	private Map<Integer, Integer> moves; // number of moves for the number of
											// sensors
	private Map<Integer, Float> distances;

	private String fn; // the f(n) function for the radius of the sensors.

	public Experiment(String fn, String x, String y, int runsPerValue) {
		this.x = x;
		this.y = y;
		this.runs = runsPerValue;
		moves = new HashMap<>();
		distances = new HashMap<>();
		this.fn = fn;
	}

	/**
	 * registers a number of moves to the number of sensors. if this is the last
	 * run, then the average of the moves is calculated. otherwise it is just
	 * added to the last value of moves.
	 * 
	 * @param numberOfSensors
	 *            the current number of sensors
	 * @param numberOfMoves
	 *            the number of moves taken to cover the range
	 * @param currentRun
	 *            the current run the algorithm was run with this number of
	 *            sensors
	 */
	public void addMove(Integer numberOfSensors, Integer numberOfMoves, Integer currentRun) {
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

	/**
	 * registers a distance to the number of sensors. if this is the last run,
	 * then the average of the distances is calculated. otherwise it is just
	 * added to the last value of distances.
	 * 
	 * @param numberOfSensors
	 *            the current number of sensors
	 * @param distance
	 *            the taken taken to cover the range
	 * @param currentRun
	 *            the current run the algorithm was run with this number of
	 *            sensors
	 */
	public void addDistance(Integer numberOfSensors, float distance, Integer currentRun) {

		if (this.distances.containsKey(numberOfSensors)) {

			float m = this.distances.get(numberOfSensors) + distance;
			this.distances.put(numberOfSensors, m);
		} else {
			this.distances.put(numberOfSensors, distance);
		}
		if (currentRun >= runs) {
			float m = this.distances.get(numberOfSensors) / runs;
			this.distances.put(numberOfSensors, m);
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

	public Map<Integer, Float> getDistances() {
		return distances;
	}

	public void setDistances(Map<Integer, Float> distances) {
		this.distances = distances;
	}

	public String getFN() {
		return fn;
	}

}
