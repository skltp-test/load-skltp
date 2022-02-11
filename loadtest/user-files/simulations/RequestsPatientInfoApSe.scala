package SKLTP

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RequestsPatientInfoApSe extends Simulation {

        val targetHost = sys.env("TARGETHOST")
        val requestsPerSecond = sys.env("RPS").toInt
        val durationMinutes = sys.env("DURATION_MINUTES").toInt

        val baseUrl = s"https://$targetHost/vp"
        val XmlPath = "user-files/data/GetPatientInfoApSe.xml"
        val CsvPath = "../user-files/data/GetPatientInfoApSe.csv"
        val HttpRequestName = "ApSeMock"
        val ScenarioName = "ApSeMock"

        // Traffic will be routed toward LOAD-MOCKS based choosen personId
	val feeder = csv(CsvPath).circular
	
	val httpConf = http
		.baseUrl(baseUrl)
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("SKLTP load tests")

        val httpRequest = exec(http(HttpRequestName)
        .post("/")
        .body(ElFileBody(XmlPath)).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

        val loadTestScenario = scenario(ScenarioName).feed(feeder).exec(httpRequest)


  setUp(
    loadTestScenario.inject(constantUsersPerSec(requestsPerSecond) during(durationMinutes minutes)))
    .protocols(httpConf)
    .assertions(
      global.successfulRequests.percent.gt(99),
      details(HttpRequestName).successfulRequests.percent.is(100))
  
}
