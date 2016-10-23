/**
 * This algorithm puts all the sensors back to back, without any overlay. 
 * if there are too many sensors, then the sensors on the end of the range will overlay. 
 * if there are no enough sensors, then all the uncovered area will be at the end of the range.
 */
package Elyas.model.algorithms;

import java.util.Comparator;
import java.util.List;

import Elyas.model.Sensor;

public class BackToBack extends Algorithm {

	public BackToBack(List<Sensor> sensors) {
		super(sensors);
	}

	@Override
	protected void toDo() {
		getSensors().sort(new Comparator<Sensor>() {
			@Override
			public int compare(Sensor o1, Sensor o2) {
				if(o1.getPosition() < o2.getPosition()){
					return -1;
				}else if(o1.getPosition() > o2.getPosition()){
					return 1;
				}
				return 0;
			}
		});
		
		for(int i = 0; i < getSensors().size(); i++){
			Sensor curr = getSensors().get(i);
			Sensor next = null;
			if(i < getSensors().size() - 2){
				next = getSensors().get(i+1);
			}
			
			if(i == 0){
				
			}
		}
	}

}
