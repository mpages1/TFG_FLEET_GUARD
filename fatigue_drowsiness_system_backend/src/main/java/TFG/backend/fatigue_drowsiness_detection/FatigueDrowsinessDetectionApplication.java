package TFG.backend.fatigue_drowsiness_detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "TFG.backend.fatigue_drowsiness_detection")
@EnableScheduling
public class FatigueDrowsinessDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FatigueDrowsinessDetectionApplication.class, args);
	}

}
