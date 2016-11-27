package Elyas.view;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import Elyas.model.Sensor;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * 
 * @author Elyas Syoufi
 *         <p>
 *         This class is responsible for keeping track of the sensors. it
 *         randomizes and places the sensors as needed. it has the ability to
 *         animate sensors as well as pure controller without GUI updates
 *         </p>
 */
public class DrawController implements Initializable {

	@FXML
	private Line rangeLine; // The line where the sensors sit
	@FXML
	private Line startLine; // The line where the range starts
	@FXML
	private Line endLine; // The line where the range ends
	@FXML
	private Label lblRangeStart; // label for the start line
	@FXML
	private Label lblRangeEnd; // label for the end line
	@FXML
	private Pane drawPane; // where everything will be drawn (main parent)

	// component size on the screen
	private double height;
	private double width;
	// component aligning values.
	private double lineOffset = 20;
	private double labelOffset = 10;
	// sensor specific parameters
	private Map<Sensor, Circle> sensors;
	private double currentRadius;
	private Random random;
	// range values. currently not changeable by the user.
	private float rangeEnd;
	private float rangeStart;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensors = new HashMap<>();
		random = new Random();
		// set caches to false so old sensors are not re-used when randomizing
		// positions
		this.drawPane.setCache(false);
		this.startLine.setCache(false);
		this.endLine.setCache(false);
		this.rangeLine.setCache(false);
	}

	/**
	 * initializes the draw area. must be called before manipulating the
	 * sensors.
	 */
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

	/**
	 * sets the range that the sensors should be in
	 * 
	 * @param from
	 *            the start of the range
	 * @param to
	 *            the end of the range
	 */
	public void setRange(float from, float to) {
		rangeStart = from;
		rangeEnd = to;
		lblRangeStart.setText("" + from);
		lblRangeEnd.setText("" + to);
	}

	/**
	 * sets the number of sensors. sensors are automatically randomized when
	 * this function is called
	 * 
	 * @param n
	 *            the new number of sensors
	 * @param redraw
	 *            whether to update the GUI or not. should be true if the client
	 *            has enabled animation.
	 */
	public void setNumberOfSensors(int n, boolean redraw) {
		if (n == sensors.size()) {
			return;
		}
		while (n > sensors.size()) {
			Sensor sensor = new Sensor();
			sensor.setRadius((float) currentRadius);
			Circle node = getDefaultSensorNode(sensor);
			sensors.put(sensor, node);
			if (redraw) {
				drawPane.getChildren().add(node);
			}
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
			s.setPosition(getRandomPosition());

		}
		if (redraw) {
			updateDrawing();
		}
	}

	/**
	 * 
	 * @return a new random valid position for a sensor in the range specified.
	 */
	private float getRandomPosition() {
		return rangeStart + (rangeEnd - rangeStart) * random.nextFloat();
	}

	/**
	 * returns the GUI node representing this sensor. if there is no such node,
	 * a new node is created and is linked to the sensor
	 * 
	 * @param s
	 *            the sensor
	 * @return the node linked to this sensor
	 */
	private Circle getDefaultSensorNode(Sensor s) {
		Circle circle;
		if (sensors.get(s) != null) {
			circle = sensors.get(s);
		} else {
			circle = new Circle();
			circle.setCache(false);
			circle.setOnMouseEntered((e) -> circle.setFill(Color.BLUE));
			circle.setOnMouseExited((e) -> circle.setFill(Color.RED));

			Tooltip tooltip = new Tooltip();
			s.positionProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					Platform.runLater(() -> tooltip.setText(newValue.doubleValue() + ""));
				}
			});
			changeTooltipStartTiming(tooltip);
			Tooltip.install(circle, tooltip);

		}

		circle.setFill(Color.RED);
		circle.setStroke(Color.BLACK);
		circle.setRadius(this.currentRadius * (rangeLine.getEndX() - rangeLine.getStartX()));
		circle.setCenterY(rangeLine.getEndY());

		return circle;
	}

	/**
	 * updates the GUI to represent the current status of the sensors.
	 */
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

	/**
	 * sets the radius of the sensors
	 * 
	 * @param i
	 *            the radius to set
	 * @param redraw
	 *            whether or not to update the GUI
	 */
	public void setSensorRadius(float i, boolean redraw) {
		currentRadius = i;
		for (Sensor sensor : sensors.keySet()) {
			sensor.setRadius(i);
		}
		if (redraw) {
			updateDrawing();
		}

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

	/**
	 * moves the GUI node associated with this sensor to the sensor's location.
	 * 
	 * @see moveSensors()
	 * @param sensor
	 *            the sensor that the node is associated with
	 * @param speed
	 *            the animation speed to move the sensor
	 */
	public void moveSensor(Sensor sensor, Double speed) {

		Circle circle = sensors.get(sensor);
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		final KeyValue kv = new KeyValue(circle.centerXProperty(), getDrawXLocation(sensor));
		final KeyFrame kf = new KeyFrame(Duration.millis(speed), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}

	/**
	 * moves the GUI nodes associated with the given sensors.
	 * 
	 * @param toMove
	 *            the sensors with the new positions
	 * @param speed
	 *            the animation speed
	 * @param oneAtATime
	 *            true if the sensors should wait for the last sensor to finish
	 *            moving before the next one starts. false if they should all
	 *            move at the same time.
	 * @param onFinish
	 *            called when all the sensors have finished moving.
	 */
	public void moveSensors(List<Sensor> toMove, Double speed, boolean oneAtATime, EventHandler<ActionEvent> onFinish) {
		Timeline[] timeLines = new Timeline[toMove.size()];
		for (Sensor sensor : toMove) {
			Circle circle = sensors.get(sensor);
			final Timeline timeline = new Timeline();
			timeline.setCycleCount(1);
			final KeyValue kv = new KeyValue(circle.centerXProperty(), getDrawXLocation(sensor));
			final KeyFrame kf = new KeyFrame(Duration.millis(speed), kv);
			timeline.getKeyFrames().add(kf);
			timeLines[toMove.indexOf(sensor)] = timeline;

		}
		Animation seq;
		if (!oneAtATime) {
			seq = new ParallelTransition(timeLines);
		} else {
			seq = new SequentialTransition(timeLines);
		}
		seq.setOnFinished(onFinish);
		seq.play();
	}

	/**
	 * returns the draw location for this sensor. the draw location is where the
	 * center should be on the GUI
	 * 
	 * @param sensor
	 *            the sensor to get the location of
	 * @return the X coordinate of the location
	 */
	private double getDrawXLocation(Sensor sensor) {

		return rangeLine.getStartX() + ((rangeLine.getEndX() - rangeLine.getStartX()) * sensor.getPosition());
	}

	/**
	 * creates random positions for the current sensors.
	 * 
	 * @param redraw
	 *            whether or not to update the GUI
	 */
	public void randomizeSensors(boolean redraw) {
		for (Sensor sensor : sensors.keySet()) {
			sensor.setPosition(getRandomPosition());
		}
		if (redraw) {
			updateDrawing();
		}
	}

	public float getSensorRadius() {
		return (float) this.currentRadius;
	}

	/**
	 * sets the drawing pane visibility to 'n'.
	 * 
	 * @param n
	 *            true if the drawing should be visible. false otherwise
	 */
	public void setVisible(Boolean n) {
		drawPane.setVisible(n);
		updateDrawing();
	}

}
