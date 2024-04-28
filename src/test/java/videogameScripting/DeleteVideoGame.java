package videogameScripting;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DeleteVideoGame extends Simulation {

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

    public static ChainBuilder deleteVideoGame =
    exec(http("Update Video Game details")
            .delete(":443/api/videogame/5")
            .header("Authorization","Bearer #{AuthToken}")
            .check(status().is(200)));

    private final ScenarioBuilder scn = scenario("delete Video Game details")
            .exec(authentication)
            .pause(3)
            .exec(deleteVideoGame);
    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }

}
