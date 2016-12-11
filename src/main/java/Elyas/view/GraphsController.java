package Elyas.view;


import java.net.URL;
import java.util.ResourceBundle;

import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import Elyas.model.expirement.Experiment;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
	LineChart<Integer, Integer> moveGraph;

	@FXML
	LineChart<Integer, Float> distanceGraph;
	@FXML
	ScrollPane pane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Panning works via either secondary (right) mouse or primary with ctrl
		// held down
		ChartPanManager panner = new ChartPanManager(moveGraph);
		panner.setMouseFilter(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() == MouseButton.SECONDARY
						|| (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isShortcutDown())) {
					// let it through
				} else {
					mouseEvent.consume();
				}
			}
		});
		panner.start();
		// Zooming works only via primary mouse button without ctrl held down
		JFXChartUtil.setupZooming(moveGraph, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isShortcutDown())
					mouseEvent.consume();				
			}
		});
		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(moveGraph);
		
		// Panning works via either secondary (right) mouse or primary with ctrl
				// held down
				ChartPanManager panner2 = new ChartPanManager(distanceGraph);
				panner2.setMouseFilter(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (mouseEvent.getButton() == MouseButton.SECONDARY
								|| (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isShortcutDown())) {
							// let it through
						} else {
							mouseEvent.consume();
						}
					}
				});
				panner2.start();
				// Zooming works only via primary mouse button without ctrl held down
				JFXChartUtil.setupZooming(distanceGraph, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isShortcutDown())
							mouseEvent.consume();				
					}
				});
				JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(distanceGraph);
	}

	/**
	 * plots the given experiment on a line graph.
	 * 
	 * @param experiment
	 *            the experiment to plot
	 */
	public void setGraph(Experiment experiment) {
		moveGraph.getXAxis().setLabel(experiment.getX());
		moveGraph.getYAxis().setLabel(experiment.getY());
		moveGraph.setTitle("Moves Used to Cover Range using f(n) = " + experiment.getFN());

		Series<Integer, Integer> series = new Series<>();
		for (Integer x : experiment.getMoves().keySet()) {

			series.getData().add(new Data<>(x, experiment.getMoves().get(x)));
		}
		moveGraph.getData().clear();
		moveGraph.getData().add(series);

		distanceGraph.getXAxis().setLabel(experiment.getX());
		distanceGraph.getYAxis().setLabel("Distance Traveled");
		distanceGraph.setTitle("Total Distance Used to Cover Range using f(n) = " + experiment.getFN());

		Series<Integer, Float> series2 = new Series<>();
		for (Integer x : experiment.getDistances().keySet()) {

			series2.getData().add(new Data<>(x, experiment.getDistances().get(x)));
		}
		distanceGraph.getData().clear();
		distanceGraph.getData().add(series2);

	}

}
