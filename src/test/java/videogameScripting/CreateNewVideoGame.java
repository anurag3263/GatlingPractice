package videogameScripting;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CreateNewVideoGame extends Simulation {

    private final HttpProtocolBuilder httpProtocol= http
            .baseUrl("https://videogamedb.uk")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static ChainBuilder authentication =
            exec(http("Authentication")
                    .post(":443/api/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jmesPath("token").saveAs("AuthToken")))
                    .exec(
                            session ->{
                                System.out.println(session.getString("AuthToken"));
                                return session;
                            }
                            );

    public static ChainBuilder createNewVideoGame =
    exec(http("Create New Video Game ")
            .post(":443/api/videogame")
            .header("Authorization","Bearer #{AuthToken}")
            .body(StringBody("{\n" +
                    "  \"category\": \"Platform\",\n" +
                    "  \"name\": \"Mario\",\n" +
                    "  \"rating\": \"Mature\",\n" +
                    "  \"releaseDate\": \"2012-05-04\",\n" +
                    "  \"reviewScore\": 85\n" +
                    "}"))
            .check(status().is(200)));

    private final ScenarioBuilder scn = scenario("Create New Video Game")
            .exec(authentication)
            .pause(3)
            .exec(createNewVideoGame);
    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }

}
