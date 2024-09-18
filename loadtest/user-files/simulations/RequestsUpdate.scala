package SKLTP

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RequestsUpdate extends Simulation {
        // Input paramters
        val targetHost = sys.env("TARGETHOST")
        val rps = sys.env("RPS").toInt
        val update1000Pause = sys.env("UPDATE1000_PAUSE").toInt
        val update20Users = sys.env("UPDATE20_USERS").toInt

        // Traffic will be routed toward LOAD-MOCKS based choosen personId
	val feeder1 = csv("../user-files/data/ViaAGP.csv").circular
	val feederUpdate = csv("user-files/data/Update.csv").circular
	
	val httpConf = http
		.baseUrl(s"https://$targetHost/vp")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("SKLTP load tests")

        val GetDiagnosis_2 = exec(http("GetDiagnosis_2")
                .post("/clinicalprocess/healthcond/description/GetDiagnosis/2/rivtabp21")
                .body(ElFileBody("user-files/data/GetDiagnosis2.xml")).asXml
                .check(status.is(200), responseTimeInMillis.lt(35000)))

        val Update20 = exec(http("Update20")
                .post("/Update/1/rivtabp21")
                .body(ElFileBody("user-files/data/Update20_GCC.xml")).asXml
                .check(status.is(200), responseTimeInMillis.lt(35000)))

        val Update1000 = exec(http("Update1000")
                .post("/Update/1/rivtabp21")
                .body(ElFileBody("user-files/data/Update1000_GD.xml")).asXml
                .check(status.is(200), responseTimeInMillis.lt(35000)))

        val requestGetDiagnosis_2 = scenario("GetDiagnosis_2").feed(feeder1).exec(GetDiagnosis_2)
        val requestUpdate20 = scenario("Update20").feed(feederUpdate).exec(Update20).pause(5,15)
        val requestUpdate1000 = scenario("Update1000").feed(feederUpdate).exec(Update1000).pause(update1000Pause)

  // Simulera grundladdning med 1000 poster/anrop, varierbar paus mellan
  // + Mindre Update-anrop med 5-15 sek paus mellan
  // + Trafik via AgP
  setUp(
    requestUpdate1000.inject(constantConcurrentUsers(1) during (20 minutes)), 
    requestUpdate20.inject(constantConcurrentUsers(update20Users) during (20 minutes)), 
    requestGetDiagnosis_2.inject(constantUsersPerSec(rps) during(20 minutes)))
    .protocols(httpConf)  
    .assertions(
      global.successfulRequests.percent.gt(99),
      details("Update1000").successfulRequests.percent.is(100),
      details("GetDiagnosis_2").successfulRequests.percent.gt(95))
  
}
