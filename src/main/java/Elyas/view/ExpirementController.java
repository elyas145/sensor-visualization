package Elyas.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ExpirementController implements Initializable {

	@FXML
	VBox vbxMain;
	private DrawController drawController;
	
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

	}
	
	public void initializeDrawing(){
		drawController.setDrawableArea();
	}

}
