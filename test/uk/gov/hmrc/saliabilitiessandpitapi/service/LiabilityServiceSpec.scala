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

package uk.gov.hmrc.saliabilitiessandpitapi.service

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfter
import org.scalatest.RecoverMethods.recoverToExceptionIf
import org.scalatest.concurrent.Futures.whenReady
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.test.Helpers
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.saliabilitiessandpitapi.connectors.LiabilityConnector
import uk.gov.hmrc.saliabilitiessandpitapi.mapper.LiabilityMapper
import uk.gov.hmrc.saliabilitiessandpitapi.models.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail
import uk.gov.hmrc.saliabilitiessandpitapi.service.LiabilityServiceSpec.{balanceDetail, emptyLiabilityResponse, mockLiabilityResponse}

import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future, TimeoutException}

class LiabilityServiceSpec
    extends AnyFunSuite,
      Matchers,
      ScalaFutures,
      BeforeAndAfter,
      LiabilityService,
      LiabilityMapper:
  given ec: ExecutionContext = Helpers.stubControllerComponents().executionContext

  val liabilityConnector: LiabilityConnector = mock[LiabilityConnector]
  private val fetchAllBalancesFunction       =
    mock[String => Future[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]

  before {
    when(liabilityConnector.fetchAllBalances) thenReturn fetchAllBalancesFunction
  }

  test("getLiability should return a valid liability response for a given NINO") {
    when(fetchAllBalancesFunction apply any[String]) thenReturn successful(Right(Seq(balanceDetail)))

    val result = getLiability("AA123456A")

    whenReady(result)(_ shouldBe mockLiabilityResponse)
  }

  test("getLiability should handle an empty response from the connector") {
    when(fetchAllBalancesFunction apply any[String]) thenReturn successful(Right(Seq.empty))

    val result = getLiability("AA123456A")

    whenReady(result)(_ shouldBe emptyLiabilityResponse)
  }

  test("getLiability should return a failed future when the connector fails") {
    when(liabilityConnector fetchAllBalances any[String]) thenReturn failed(new RuntimeException("Connector error"))

    val result = getLiability("AA123456A")

    recoverToExceptionIf[RuntimeException](result) map (_.getMessage shouldBe "Connector error")
  }

  test("getLiability should handle multiple requests concurrently") {
    when(fetchAllBalancesFunction apply any[String]) thenReturn successful(Right(Seq(balanceDetail)))
    lazy val future1: Future[LiabilityResponse] = getLiability("AA123456A")
    lazy val future2: Future[LiabilityResponse] = getLiability("ZZ123456B")

    val result = Future sequence Seq(future1, future2)

    whenReady(result) { results =>
      results should have size 2
      results foreach (_ shouldBe mockLiabilityResponse)
    }
  }

  test("getLiability should return an empty response if the NINO is invalid") {
    when(liabilityConnector fetchAllBalances any[String]) thenReturn successful(Right(Seq.empty))

    val result = getLiability("INVALID_NINO")

    whenReady(result)(_ shouldBe emptyLiabilityResponse)
  }

  test("getLiability should return an error when the connector times out") {
    when(liabilityConnector fetchAllBalances any[String]) thenReturn failed(new TimeoutException("Request timed out"))

    val result = getLiability("AA123456A")

    recoverToExceptionIf[TimeoutException](result) map (_.getMessage shouldBe "Request timed out")
  }

private[this] object LiabilityServiceSpec:
  val balanceDetail: BalanceDetail = BalanceDetail(
    payableAmount = PayableAmount(BigDecimal(100.00)),
    payableDueDate = Some(PayableDueDate("2024-07-20")),
    pendingDueAmount = PendingDueAmount(BigDecimal(100.02)),
    pendingDueDate = Some(PendingDueDate("2024-08-20")),
    overdueAmount = OverdueAmount(BigDecimal(100.03)),
    totalBalance = Some(TotalBalance(BigDecimal(300.5)))
  )

  val mockLiabilityResponse: LiabilityResponse  = LiabilityResponse.Ok(Seq(balanceDetail))
  val emptyLiabilityResponse: LiabilityResponse = LiabilityResponse.Ok(Seq.empty)
