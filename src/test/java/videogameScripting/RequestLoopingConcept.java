package videogameScripting;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RequestLoopingConcept extends Simulation {

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    public static ChainBuilder getAllVideoGames =
            repeat(5).on(
             exec(http("Get All video Games")
                    .get("/api/videogame")
                     .check(status().is(200)))
            );

             public static ChainBuilder getSpecificVideoGame =
                     repeat(11,"counter").on(
                     exec(http("Get Specific Video Game - #{counter}")
                             .get("/api/videogame/#{counter}")
                             .check(status().is(200)))
                     );

    private final ScenarioBuilder scn = scenario("Multiple Request with Reusable Method")
            .exec(getAllVideoGames) // 5 request
            .pause(3)
            .exec(getSpecificVideoGame) // 11 request
            .pause(3)
            .exec(getAllVideoGames) // 5 request
            ;// total 21 request will be there
    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
