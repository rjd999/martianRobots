package mars;

import mars.lander.Lander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MartianRobotsApp implements CommandLineRunner {
    private final Lander lander;

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(MartianRobotsApp.class);

        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }

    @Autowired
    public MartianRobotsApp(final Lander lander) {
        this.lander = lander;
    }

    @Override
    public void run(final String[] args) {
        lander.coordinateExploration();
    }
}
