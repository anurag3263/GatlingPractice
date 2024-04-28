package videogameScripting;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CheckResponseBodyExtractData extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    private final ScenarioBuilder scn = scenario("Save Data")
            .exec(http("Get All video Games")
                    .get("/api/videogame")
                    .check(status().is(200),status().not(404))
                    .check(jmesPath("[6].id").saveAs("gameId")))
            .pause(3)

            .exec(http("Get Specific video Game : #{gameId}")
                    .get("/api/videogame/#{gameId}")
                    .check(status().is(200))
                    .check(jmesPath("name").is("Minecraft")));



    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
