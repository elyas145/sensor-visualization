/**
 * Abstract Algorithm class. defines an outline that 
 * an algorithm must use in order to move the sensors.
 * 
 */
package Elyas.model.algorithms;

import java.util.ArrayList;
import java.util.List;

import Elyas.model.Sensor;

public abstract class Algorithm {
	private int moves;
	private List<Sensor> sensors;
	private List<MoveListener> moveListeners;
	private List<FinishListener> finishListeners;

	/**
	 * 
	 * @return number of sensor moves taken so far.
	 */
	public int getNumberOfMoves() {
		return moves;
	}

	/**
	 * adds a listener to notify when a sensor move is made.
	 * @param moveListener
	 */
	public void addMoveListener(MoveListener moveListener) {
		moveListeners.add(moveListener);
	}

	/**
	 * initializes the sensors to be used in this algorithm
	 * @param sensors
	 */
	protected Algorithm(List<Sensor> sensors) {
		this.sensors = sensors;
		moveListeners = new ArrayList<>();
		finishListeners = new ArrayList<>();
	}

	/**
	 * register that a move has been made to the sensor(s)
	 * informs any listeners of the move.
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
	 * method to be called when the algorithm is ready to be run.
	 * simple convenience method to run the algorithm in a separate thread.
	 * notifies any finish listeners when the algorithm is finished executing.
	 */
	protected void startAlgorithm() {
		Thread thread = new  Thread(()->{
			toDo();
			notifyFinish();
		});
		thread.start();
	}

	/**
	 * notifies any listeners that the algorithm has finished executing.
	 */
	private void notifyFinish() {
		for(FinishListener finishListener : finishListeners){
			finishListener.onFinish();
		}
	}

	/**
	 * the main algorithm function.
	 * anything the algorithm does goes in this function.
	 * this function will be run in a separate thread.
	 */
	protected abstract void toDo();
	
}
