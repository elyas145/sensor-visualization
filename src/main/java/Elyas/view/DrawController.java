package Elyas.view;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import Elyas.model.Sensor;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

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
	private Map<Sensor, Circle> sensors;
	private Double currentRadius;
	private Random random;
	private double rangeEnd;
	private double rangeStart;
	private double sensorOffset = 10;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensors = new HashMap<>();
		random = new Random();
		this.drawPane.setCache(false);
		this.startLine.setCache(false);
		this.endLine.setCache(false);
		this.rangeLine.setCache(false);
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
			Circle node = getDefaultSensorNode(sensor);
			sensors.put(sensor, node);
			drawPane.getChildren().add(node);

		}
		if (n < sensors.size()) {
			for (Iterator<Sensor> iterator = sensors.keySet().iterator(); iterator.hasNext();) {
				if (n < sensors.size()) {
					iterator.next();
					iterator.remove();
				} else {
					break;
				}
			}
		}
		for (Sensor s : sensors.keySet()) {
			s.setPosition(rangeStart + (rangeEnd - rangeStart) * random.nextDouble());

		}
		updateDrawing();
	}

	private Circle getDefaultSensorNode(Sensor s) {
		Circle circle;
		if(sensors.get(s) != null){
			circle = sensors.get(s);
		}else{
			circle = new Circle();
			circle.setCache(false);
			circle.setOnMouseEntered((e) -> circle.setFill(Color.BLUE));
			circle.setOnMouseExited((e) -> circle.setFill(Color.RED));

			Tooltip tooltip = new Tooltip();
			s.positionProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					tooltip.setText(newValue.doubleValue() + "");
				}
			});
			changeTooltipStartTiming(tooltip);
			Tooltip.install(circle, tooltip);
			
		}
		
		circle.setFill(Color.RED);
		circle.setStroke(Color.BLACK);
		circle.setRadius(this.currentRadius * (rangeLine.getEndX()-rangeLine.getStartX()));
		circle.setCenterY(rangeLine.getEndY());
		
		return circle;
	}

	public void updateDrawing() {
		this.drawPane.getChildren().clear();
		drawPane.getChildren().add(startLine);
		drawPane.getChildren().add(endLine);
		drawPane.getChildren().add(rangeLine);
		drawPane.getChildren().add(lblRangeStart);
		drawPane.getChildren().add(lblRangeEnd);
		for (Sensor s : sensors.keySet()) {
			sensors.get(s).setCenterX(getDrawXLocation(s));
			getDefaultSensorNode(s);
			drawPane.getChildren().add(sensors.get(s));
			sensors.get(s).toBack();
		}

		drawPane.toBack();
	}

	public void setSensorRadius(Double i) {
		currentRadius = i;
		for (Sensor sensor : sensors.keySet()) {
			sensor.setRadius(i);
		}
		updateDrawing();
	}

	/**
	 * changes the tooltip start time.
	 * 
	 * @param tooltip
	 *            the tooltip to modify.
	 */
	private void changeTooltipStartTiming(Tooltip tooltip) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(5)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<Sensor> getSensors() {
		return sensors.keySet();
	}

	public void moveSensor(Sensor sensor, Double speed) {
		Circle circle = sensors.get(sensor);
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		final KeyValue kv = new KeyValue(circle.centerXProperty(), getDrawXLocation(sensor));
		final KeyFrame kf = new KeyFrame(Duration.millis(speed), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}

	private double getDrawXLocation(Sensor sensor) {

		return rangeLine.getStartX() + ((rangeLine.getEndX() - rangeLine.getStartX()) * sensor.getPosition());
	}

	public void setupDrawing() {
		
	}
}
