package Elyas.view;

/**
 * 
 * @author Elyas Syoufi
 *         <p>
 *         notified when the expirement is finished. should be used by the main
 *         controller in order to trigger an update to the graph.
 *         </p>
 */
public interface ExpirementFinishListener {
	void onFinish(Elyas.model.expirement.Experiment expirement);
}
