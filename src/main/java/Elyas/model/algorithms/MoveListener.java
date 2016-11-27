package Elyas.model.algorithms;

import Elyas.model.Sensor;

/**
 * 
 * @author Elyas Syoufi
 *         <p>
 *         algorithm move listener. should be used by observers to be notified
 *         when the algorithm moves a sensor.
 *         </p>
 */
public interface MoveListener {
	public void onMove(Sensor s);
}
