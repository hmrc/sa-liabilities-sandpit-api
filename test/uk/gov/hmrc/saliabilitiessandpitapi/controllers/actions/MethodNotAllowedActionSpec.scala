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

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.MethodNotAllowedActionSpec.TestController

class MethodNotAllowedActionSpec extends AnyFunSuite, Matchers:

  private val controller = new TestController()
  given ActorSystem = ActorSystem("test-system")

  test("methodNotAllowed should return MethodNotAllowed response with correct message") {
    val request = FakeRequest(GET, "/test")
    val expectedJson = Json.obj("description" -> "Method Not Allowed")

    val result = (controller methodNotAllowed "dummy")(request)

    status(result) shouldEqual METHOD_NOT_ALLOWED
    contentType(result) shouldEqual Some("application/json")
    contentAsJson(result) shouldEqual expectedJson
  }

private object MethodNotAllowedActionSpec:
  class TestController extends BaseController with MethodNotAllowedAction:
    override def controllerComponents: ControllerComponents = stubControllerComponents()
