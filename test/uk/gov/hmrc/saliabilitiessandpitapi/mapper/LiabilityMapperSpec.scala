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

package uk.gov.hmrc.saliabilitiessandpitapi.mapper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityHttpException.NinoNotFoundException
import uk.gov.hmrc.saliabilitiessandpitapi.models.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail

class LiabilityMapperSpec extends AnyFunSuite, Matchers, MockitoSugar:

  val TestLiabilityMapper: LiabilityMapper = new LiabilityMapper {}

  test("mapToLiabilityResponse should throw NinoNotFoundException for Left(ErrorResponse)") {
    val errorResponse: Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]] = Left(mock[ErrorResponse])

    an[NinoNotFoundException] should be thrownBy TestLiabilityMapper.mapToLiabilityResponse(errorResponse)

  }

  test("mapToLiabilityResponse should return Ok with single BalanceDetail") {
    val balance                                                             = BalanceDetail(
      payableAmount = PayableAmount(100.00),
      payableDueDate = PayableDueDate("2024-07-20"),
      pendingDueAmount = PendingDueAmount(100.02),
      pendingDueDate = PendingDueDate("2024-08-20"),
      overdueAmount = OverdueAmount(100.03),
      totalBalance = TotalBalance(300.5)
    )
    val response: Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]] = Right(balance)
    val expectedResult                                                      = LiabilityResponse.Ok(Seq(balance))

    val result = TestLiabilityMapper.mapToLiabilityResponse(response)

    result shouldEqual expectedResult
  }

  test("mapToLiabilityResponse should return Ok with a sequence of BalanceDetail") {
    val balances                                                            = Seq(
      BalanceDetail(
        payableAmount = PayableAmount(100.00),
        payableDueDate = PayableDueDate("2024-07-20"),
        pendingDueAmount = PendingDueAmount(100.02),
        pendingDueDate = PendingDueDate("2024-08-20"),
        overdueAmount = OverdueAmount(100.03),
        totalBalance = TotalBalance(300.5)
      ),
      BalanceDetail(
        payableAmount = PayableAmount(200.00),
        payableDueDate = PayableDueDate("2024-08-20"),
        pendingDueAmount = PendingDueAmount(200.02),
        pendingDueDate = PendingDueDate("2024-09-20"),
        overdueAmount = OverdueAmount(200.03),
        totalBalance = TotalBalance(600.5)
      )
    )
    val expectedResult                                                      = LiabilityResponse.Ok(balances)
    val response: Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]] = Right(balances)

    val result = TestLiabilityMapper.mapToLiabilityResponse(response)

    result shouldEqual expectedResult
  }
