package videogameScripting;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReusableMethods extends Simulation {

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    public static ChainBuilder getAllVideoGames =
             exec(http("Get All video Games")
                    .get("/api/videogame")
                     .check(status().is(200)));

             public static ChainBuilder getSpecificVideoGame =
                     exec(http("Get Specific Video Game")
                             .get("/api/videogame/3")
                             .check(status().is(200)));

    private final ScenarioBuilder scn = scenario("Multiple Request with Reusable Method")
            .exec(getAllVideoGames)
            .pause(3)
            .exec(getSpecificVideoGame)
            .pause(3)
            .exec(getAllVideoGames)
            .pause(3)
            .exec(getSpecificVideoGame);
    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
