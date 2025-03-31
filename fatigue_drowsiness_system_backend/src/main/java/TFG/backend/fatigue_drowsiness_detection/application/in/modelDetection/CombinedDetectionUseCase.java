/**
 * @author mpages1
 */
package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

public interface CombinedDetectionUseCase {

	void initializeCombinedDetection();

	void startCombinedDetection(int driverId);

	boolean isAdaBoostTrained();

	void trainAdaBoostIfNecessary();

}
