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

package uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.mvc.Results.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.NINOValidationActionSpec.TestController

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

class NINOValidationActionSpec extends AnyFunSuite, Matchers {

  private val controller: TestController = TestController()

  test("Valid NINO should pass validation") {
    val validNino = "AB123456C"
    val request   = FakeRequest(GET, s"/path/$validNino")
    val result    = (controller testAction validNino)(request)

    status(result) shouldEqual OK
    contentAsString(result) shouldEqual "Valid NINO"
  }

  test("Invalid NINO should return BadRequest with correct JSON error response") {
    val invalidNino = "AB12345"
    val request     = FakeRequest(GET, s"/path/$invalidNino")
    val result      = (controller testAction invalidNino)(request)

    status(result) shouldEqual BAD_REQUEST
    headers(result) get "CorrelationId" shouldBe defined

    val expectedJson = Json.parse("""{"errorCode":"1113","errorDescription":"Invalid path parameters"}""")
    val actualJson   = contentAsJson(result)

    actualJson shouldEqual expectedJson
  }

  test("Request without NINO should return BadRequest with correct JSON error response") {
    val request = FakeRequest(GET, "/path/")
    val result  = (controller testAction "invalid")(request)

    status(result) shouldEqual BAD_REQUEST

    val expectedJson = Json.parse("""{"errorCode":"1113","errorDescription":"Invalid path parameters"}""")
    val actualJson   = contentAsJson(result)

    actualJson shouldEqual expectedJson
  }
}

private object NINOValidationActionSpec:
  class TestController(
    val controllerComponents: ControllerComponents = stubControllerComponents(),
    val executionContext: ExecutionContext = stubControllerComponents().executionContext
  ) extends BaseController
      with NINOValidationAction:

    def testAction(path: String): Action[AnyContent] = Action.async(filter(_).map {
      case Some(result) => result
      case None         => Ok("Valid NINO")
    })
