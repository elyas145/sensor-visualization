/**
 * Abstract Algorithm class. defines an outline that 
 * an algorithm must use in order to move the sensors.
 * 
 */
package Elyas.model.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Elyas.model.Sensor;

public abstract class Algorithm {
	private int moves;
	private List<Sensor> sensors;
	private List<MoveListener> moveListeners;
	private List<FinishListener> finishListeners;
	private float start;
	private float end;
	private float precision;

	/**
	 * returns true if a and b are equal with the set precision value.
	 */
	protected boolean equalPosition(float a, float b) {
		if (a < b && a + precision < b) {
			return false;
		}
		if (b < a && b + precision < a) {
			return false;
		}
		return true;
	}

	/**
	 * returns true if a < b with the set precision value.
	 */
	protected boolean lessPosition(float a, float b) {
		if (a < b && a + precision < b) {
			return true;
		}
		return false;
	}

	/**
	 * returns true if a <= b with the set precision value.
	 */
	protected boolean lessEqualPosition(float a, float b) {

		return lessPosition(a, b) || equalPosition(a, b);
	}

	protected void setPositionPrecision(float p) {
		precision = p;
	}

	/**
	 * 
	 * @return number of sensor moves taken so far.
	 */
	public int getNumberOfMoves() {
		return moves;
	}

	/**
	 * adds a listener to notify when a sensor move is made.
	 * 
	 * @param moveListener
	 */
	public void addMoveListener(MoveListener moveListener) {
		moveListeners.add(moveListener);
	}

	public void addFinishListener(FinishListener listener) {
		finishListeners.add(listener);
	}

	/**
	 * initializes the sensors to be used in this algorithm
	 * 
	 * @param sensors
	 * @param end
	 * @param start
	 */
	protected Algorithm(Collection<Sensor> sensors, float start, float end) {
		this.sensors = new ArrayList<Sensor>(sensors);
		moveListeners = new ArrayList<>();
		finishListeners = new ArrayList<>();
		this.start = start;
		this.end = end;
		this.precision = 0.01f;
	}

	protected float getStart() {
		return start;
	}

	protected void setStart(float start) {
		this.start = start;
	}

	protected float getEnd() {
		return end;
	}

	protected void setEnd(float end) {
		this.end = end;
	}

	/**
	 * register that a move has been made to the sensor(s) informs any listeners
	 * of the move.
	 */
	protected void addMove(Sensor s) {
		moves++;
		for (MoveListener listener : moveListeners) {
			listener.onMove(s);
		}
	}

	/**
	 * @return the sensors currently used.
	 */
	protected List<Sensor> getSensors() {
		return sensors;
	}

	/**
	 * method to be called when the algorithm is ready to be run. simple
	 * convenience method to run the algorithm in a separate thread. notifies
	 * any finish listeners when the algorithm is finished executing.
	 */
	public void startAlgorithm() {
		Thread thread = new Thread(() -> {
			toDo();
			notifyFinish();
		});
		thread.start();
	}

	/**
	 * notifies any listeners that the algorithm has finished executing.
	 */
	private void notifyFinish() {
		for (FinishListener finishListener : finishListeners) {
			finishListener.onFinish();
		}
	}

	/**
	 * the main algorithm function. anything the algorithm does goes in this
	 * function. this function will be run in a separate thread.
	 */
	protected abstract void toDo();

}
