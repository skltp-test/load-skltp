package SKLTP

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RequestsLargePayload extends Simulation {
        // Input paramters
        val targetHost = sys.env("TARGETHOST")
        val rps = sys.env("RPS").toInt
        var durationMinutes = sys.env("DURATION_MINUTES").toInt

        // Traffic will be routed toward LOAD-MOCKS based choosen personId
	val feeder2 = csv("../user-files/data/Direktadresserad.csv").circular
	
	val httpConf = http
		.baseUrl(s"https://$targetHost/vp")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("SKLTP load tests")
           val ProcessRequest = exec(http("ProcessRequest")
                .post("/clinicalprocess/activity/request/ProcessRequestResponder/1/rivtabp21")
                .body(ElFileBody("user-files/data/ProcessRequest1.xml")).asXml
                        .check(status.is(200), responseTimeInMillis.lt(35000)))

        // Direktadresserad trafik
        val requestProcessRequest = scenario("ProcessRequest").feed(feeder2).exec(ProcessRequest)


  setUp(
    requestProcessRequest.inject(
	constantUsersPerSec(rps) during(durationMinutes minutes))) 
    .protocols(httpConf)
    .assertions(
      global.successfulRequests.percent.gt(99),
      details("ProcessRequest").successfulRequests.percent.is(100))
  
}
