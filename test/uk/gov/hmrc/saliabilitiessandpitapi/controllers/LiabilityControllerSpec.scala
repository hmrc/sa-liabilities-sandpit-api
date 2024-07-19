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

package uk.gov.hmrc.saliabilitiessandpitapi.controllers

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.http.Status
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents, Request}
import play.api.test.*
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.stubs.NINOValidationActionStubs.validNINO
import uk.gov.hmrc.saliabilitiessandpitapi.models.LiabilityResponse
import uk.gov.hmrc.saliabilitiessandpitapi.service.LiabilityService

import scala.concurrent.Future.*
import scala.concurrent.{ExecutionContext, Future}

class LiabilityControllerSpec extends AnyWordSpec with Matchers {

  private val fakeRequest: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/liability/nino/QQ123456A")
  private implicit val components: ControllerComponents    = Helpers.stubControllerComponents()
  private implicit val ec: ExecutionContext                = components.executionContext
  private val service: LiabilityService                    = mock[LiabilityService]
  private val getLiabilityFunction                         = mock[String => Future[LiabilityResponse]]
  private val controller                                   = new LiabilityController(service, validNINO)

  "GET /liability/nino/QQ123456A" should {
    "return 200" in {
      when(getLiabilityFunction.apply(any[String]))
        .thenReturn(successful(LiabilityResponse.InvalidInputNino("Invalid NINO format.")))
      when(service.getLiability).thenReturn(getLiabilityFunction)

      val result = controller.getLiabilityByNino("QQ123456A")(fakeRequest)

      status(result) shouldBe Status.OK
      verify(service.getLiability)("QQ123456A")
    }
  }
}
