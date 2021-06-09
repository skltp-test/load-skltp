package SKLTP

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RequestsProducerDelay extends Simulation {
        // Input paramters
        val targetHost = sys.env("TARGETHOST")
        val rps = sys.env("RPS").toInt

        // Traffic will be routed toward LOAD-MOCKS based choosen personId
	val feeder2 = csv("../user-files/data/ProducerDelay.csv").circular
	val httpConf = http
		.baseUrl(s"https://$targetHost/vp")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("SKLTP load tests")

           val Direktadresserad = exec(http("Direktadresserad")
                .post("/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
                .body(ElFileBody("user-files/data/GetCareDocumentation2.xml")).asXml
                        .check(status.is(200), responseTimeInMillis.lt(35000)))

        // Direktadresserad trafik
        val requestDirektadresserad = scenario("Direktadresserad").feed(feeder2).exec(Direktadresserad)


  setUp(
    requestDirektadresserad.inject(
	constantUsersPerSec(rps) during(300 seconds))) 
    .protocols(httpConf)
    .assertions(
      global.successfulRequests.percent.gt(99),
      details("Direktadresserad").successfulRequests.percent.is(100))
  
}
