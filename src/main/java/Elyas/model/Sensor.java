package Elyas.model;

public class Sensor {
	private double radius;
	private double position;
	
	public Sensor(){
		radius = 0.1;
		position = 0.0;
	}
	public Sensor(double radius, double position){
		this.radius = radius;
		this.position = position;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	
}
