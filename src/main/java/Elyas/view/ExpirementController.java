package Elyas.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import Elyas.model.Sensor;
import Elyas.model.algorithms.Algorithm;
import Elyas.model.algorithms.BackToBack;
import Elyas.model.expirement.Expirement;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	TextField txtRangeFrom;
	@FXML
	TextField txtRangeTo;
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
	Button btnStart;
	@FXML
	CheckBox chkDisableAnimation;
	@FXML
	private TextField txtRadius;

	private DrawController drawController;
	private int currentRun = 0;
	private int numberOfRuns = 0;
	private ExpirementFinishListener expirementFinishListener;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
					drawController.setNumberOfSensors(Integer.valueOf(newValue), !chkDisableAnimation.isSelected());
					drawController.setSensorRadius(calculateRadius(), !chkDisableAnimation.isSelected());
				} catch (Exception e) {
					txtSensorNumber.setText("");
				}
			}
		});

		chkDisableAnimation.selectedProperty().addListener((obs, o, n) -> this.drawController.setVisible(!n));

	}

	@FXML
	protected void btnStartAction(ActionEvent e) {
		float from = Float.valueOf(this.txtRangeFrom.getText());
		float to = Float.valueOf(this.txtRangeTo.getText());
		numberOfRuns = Integer.valueOf(this.txtNumberOfRuns.getText());
		currentRun = 1;
		drawController.setNumberOfSensors(Integer.valueOf(this.txtSensorNumber.getText()),
				!chkDisableAnimation.isSelected());
		drawController.setSensorRadius(calculateRadius(), !chkDisableAnimation.isSelected());
		setControlsDisabled(true);

		Expirement expirement = new Expirement(this.txtRadius.getText(), "Number of Sensors", "Number of Moves",
				numberOfRuns);
		this.txtLog.appendText("\nPlease Wait...\n");
		this.txtLog.appendText("Progress: 0 / " + numberOfRuns + "\n");
		runAlgorithm(from, to, numberOfRuns, expirement);

	}

	private float calculateRadius() {
		float fn = Integer.valueOf(this.txtRadius.getText());
		float den = 2 * Integer.valueOf(this.txtSensorNumber.getText());
		float radius = fn / den;
		return radius;
	}

	private void setControlsDisabled(boolean b) {
		this.txtAnimationSpeed.setDisable(b);
		this.txtNumberOfRuns.setDisable(b);
		this.txtSensorIncrementEnd.setDisable(b);
		this.txtSensorNumber.setDisable(b);
		this.rdbAllSensors.setDisable(b);
		this.rdbOneSensor.setDisable(b);
		this.btnStart.setDisable(b);
	}

	private boolean initNextRun() {
		if (currentRun++ < numberOfRuns) {
			drawController.randomizeSensors(!chkDisableAnimation.isSelected());
			return true;
		}

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
		drawController.setNumberOfSensors(numberOfSensors, !chkDisableAnimation.isSelected());

		currentRun = 1;
		drawController.randomizeSensors(!chkDisableAnimation.isSelected());
		return true;
	}

	private void runAlgorithm(final float from, final float to, int numberOfRuns, Expirement expirement) {
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

				EventHandler<ActionEvent> onFinish = (ae) -> {
					if (initNextRun()) {
						runAlgorithm(from, to, numberOfRuns, expirement);
					} else {
						this.txtLog.appendText("Finished Expirement.");
						this.setControlsDisabled(false);
						if (expirementFinishListener != null) {
							expirementFinishListener.onFinish(expirement);
						}
					}
					expirement.addMove(drawController.getSensors().size(), toMove.size(), currentRun);
				};
				if (!this.chkDisableAnimation.isSelected()) {
					txtLog.appendText("algorithm finished with " + algorithm.getNumberOfMoves() + " moves.\n");
					txtLog.appendText("Waiting for animation to finish.\n");
					boolean oneAtATime = this.rdbOneSensor.isSelected();
					drawController.moveSensors(toMove, Double.valueOf(txtAnimationSpeed.getText()), oneAtATime,
							onFinish);
				} else {
					onFinish.handle(null);
				}

			});
		});

		if (!this.chkDisableAnimation.isSelected()) {
			// start the next iteration.
			txtLog.appendText("\nStarting Algorithm number " + currentRun + " out of " + numberOfRuns + "\n");

			txtLog.appendText("Current number of sensors: " + drawController.getSensors().size() + "\n");
		} else {
			txtLog.replaceText(txtLog.getText().lastIndexOf("Progress:"), txtLog.getLength(), "Progress: " + currentRun
					+ " / " + numberOfRuns + " for " + drawController.getSensors().size() + " sensors." + "\n");
		}
		algorithm.startAlgorithm();
	}

	public void initializeDrawing() {
		drawController.setDrawableArea();
		drawController.setRange(Float.valueOf(txtRangeFrom.getText()), Float.valueOf(txtRangeTo.getText()));
		drawController.setSensorRadius(calculateRadius(), true);
		drawController.setNumberOfSensors(Integer.valueOf(txtSensorNumber.getText()), true);

	}

	public void setFinishListener(ExpirementFinishListener listener) {
		this.expirementFinishListener = listener;
	}

}
