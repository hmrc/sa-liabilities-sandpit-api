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
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents, Request, Result}
import play.api.test.*
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.LiabilityControllerSpec.method.*
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.LiabilityControllerSpec.{invalidInputNino, method}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.{AuthAction, NINOValidationAction}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.stubs.AuthActionStubs
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.stubs.AuthActionStubs.*
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.stubs.NINOValidationActionStubs.{FailingNINOValidationAction, ValidNINOValidationAction}
import uk.gov.hmrc.saliabilitiessandpitapi.models.LiabilityResponse
import uk.gov.hmrc.saliabilitiessandpitapi.models.LiabilityResponse.*
import uk.gov.hmrc.saliabilitiessandpitapi.service.LiabilityService

import scala.concurrent.Future.*
import scala.concurrent.{ExecutionContext, Future}

class LiabilityControllerSpec extends AnyWordSpec, Matchers:

  private val service: LiabilityService = mock[LiabilityService]
  private val getLiabilityFunction      = mock[String => Future[LiabilityResponse]]

  private given components: ControllerComponents = Helpers.stubControllerComponents()
  private given ec: ExecutionContext             = components.executionContext

  "GET /liability/nino/:nino endpoint" should {

    "returns 200 for QQ123456A" in {
      given request: Request[AnyContentAsEmpty.type] = FakeRequest(GET, "/liability/nino/QQ123456A")
      given authAction: AuthAction                   = SuccessfulAuthorisation
      given validation: NINOValidationAction         = ValidNINOValidationAction
      val controller: LiabilityController            = LiabilityController(service)

      when(getLiabilityFunction.apply(any[String])) thenReturn successful(invalidInputNino)
      when(service.getLiability) thenReturn getLiabilityFunction

      val result: Future[Result] = controller.getLiabilityByNino("QQ123456A")(request)

      status(result) shouldBe Status.OK
      verify(service.getLiability)("QQ123456A")
    }

    "returns 400 for invalid NINO format" in {
      given request: Request[AnyContentAsEmpty.type] = FakeRequest(GET, "/liability/INVALID")
      given authAction: AuthAction                   = SuccessfulAuthorisation
      given validation: NINOValidationAction         = FailingNINOValidationAction
      val controller: LiabilityController            = LiabilityController(service)

      val result: Future[Result] = controller.getLiabilityByNino("INVALID")(request)

      status(result)        shouldBe Status.BAD_REQUEST
      contentAsString(result) should include("Invalid NINO format")
    }

    "returns 400 for insufficient enrolments" in {
      given request: Request[AnyContentAsEmpty.type] = FakeRequest(GET, "/liability/QQ123456A")
      given authAction: AuthAction                   = InsufficientEnrolmentsAuthorisation
      given validation: NINOValidationAction         = ValidNINOValidationAction
      val controller: LiabilityController            = LiabilityController(service)

      val result: Future[Result] = controller.getLiabilityByNino("QQ123456A")(request)

      status(result) shouldBe Status.UNAUTHORIZED
    }
  }

private object LiabilityControllerSpec:
  private val invalidInputNino: LiabilityResponse = Ok(Seq())

  object method:
    val GET = "GET"
