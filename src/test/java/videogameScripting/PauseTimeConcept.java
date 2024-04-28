package videogameScripting;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class PauseTimeConcept extends Simulation {

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    //Scenario Definition

    private final ScenarioBuilder scn = scenario("Multiple Request Testing")
            .exec(http("Get All video Games : first Request")
                    .get("/api/videogame"))
            .pause(3) //3 Seconds

            .exec(http("Get Specific Video Games : second Request")
                    .get("/api/videogame/3"))
            .pause(1,10)//Time Range

            .exec(http("Get All the Video Game : third request")
            .get("/api/videogame"))
            .pause(Duration.ofMillis(3000))//Millisecond

            .exec(http("Get specific video game : fourth request")
                    .get("/api/videogame/7"));


    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
