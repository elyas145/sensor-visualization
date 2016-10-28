package Elyas.model.expirement;

import java.util.List;
import java.util.Map;

public class Expirement {
	/*
	 * list: the values are kept for every iteration. usefull for viewing the
	 * data, but uses more memory. should not be used for large numbers.
	 * 
	 * average: only stores the average of all the iterations. easier on memory,
	 * but data not available for display.
	 */
	public static enum Type {
		LIST, AVERAGE
	};
	
	//strategy pattern, to set the proper type of data stored.
	private ExpirementType expirement;
	
	String x;
	String y;

	public Expirement(String x, String y, Type type){
		this.x = x;
		this.y = y;
	}
}
