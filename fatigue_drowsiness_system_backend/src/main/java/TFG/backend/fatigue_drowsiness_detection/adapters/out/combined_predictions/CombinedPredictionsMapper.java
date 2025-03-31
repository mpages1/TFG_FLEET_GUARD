package TFG.backend.fatigue_drowsiness_detection.adapters.out.combined_predictions;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;

final class CombinedPredictionsMapper {

    private CombinedPredictionsMapper() {
    }

    static CombinedPredictionsEntity toEntity(CombinedPredictions combinedpredictions) {
        return CombinedPredictionsEntity.builder()
                .id(combinedpredictions.getId())
                .driverId(combinedpredictions.getDriverId())
                .latestTimestampCamera(combinedpredictions.getLatestTimestampCamera())
                .eyeAspectRatio(combinedpredictions.getEyeAspectRatio())
                .mouthAspectRatio(combinedpredictions.getMouthAspectRatio())
                .latestTimestampTachograph(combinedpredictions.getLatestTimestampTachograph())
                .combinedPrediction(combinedpredictions.getCombinedPrediction())
                .fatigueScore(combinedpredictions.getFatigueScore())
                .drowsinessScore(combinedpredictions.getDrowsinessScore())
                .fatigueDetected(combinedpredictions.getFatigueDetected())
                .drowsinessDetected(combinedpredictions.getDrowsinessDetected())
                .adaFatigueDetected(combinedpredictions.getAdaFatigueDetected())
                .build();
    }

    static CombinedPredictions toModel(CombinedPredictionsEntity combinedpredictionsEntity) {
        return CombinedPredictions.builder()
                .id(combinedpredictionsEntity.getId())
                .driverId(combinedpredictionsEntity.getDriverId())
                .latestTimestampCamera(combinedpredictionsEntity.getLatestTimestampCamera())
                .eyeAspectRatio(combinedpredictionsEntity.getEyeAspectRatio())
                .mouthAspectRatio(combinedpredictionsEntity.getMouthAspectRatio())
                .latestTimestampTachograph(combinedpredictionsEntity.getLatestTimestampTachograph())
                .combinedPrediction(combinedpredictionsEntity.getCombinedPrediction())
                .fatigueScore(combinedpredictionsEntity.getFatigueScore())
                .drowsinessScore(combinedpredictionsEntity.getDrowsinessScore())
                .fatigueDetected(combinedpredictionsEntity.getFatigueDetected())
                .drowsinessDetected(combinedpredictionsEntity.getDrowsinessDetected())
                .adaFatigueDetected(combinedpredictionsEntity.getAdaFatigueDetected()).build();
    }
}
