package Elyas.model;

import java.math.BigDecimal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.StringProperty;

public class Sensor {
	private float radius;
	private FloatProperty position;
	
	public Sensor(){
		radius = 0.1f;
		position = new SimpleFloatProperty(0.0f);
	}
	public Sensor(float radius, float position){
		this.radius = radius;
		this.position = new SimpleFloatProperty(position);
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public float getPosition() {
		return position.get();
	}
	public void setPosition(float bigDecimal) {
		this.position.set(bigDecimal);
	}
	public FloatProperty positionProperty() {
		return position;
	}
	
}
