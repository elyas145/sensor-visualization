package Elyas.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import Elyas.model.Sensor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DrawController implements Initializable {

	@FXML
	private Line rangeLine;
	@FXML
	private Line startLine;
	@FXML
	private Line endLine;
	@FXML
	private Label lblRangeStart;
	@FXML
	private Label lblRangeEnd;
	@FXML
	private Pane drawPane;

	private double height;
	private double width;
	private double lineOffset = 20;
	private double labelOffset = 10;
	private List<Sensor> sensors;
	private Double currentRadius;
	private Random random;
	private double rangeEnd;
	private double rangeStart;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensors = new ArrayList<>();
		random = new Random();
	}

	public void setDrawableArea() {
		width = drawPane.getWidth();
		height = drawPane.getHeight();

		// set the position of the range line
		rangeLine.setStartX(lineOffset);
		rangeLine.setEndX(width - lineOffset);
		rangeLine.setStartY(height / 2);
		rangeLine.setEndY(height / 2);

		// set the position of the start and end markers.
		startLine.setStartX(rangeLine.getStartX() - 10);
		startLine.setEndX(rangeLine.getStartX() - 10);
		startLine.setStartY(rangeLine.getStartY() - 20);
		startLine.setEndY(rangeLine.getStartY());

		endLine.setStartX(rangeLine.getEndX() - 10);
		endLine.setEndX(rangeLine.getEndX() - 10);
		endLine.setStartY(rangeLine.getEndY() - 20);
		endLine.setEndY(rangeLine.getEndY());

		// set start and end labels.
		lblRangeStart.setLayoutX(startLine.getStartX());
		lblRangeStart.setLayoutY(startLine.getEndY() + labelOffset);
		lblRangeEnd.setLayoutX(endLine.getStartX());
		lblRangeEnd.setLayoutY(endLine.getEndY() + labelOffset);
	}

	public void setRange(double from, double to) {
		rangeStart = from;
		rangeEnd = to;
		lblRangeStart.setText("" + from);
		lblRangeEnd.setText("" + to);
	}

	public void setNumberOfSensors(int n) {
		if (n == sensors.size()) {
			return;
		}
		System.out.println("updating number of sensors: " + n);
		while (n > sensors.size()) {
			Sensor sensor = new Sensor();
			sensor.setRadius(currentRadius);
			
			sensors.add(sensor);			
		}
		while (n < sensors.size()) {
			sensors.remove(sensors.size()-1);
		}
		for(Sensor s : sensors){
			s.setPosition(rangeStart + (rangeEnd - rangeStart) * random.nextDouble());			
		}
		updateDrawing();
	}

	private void updateDrawing() {
		this.drawPane.getChildren().clear();
		for(Sensor s : sensors){
			Circle circle = new Circle();
			circle.setLayoutY(rangeLine.getEndY());
			circle.setFill(Color.RED);
			circle.setStroke(Color.BLACK);
			circle.setLayoutX(startLine.getEndX() + (s.getPosition()*endLine.getEndX()));
			circle.setRadius(s.getRadius()*rangeLine.getEndX());
			drawPane.getChildren().add(circle);
		}
		drawPane.getChildren().add(startLine);
		drawPane.getChildren().add(endLine);
		drawPane.getChildren().add(rangeLine);
		drawPane.getChildren().add(lblRangeStart);
		drawPane.getChildren().add(lblRangeEnd);
	}

	public void setSensorRadius(Double i) {
		currentRadius = i;
	}
}
