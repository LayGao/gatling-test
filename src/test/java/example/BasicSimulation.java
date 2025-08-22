package example;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.UUID;

public class BasicSimulation extends Simulation {

    // Load VU count from system properties
    // Reference: https://docs.gatling.io/guides/passing-parameters/
    private static final int vu = Integer.getInteger("vu", 3000);

    // Define HTTP configuration
    // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
    private static final HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .userAgentHeader(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
            .shareConnections();

    // Define scenario
    // Reference: https://docs.gatling.io/reference/script/core/scenario/
//    private static final ScenarioBuilder scenario = scenario("Scenario")
//            .repeat(30).on(
//                exec(session -> session.set("uuid", UUID.randomUUID().toString()))
//                .exec(http("Session")
//                        .get("/params")
//                        .queryParam("requestId", "#{uuid}").requestTimeout(1200))
//            );
    private static final ScenarioBuilder scenario = scenario("Scenario")

            .exec(session -> session.set("uuid", UUID.randomUUID().toString()))
                            .exec(http("Session")
                                    .get("/params")
                                    .queryParam("requestId", "#{uuid}").requestTimeout(600)
            );

    // Define assertions
    // Reference: https://docs.gatling.io/reference/script/core/assertions/
    private static final Assertion assertion = global().failedRequests().count().lt(1L);

    // Define injection profile and execute the test
    // Reference: https://docs.gatling.io/reference/script/core/injection/
    {
//    setUp(scenario.injectOpen(atOnceUsers(1))).assertions(assertion).protocols(httpProtocol);
        {
//            setUp(scenario.injectOpen(constantUsersPerSec(3000).during(10))).protocols(httpProtocol);
//            setUp(scenario.injectOpen(rampUsers(10000).during(30),  constantUsersPerSec(500).during(60))).protocols(httpProtocol);


        }

        setUp(
                scenario.injectClosed(
                        incrementConcurrentUsers(800)
                                .times(10)
                                .eachLevelLasting(10)
                                .separatedByRampsLasting(10)
                                .startingFrom(800)
                )
        ).protocols(httpProtocol);




    }
}
