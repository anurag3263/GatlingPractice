package videogameScripting;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class CheckResponseBodyJsonPath extends Simulation {

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json");

    //Scenario Definition

    private final ScenarioBuilder scn = scenario("Multiple Request Testing")
            .exec(http("Get All video Games : first Request")
                    .get("/api/videogame")
                    .check(status().is(200))//Single status code
                    .check(jsonPath("$[?(@.id==5)].name")
                            .is("The Legend of Zelda: Ocarina of Time")))
            .pause(3) //3 Seconds

            .exec(http("Get Specific Video Games : second Request")
                    .get("/api/videogame/3")
                    .check(status().in(200,201,202))//Multiple Status code
                    .check(jsonPath("$[?(@.id==3)].reviewScore").is("88")))
            .pause(1,10)//Time Range

            .exec(http("Get All the Video Game : third request")
                    .get("/api/videogame")
                    .check(status().not(400),status().not(404))
                    .check(jsonPath("$[?(@.id==6)].category").is("Shooter")))
            .pause(Duration.ofMillis(3000))//Millisecond

            .exec(http("Get specific video game : fourth request")
                    .get("/api/videogame/7")
                    .check(status().is(200),status().not(404))
                    .check(jsonPath("$[?(@.id==7)].rating").is("Universal")));


    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
