package Elyas.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

public class Sensor {
	private double radius;
	private DoubleProperty position;
	
	public Sensor(){
		radius = 0.1;
		position = new SimpleDoubleProperty(0.0);
	}
	public Sensor(double radius, double position){
		this.radius = radius;
		this.position = new SimpleDoubleProperty(position);
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getPosition() {
		return position.get();
	}
	public void setPosition(double position) {
		this.position.set(position);
	}
	public DoubleProperty positionProperty() {
		return position;
	}
	
}
