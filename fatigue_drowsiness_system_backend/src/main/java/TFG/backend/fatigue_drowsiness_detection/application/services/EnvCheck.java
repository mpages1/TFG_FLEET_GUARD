package TFG.backend.fatigue_drowsiness_detection.application.services;

import org.springframework.stereotype.Component;

import TFG.backend.fatigue_drowsiness_detection.app.AppConfig;
import jakarta.annotation.PostConstruct;

@Component
public class EnvCheck {

	private final AppConfig appConfig;

	public EnvCheck(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	@PostConstruct
	public void checkVariables() {
		System.out.println("CAMERA_SERVICE_INIT_URL: " + appConfig.getINIT_CAMERA_SERVICE_URL());
		System.out.println("TACHOGRAPH_SERVICE_INIT_URL: " + appConfig.getINIT_TACHOGRAPH_SERVICE_URL());
		System.out.println("COMBINED_SERVICE_INIT_URL: " + appConfig.getINIT_COMBINED_SERVICE_URL());
		System.out.println("");
		System.out.println("CAMERA_SERVICE_URL: " + appConfig.getSTART_CAMERA_SERVICE_URL());
		System.out.println("TACHOGRAPH_SERVICE_URL: " + appConfig.getSTART_TACHOGRAPH_SERVICE_URL());
		System.out.println("COMBINED_SERVICE_URL: " + appConfig.getSTART_COMBINED_SERVICE_URL());
	}
}
