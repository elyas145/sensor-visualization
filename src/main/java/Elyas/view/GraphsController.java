package Elyas.view;

import java.net.URL;
import java.util.ResourceBundle;

import Elyas.model.expirement.Expirement;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class GraphsController implements Initializable {

	@FXML
	LineChart<Integer, Integer> graph;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setGraph(Expirement expirement) {
		graph.getXAxis().setLabel(expirement.getX());
		graph.getYAxis().setLabel(expirement.getY());
		graph.setTitle("Coverage using f(n) = " + expirement.getFN());
		
		Series<Integer, Integer> series = new Series<>();
		for(Integer x : expirement.getMoves().keySet()){
			
			series.getData().add(new Data<>(x, expirement.getMoves().get(x)));			
		}
		graph.getData().clear();
		graph.getData().add(series);
	}

}
 