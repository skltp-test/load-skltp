package SKLTP

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RequestsTrafikmixVariable extends Simulation {
    // Input paramters
    val targetHost = sys.env("TARGETHOST")
    val durationMinutes = sys.env("DURATION_MINUTES").toInt
    val directRps = sys.env("DIRECT_RPS").toInt
    val aggRpsMin = sys.env("AGG10_RPS_MIN").toInt
    val aggRpsMax = sys.env("AGG10_RPS_MAX").toInt
    val updateRpsMin = sys.env("UPDATE_RPS_MIN").toInt
    val updateRpsMax = sys.env("UPDATE_RPS_MAX").toInt
    val errorReqs = sys.env("ERR3_REQ").toInt

    // Traffic will be routed toward LOAD-MOCKS based choosen personId
	val feeder1 = csv("user-files/data/ViaAGP-delay.csv").circular
	val feeder2 = csv("user-files/data/Direktadresserad-delay.csv").circular
	val feederUpdate = csv("user-files/data/Update.csv").circular
	val feederVP004 = csv("user-files/data/VP004.csv").circular
	val feederVP007 = csv("user-files/data/VP007.csv").circular
	val feederVP009 = csv("user-files/data/VP009.csv").circular
	
	val httpConf = http
		.baseUrl(s"https://$targetHost/vp")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("SKLTP load tests")

    val GetDiagnosis_2 = exec(http("GetDiagnosis_2")
            .post("/clinicalprocess/healthcond/description/GetDiagnosis/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetDiagnosis2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetMedicationHistory_2 = exec(http("GetMedicationHistory_2")
        .post("/clinicalprocess/activityprescription/actoutcome/GetMedicationHistory/2/rivtabp21")
        .body(ElFileBody("user-files/data/GetMedicationHistory2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))  

    val GetImagingOutcome_1 = exec(http("GetImagingOutcome_1")
            .post("/clinicalprocess/healthcond/actoutcome/GetImagingOutcome/1/rivtabp21")
            .body(ElFileBody("user-files/data/GetImagingOutcome1.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetLaboratoryOrderOutcome_3 = exec(http("GetLaboratoryOrderOutcome_3")
            .post("/clinicalprocess/healthcond/actoutcome/GetLaboratoryOrderOutcome/3/rivtabp21")
            .body(ElFileBody("user-files/data/GetLaboratoryOrderOutcome3.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetAlertInformation_2 = exec(http("GetAlertInformation_2")
            .post("/clinicalprocess/healthcond/description/GetAlertInformation/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetAlertInformation2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetCareContacts_2 = exec(http("GetCareContacts_2")
            .post("/clinicalprocess/logistics/logistics/GetCareContacts/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCareContacts2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetCareDocumentation_2 = exec(http("GetCareDocumentation_2")
            .post("/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCareDocumentation2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetVaccinationHistory_2 = exec(http("GetVaccinationHistory_2")
            .post("/clinicalprocess/activityprescription/actoutcome/GetVaccinationHistory/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetVaccinationHistory2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetFunctionalStatus_2 = exec(http("GetFunctionalStatus_2")
            .post("/clinicalprocess/healthcond/description/GetFunctionalStatus/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetFunctionalStatus2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val GetCarePlans_2 = exec(http("GetCarePlans_2")
            .post("/clinicalprocess/logistics/logistics/GetCarePlans/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCarePlans2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val Direktadresserad = exec(http("Direktadresserad")
            .post("/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCareDocumentation2.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val Update20 = exec(http("Update20")
            .post("/Update/1/rivtabp21")
            .body(ElFileBody("user-files/data/Update20_GD.xml")).asXml
        .check(status.is(200), responseTimeInMillis.lt(35000)))

    val VP004 = exec(http("VP004")
            .post("/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCareDocumentation2.xml")).asXml
        .check(status.is(500)))

    val VP007 = exec(http("VP007")
            .post("/FindAllQuestions/1/rivtabp20")
            .body(ElFileBody("user-files/data/FindAllQuestions1.xml")).asXml
        .check(status.is(500)))

    val VP009 = exec(http("VP009")
            .post("/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
            .body(ElFileBody("user-files/data/GetCareDocumentation2.xml")).asXml
        .check(status.is(500)))

	// Available scenarios for different types of setups
	val requestGetDiagnosis_2 = scenario("GetDiagnosis_2").feed(feeder1).exec(GetDiagnosis_2) 
	val requestGetMedicationHistory_2 = scenario("GetMedicationHistory_2").feed(feeder1).exec(GetMedicationHistory_2)
	val requestGetImagingOutcome_1 = scenario("GetImagingOutcome_1").feed(feeder1).exec(GetImagingOutcome_1)
	val requestGetLaboratoryOrderOutcome_3 = scenario("GetLaboratoryOrderOutcome_3").feed(feeder1).exec(GetLaboratoryOrderOutcome_3)
	val requestGetAlertInformation_2 = scenario("GetAlertInformation_2").feed(feeder1).exec(GetAlertInformation_2)
	val requestGetCareContacts_2 = scenario("GetCareContacts_2").feed(feeder1).exec(GetCareContacts_2)
	val requestGetCareDocumentation_2 = scenario("GetCareDocumentation_2").feed(feeder1).exec(GetCareDocumentation_2)
	val requestGetVaccinationHistory_2 = scenario("GetVaccinationHistory_2").feed(feeder1).exec(GetVaccinationHistory_2)
	val requestGetFunctionalStatus_2 = scenario("GetFunctionalStatus_2").feed(feeder1).exec(GetFunctionalStatus_2)
	val requestGetCarePlans_2 = scenario("GetCarePlans_2").feed(feeder1).exec(GetCarePlans_2)
    // Direktadresserad trafik
    val requestDirektadresserad = scenario("Direktadresserad").feed(feeder2).exec(Direktadresserad)
    // Updates (add followed by delete)
    val requestUpdate20 = scenario("Update20").feed(feederUpdate).exec(Update20)
    // Fault scenarios
    val requestVP004 = scenario("VP004").feed(feederVP004).exec(VP004)
    val requestVP007 = scenario("VP007").feed(feederVP007).exec(VP007)
    val requestVP009 = scenario("VP009").feed(feederVP009).exec(VP009)


  // Simulate NPÖ that requests patient data once per second
  // NOTE: Injections are run in parallell, stepts within an injection are sequencial
  setUp(
    requestDirektadresserad.inject(
	constantUsersPerSec(directRps) during(durationMinutes minutes)), 
    requestUpdate20.inject(
        (1 to durationMinutes).flatMap(i => Seq(constantUsersPerSec(updateRpsMin) during(55 seconds), constantUsersPerSec(updateRpsMax) during(5 seconds)))),
    requestVP004.inject(
        heavisideUsers(errorReqs) during (durationMinutes minutes)),
    requestVP007.inject(
        heavisideUsers(errorReqs) during (durationMinutes minutes)),
    requestVP009.inject(
        heavisideUsers(errorReqs) during (durationMinutes minutes)),
    requestGetDiagnosis_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetMedicationHistory_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetImagingOutcome_1.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetLaboratoryOrderOutcome_3.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetAlertInformation_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetCareContacts_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetCareDocumentation_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetVaccinationHistory_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetFunctionalStatus_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)),
    requestGetCarePlans_2.inject(
        rampUsersPerSec(aggRpsMin) to (aggRpsMax) during(1 minutes), 
        constantUsersPerSec(aggRpsMax) during(durationMinutes-1 minutes)))
    .protocols(httpConf)  
    .assertions(
        global.successfulRequests.percent.gt(99),
        details("Direktadresserad").successfulRequests.percent.is(100),
        details("GetDiagnosis_2").successfulRequests.percent.is(100),
        details("GetMedicationHistory_2").successfulRequests.percent.is(100),
        details("GetImagingOutcome_1").successfulRequests.percent.is(100), 
        details("GetLaboratoryOrderOutcome_3").successfulRequests.percent.is(100), 
        details("GetAlertInformation_2").successfulRequests.percent.is(100), 
        details("GetCareContacts_2").successfulRequests.percent.is(100), 
        details("GetCareDocumentation_2").successfulRequests.percent.is(100), 
        details("GetVaccinationHistory_2").successfulRequests.percent.is(100), 
        details("GetFunctionalStatus_2").successfulRequests.percent.is(100), 
        details("GetCarePlans_2").successfulRequests.percent.is(100)) 

}
