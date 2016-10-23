package Elyas.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Elyas.model.AxisType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
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

	private DrawController drawController;

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
					drawController.setRange(Double.valueOf(txtRangeFrom.getText()),
							Double.valueOf(txtRangeTo.getText()));
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
					txtRangeTo.setText("");
				}
			}
		});

		txtRadius.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					drawController.setSensorRadius(Double.valueOf(txtRadius.getText()));
				} catch (Exception e) {
					txtRangeTo.setText("");
				}
			}
		});

	}

	@FXML
	protected void btnStartAction(ActionEvent e) {
		
	}

	public void initializeDrawing() {
		drawController.setDrawableArea();
		drawController.setRange(Double.valueOf(txtRangeFrom.getText()), Double.valueOf(txtRangeTo.getText()));
		drawController.setSensorRadius(Double.valueOf(txtRadius.getText()));
		drawController.setNumberOfSensors(Integer.valueOf(txtSensorNumber.getText()));
	}

}
