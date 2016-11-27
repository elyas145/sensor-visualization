package Elyas.model.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Elyas.model.Sensor;

/**
 * @author Elyas Syoufi
 *         <p>
 *         Abstract Algorithm class. defines an outline that an algorithm must
 *         use in order to move the sensors. this class provides the basic
 *         functionality that an algorithm will need.
 *         </p>
 */
public abstract class Algorithm {
	private int moves;
	private List<Sensor> sensors;
	private List<MoveListener> moveListeners;
	private List<FinishListener> finishListeners;
	private float start;
	private float end;
	private float precision;

	/**
	 * returns true if a and b are equal with the <code>precision</code> value.
	 * 
	 * @see setPositionPrecision(float p)
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return true if <code>a == b</code>, false otherwise
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
	 * returns true if a is less than b with the <code>precision</code> value.
	 * 
	 * @see setPositionPrecision(float p)
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return true if <code>a &lt; b</code>, false otherwise
	 */
	protected boolean lessPosition(float a, float b) {
		if (a < b && a + precision < b) {
			return true;
		}
		return false;
	}

	/**
	 * returns true if a is less than or equal to b with the
	 * <code>precision</code> value.
	 * 
	 * @see setPositionPrecision(float p)
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return true if <code> a &lt;= b </code>, false otherwise
	 */
	protected boolean lessEqualPosition(float a, float b) {

		return lessPosition(a, b) || equalPosition(a, b);
	}

	/**
	 * controls how much error is allowed between the position of the sensors.
	 * as in, how much room is allowed between sensors, or how much they are
	 * allowed to overlap.
	 * 
	 * @param p
	 *            the precision value
	 */
	protected void setPositionPrecision(float p) {
		precision = p;
	}

	/**
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

	/**
	 * add a listener to notify when the algorithm is completed.
	 * 
	 * @param listener
	 */
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

	/**
	 * @return the start position. (most likely 0.0)
	 */
	protected float getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start position to be set. (most likely 0.0)
	 */
	protected void setStart(float start) {
		this.start = start;
	}

	/**
	 * @return the end position. (most likely 1)
	 */
	protected float getEnd() {
		return end;
	}

	/**
	 * @param the
	 *            end position. (most likely 1)
	 */
	protected void setEnd(float end) {
		this.end = end;
	}

	/**
	 * register that a move has been made to the sensor(s). informs any
	 * listeners of the move.
	 * 
	 * @param s
	 *            the sensor that just moved.
	 */
	protected void addMove(Sensor s) {
		moves++;
		for (MoveListener listener : moveListeners) {
			listener.onMove(s);
		}
	}

	/**
	 * @return the sensors currently used in this algorithm
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
	 * function. this function will be run in a separate thread. any GUI calls
	 * should be explicitly run on the GUI thread.
	 */
	protected abstract void toDo();

}
