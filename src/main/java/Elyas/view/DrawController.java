package Elyas.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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

	public void setRange(double from, double to){
		lblRangeStart.setText(""+from);
		lblRangeEnd.setText(""+to);
	}
}
