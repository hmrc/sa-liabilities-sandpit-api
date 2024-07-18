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

import org.mockito.ArgumentMatchers.{any, anyString}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.http.Status
import play.api.test.Helpers.*
import play.api.test.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.saliabilitiessandpitapi.config.AppConfig
import uk.gov.hmrc.saliabilitiessandpitapi.connectors.LiabilityConnector
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail

import scala.concurrent.Future

class MicroserviceHelloWorldControllerSpec extends AnyWordSpec with Matchers {

  private val fakeRequest = FakeRequest("GET", "/liability/nino/:nino")
  private val controller  = new MicroserviceHelloWorldController(
    mock[HttpClientV2],
    mock[AppConfig],
    Helpers.stubControllerComponents()
  ) with LiabilityConnector:
    override val fetchAllBalances: String => Future[Seq[BalanceDetail]] = nino => Future.successful(Seq())

  "GET /" should {
    "return 200" in {
      val result = controller.hello("QQ123456A")(fakeRequest)
      status(result) shouldBe Status.OK
    }
  }
}
