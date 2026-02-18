package tr.alid.project2;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Project2Application {
	private static final Logger logger = LoggerFactory.getLogger(Project2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Project2Application.class, args);
	}

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Environment env;

	private List<String> getOtherNodes() {
		String node1 = env.getProperty("node1-url", "http://localhost:8081/api/save");
		String node2 = env.getProperty("node2-url", "http://localhost:8082/api/save");
		String node3 = env.getProperty("node3-url", "http://localhost:8083/api/save");

		return Arrays.asList(node1, node2, node3);
	}

	@PostConstruct
	public void init() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(() -> {
			String randomString = UUID.randomUUID().toString().substring(0, 36);
			sendToOtherNodes(randomString);
		}, 0, 10, TimeUnit.SECONDS);
	}

	private void sendToOtherNodes(String randomString) {
		List<String> otherNodes = getOtherNodes();
		for (String nodeUrl : otherNodes) {
			try {
				restTemplate.postForObject(nodeUrl, randomString, String.class);
				logger.info("Gönderildi: {} -> {}", randomString, nodeUrl);
			} catch (Exception e) {
				logger.error("Gönderim hatası: {} -> {}", randomString, nodeUrl, e);
			}
		}
	}
}
