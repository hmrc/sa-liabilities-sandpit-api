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
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import play.api.libs.json.Json.toJson
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorResponse.{InvalidInputNino, NinoNotFound}
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityHttpException.{InvalidPathParametersException, NinoNotFoundException}

import scala.concurrent.Future

class LiabilityErrorHandlerSpec extends AnyFunSuite, MockitoSugar, Matchers:

  val errorHandler: LiabilityErrorHandler = new LiabilityErrorHandler(
    auditConnector = mock[AuditConnector],
    httpAuditEvent = mock[HttpAuditEvent],
    configuration = mock[Configuration]
  )(stubControllerComponents().executionContext)

  val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  test("should handle NinoNotFoundException and return the correct result") {
    val exception              = NinoNotFoundException()
    val expectedResult         = toJson(NinoNotFound).toString
    val result: Future[Result] = errorHandler.onServerError(request, exception)

    status(result)          shouldBe 400
    contentAsString(result) shouldBe expectedResult
  }

  test("should add a CorrelationId header to the result") {
    val exception              = NinoNotFoundException()
    val result: Future[Result] = errorHandler.onServerError(request, exception)

    status(result)                      shouldBe 400
    headers(result) get "CorrelationId" shouldBe defined
  }

  test("should delegate unknown exceptions to the superclass handler") {
    val exception = new RuntimeException("Unknown error")

    val result: Future[Result] = errorHandler.onServerError(request, exception)

    status(result) shouldBe 500
    assert(contentAsString(result).contains("Unknown error"))
  }

  test("should handle InvalidPathParametersException and return the correct result") {
    val exception      = InvalidPathParametersException()
    val expectedResult = toJson(InvalidInputNino).toString

    val result: Future[Result] = errorHandler.onServerError(request, exception)

    status(result)          shouldBe 400
    contentAsString(result) shouldBe expectedResult
  }
