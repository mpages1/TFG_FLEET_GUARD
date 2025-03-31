/**
 * @author mpages1
 */
package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

public interface TachographDetectionUseCase {

	void initializeTachograph();

	void startTachograph(int driverId);

}
