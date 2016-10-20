package Elyas.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class MainController implements Initializable {

	@FXML
	private Tab expirementTab;
	@FXML
	private Tab graphsTab;
	
	private GraphsController graphsController;
	private ExpirementController expirementController;

	public boolean shutdown() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			FXMLLoader experimentldr = new FXMLLoader(getClass().getResource("/fxml/expirement.fxml"));
			Parent pnlExperiment = (Parent) experimentldr.load();
			expirementController = (ExpirementController) experimentldr.getController();

			FXMLLoader graphldr = new FXMLLoader(getClass().getResource("/fxml/graphs.fxml"));
			Parent pnlGraphs = (Parent) graphldr.load();
			graphsController = (GraphsController) graphldr.getController();

			expirementTab.setContent(pnlExperiment);
			graphsTab.setContent(pnlGraphs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initializeDrawArea() {
		expirementController.initializeDrawing();
	}

}
