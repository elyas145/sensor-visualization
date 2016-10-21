package Elyas.model;

public enum AxisType {
	SENSOR_RADIUS, NUMBER_OF_SENSORS; 
	
	public String toString(){
		switch(this){
		case NUMBER_OF_SENSORS:
			return "Number of Sensors";
		case SENSOR_RADIUS:
			return "Radius of Sensor";
		default:
			return "";
		
		}
	}
}
