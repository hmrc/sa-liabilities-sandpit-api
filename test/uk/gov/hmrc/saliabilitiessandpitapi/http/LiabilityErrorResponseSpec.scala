/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.saliabilitiessandpitapi.http

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.{Json, Writes}
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorResponse.*

class LiabilityErrorResponseSpec extends AnyFunSuite:

  test("InvalidInputNino should have correct errorCode and errorDescription") {
    val response = InvalidInputNino

    assert(response.errorCode == "1113")
    assert(response.errorDescription == "Invalid path parameters")
  }

  test("NinoNotFound should have correct errorCode and errorDescription") {
    val response = NinoNotFound

    assert(response.errorCode == "1002")
    assert(response.errorDescription == "NINO not found")
  }

  test("InvalidInputNino should serialize to correct JSON") {
    val response = InvalidInputNino

    val json = Json.toJson(response)

    assert((json \ "errorCode").as[String] == "1113")
    assert((json \ "errorDescription").as[String] == "Invalid path parameters")
  }

  test("NinoNotFound should serialize to correct JSON") {
    val response = NinoNotFound

    val json = Json.toJson(response)(LiabilityErrorResponse.given_Writes_LiabilityErrorResponse)

    assert((json \ "errorCode").as[String] == "1002")
    assert((json \ "errorDescription").as[String] == "NINO not found")
  }
