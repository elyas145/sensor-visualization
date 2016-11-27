package Elyas.view;

import java.net.URL;
import java.util.ResourceBundle;

import Elyas.model.expirement.Experiment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * 
 * @author Elyas Syoufi
 *         <p>
 *         responsible for the graph of the experiment
 *         </p>
 * 
 */
public class GraphsController implements Initializable {

	@FXML
	LineChart<Integer, Integer> graph;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * plots the given experiment on a line graph.
	 * 
	 * @param experiment
	 *            the experiment to plot
	 */
	public void setGraph(Experiment experiment) {
		graph.getXAxis().setLabel(experiment.getX());
		graph.getYAxis().setLabel(experiment.getY());
		graph.setTitle("Coverage using f(n) = " + experiment.getFN());

		Series<Integer, Integer> series = new Series<>();
		for (Integer x : experiment.getMoves().keySet()) {

			series.getData().add(new Data<>(x, experiment.getMoves().get(x)));
		}
		graph.getData().clear();
		graph.getData().add(series);
	}

}
