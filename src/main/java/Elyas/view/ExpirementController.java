package Elyas.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Elyas.model.AxisType;
import Elyas.model.Sensor;
import Elyas.model.algorithms.Algorithm;
import Elyas.model.algorithms.BackToBack;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ExpirementController implements Initializable {

	@FXML
	VBox vbxMain;
	@FXML
	TextField txtSensorNumber;
	@FXML
	TextField txtSensorIncrement;
	@FXML
	TextField txtRadius;
	@FXML
	TextField txtRadiusIncrement;
	@FXML
	TextField txtRangeFrom;
	@FXML
	TextField txtRangeTo;
	@FXML
	ComboBox<AxisType> cmbXAxis;
	@FXML
	TextField txtAnimationSpeed;
	@FXML
	TextArea txtLog;
	@FXML
	RadioButton rdbAllSensors;
	@FXML
	RadioButton rdbOneSensor;
	@FXML
	TextField txtNumberOfRuns;
	@FXML
	TextField txtSensorIncrementEnd;
	@FXML
	TextField txtRadiusIncrementEnd;
	@FXML
	Button btnStart;
	@FXML
	CheckBox chkDisableAnimation;

	private DrawController drawController;
	private int currentRun = 0;
	private int numberOfRuns = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<AxisType> xAxis = FXCollections.observableArrayList(AxisType.values());
		cmbXAxis.setItems(xAxis);
		cmbXAxis.valueProperty().addListener(new ChangeListener<AxisType>() {
			@Override
			public void changed(ObservableValue<? extends AxisType> observable, AxisType oldValue, AxisType newValue) {
				switch (newValue) {
				case NUMBER_OF_SENSORS:
					txtRadiusIncrement.setDisable(true);
					txtSensorIncrement.setDisable(false);
					break;
				case SENSOR_RADIUS:
					txtRadiusIncrement.setDisable(false);
					txtSensorIncrement.setDisable(true);
					break;
				default:
					break;
				}

			}
		});
		cmbXAxis.setValue(AxisType.NUMBER_OF_SENSORS);

		try {
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxml/drawing.fxml"));
			Pane root = (Pane) ldr.load();
			drawController = (DrawController) ldr.getController();
			vbxMain.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		txtRangeTo.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					drawController.setRange(Float.valueOf(txtRangeFrom.getText()), Float.valueOf(txtRangeTo.getText()));
				} catch (Exception e) {
					txtRangeTo.setText("");
				}
			}
		});

		txtSensorNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					drawController.setNumberOfSensors(Integer.valueOf(newValue));
				} catch (Exception e) {
					txtSensorNumber.setText("");
				}
			}
		});

		txtRadius.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					drawController.setSensorRadius(Float.valueOf(txtRadius.getText()));
				} catch (Exception e) {
					txtRadius.setText("");
				}
			}
		});
		
		chkDisableAnimation.selectedProperty().addListener((obs, o, n)->this.drawController.setVisible(!n));

	}

	@FXML
	protected void btnStartAction(ActionEvent e) {
		float from = Float.valueOf(this.txtRangeFrom.getText());
		float to = Float.valueOf(this.txtRangeTo.getText());
		numberOfRuns = Integer.valueOf(this.txtNumberOfRuns.getText());
		currentRun = 1;
		drawController.setNumberOfSensors(Integer.valueOf(this.txtSensorNumber.getText()));
		drawController.setSensorRadius(Float.valueOf(this.txtRadius.getText()));
		setControlsDisabled(true);
		runAlgorithm(from, to);

	}

	private void setControlsDisabled(boolean b) {
		this.txtAnimationSpeed.setDisable(b);
		this.txtNumberOfRuns.setDisable(b);
		this.txtRadius.setDisable(b);
		this.txtRadiusIncrement.setDisable(b);
		this.txtSensorIncrement.setDisable(b);
		this.txtSensorIncrementEnd.setDisable(b);
		this.txtSensorNumber.setDisable(b);
		this.txtRadiusIncrementEnd.setDisable(b);
		this.cmbXAxis.setDisable(b);
		this.rdbAllSensors.setDisable(b);
		this.rdbOneSensor.setDisable(b);
		this.btnStart.setDisable(b);
	}

	private boolean initNextRun() {
		if (currentRun++ < numberOfRuns) {
			drawController.randomizeSensors();
			return true;
		}
		switch (this.cmbXAxis.getValue()) {
		case NUMBER_OF_SENSORS:
			int numberOfSensors = drawController.getSensors().size();
			int incrementSensor = Integer.valueOf(this.txtSensorIncrement.getText());
			int maxIncrement = Integer.valueOf(this.txtSensorIncrementEnd.getText());
			if (incrementSensor < 0) {
				if (numberOfSensors + incrementSensor < 0 || numberOfSensors + incrementSensor < maxIncrement) {
					return false;
				}
			} else if (incrementSensor > 0) {
				if (numberOfSensors + incrementSensor > maxIncrement) {
					return false;
				}
			}
			numberOfSensors += incrementSensor;
			drawController.setNumberOfSensors(numberOfSensors);
			break;
		case SENSOR_RADIUS:
			float currentRadius = drawController.getSensorRadius();
			float incrementRadius = Float.valueOf(this.txtRadiusIncrement.getText());
			float maxRadius = Float.valueOf(this.txtRadiusIncrementEnd.getText());
			if (incrementRadius < 0) {
				if (currentRadius + incrementRadius < 0 || currentRadius + incrementRadius < maxRadius) {
					return false;
				}
			} else if (incrementRadius > 0) {
				if (currentRadius + incrementRadius > maxRadius) {
					return false;
				}
			}
			currentRadius += incrementRadius;
			drawController.setSensorRadius(currentRadius);
			break;
		default:
			break;
		}
		currentRun = 1;
		drawController.randomizeSensors();
		return true;
	}

	private void runAlgorithm(final float from, final float to) {
		List<Sensor> toMove = new ArrayList<>();
		Algorithm algorithm = new BackToBack(drawController.getSensors(), from, to);
		algorithm.addMoveListener((sensor) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					toMove.add(sensor);
				}
			});
		});
		algorithm.addFinishListener(() -> {
			Platform.runLater(() -> {
				txtLog.appendText("algorithm finished with " + algorithm.getNumberOfMoves() + " moves.\n");
				EventHandler<ActionEvent> onFinish = (ae)->{
					if (initNextRun()) {
						runAlgorithm(from, to);
					} else {
						this.txtLog.appendText("Finished Expirement.");
						this.setControlsDisabled(false);
					}
				};
				if (!this.chkDisableAnimation.isSelected()) {
					txtLog.appendText("Waiting for animation to finish.\n");
					boolean oneAtATime = this.rdbOneSensor.isSelected();					
					drawController.moveSensors(toMove, Double.valueOf(txtAnimationSpeed.getText()), oneAtATime,onFinish);
				}else{
					onFinish.handle(null);
				}

			});
		});
		
		//start the next iteration.
		txtLog.appendText("\nStarting Algorithm number " + currentRun + " out of " + numberOfRuns + "\n");
		switch (this.cmbXAxis.getValue()) {
		case NUMBER_OF_SENSORS:
			txtLog.appendText("Current number of sensors: " + drawController.getSensors().size() + "\n");
			break;
		case SENSOR_RADIUS:
			txtLog.appendText("Current radius of sensors: " + drawController.getSensorRadius() + "\n");
			break;
		default:
			break;
		}
		algorithm.startAlgorithm();
	}

	public void initializeDrawing() {
		drawController.setDrawableArea();
		drawController.setRange(Float.valueOf(txtRangeFrom.getText()), Float.valueOf(txtRangeTo.getText()));
		drawController.setSensorRadius(Float.valueOf(txtRadius.getText()));
		drawController.setNumberOfSensors(Integer.valueOf(txtSensorNumber.getText()));
	}

}
