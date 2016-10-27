/**
 * This algorithm puts all the sensors back to back, without any overlay. 
 * if there are too many sensors, then the sensors that are not needed are not moved. 
 * if there are no enough sensors, then all the uncovered area will be at the end of the range.
 */
package Elyas.model.algorithms;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import Elyas.model.Sensor;

public class BackToBack extends Algorithm {

	public BackToBack(Collection<Sensor> sensors, float start, float end) {
		super(sensors, start, end);
	}

	@Override
	protected void toDo() {
		getSensors().sort(new Comparator<Sensor>() {
			@Override
			public int compare(Sensor o1, Sensor o2) {
				if (o1.getPosition() < o2.getPosition()) {
					return -1;
				} else if (o1.getPosition() > o2.getPosition()) {
					return 1;
				}
				return 0;
			}
		});

		float currentEdge = getStart();
		boolean done = false;
		for (int i = 0; i < getSensors().size(); i++) {
			Sensor curr = getSensors().get(i);
			if (curr.getPosition() - curr.getRadius() != currentEdge) {
				if (currentEdge + curr.getRadius() <= getEnd()) {
					curr.setPosition(currentEdge + curr.getRadius());
					super.addMove(curr);
					currentEdge = curr.getPosition() + curr.getRadius();
				} else {
					if (!done) {
						curr.setPosition(getEnd() - curr.getRadius());
						done = true;
						super.addMove(curr);
						currentEdge = getEnd();
					} else {
						// whole range is covered. we have too many sensors. no
						// need to move anything.
					}
				}
			}else{
				currentEdge = curr.getPosition() + curr.getRadius();
			}
		}
	}

}
