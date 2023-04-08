package dev.cah1r.JarXmlWatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JarXmlWatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(JarXmlWatcherApplication.class, args);
	}

}
