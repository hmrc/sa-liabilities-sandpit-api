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
import play.api.mvc.*
import play.api.test.*
import play.api.test.Helpers.*
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.LiabilityControllerSpec.method.GET
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.OptionAllowedActionSpec.controller

import scala.concurrent.Future

class OptionAllowedActionSpec extends AnyFunSuite, Matchers:

  test("OptionAllowedAction should add Allow headers to the result"):
    val request  = FakeRequest(GET, "/liability/nino/QQ123456A")
    
    val result: Future[Result] = (controller optionsEndpoint "dummy")(request)

    status(result) shouldBe OK
    headers(result) should contain("Allow" -> "GET")

private object OptionAllowedActionSpec:
  val controller: OptionAllowedAction & BaseController = new OptionAllowedAction with BaseController:
    override def controllerComponents: ControllerComponents = stubControllerComponents()