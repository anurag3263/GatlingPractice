package videogameScripting;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;
public class GetSpecificVideoGames extends Simulation{

    /*
     * Http configuration
     * Scenario definition
     * load simulation
     */

    // HTTP configuration

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    //Scenario Definition

    private final ScenarioBuilder scn = scenario("Second Gatling Script")
            .exec(http("Get Specific video Games")
                    .get("/api/videogame/2"));

    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }

}
