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

package uk.gov.hmrc.saliabilitiessandpitapi.models

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.*

class ModelsSpec extends AnyFunSuite with Matchers:

  test("PendingDueDate should serialize and deserialize correctly") {
    val date: PendingDueDate = PendingDueDate("2024-08-20")
    val json                 = Json.toJson(date)
    val expectedJson         = JsString("2024-08-20")

    json shouldEqual expectedJson
    json.as[PendingDueDate] shouldEqual date
  }

  test("PayableDueDate should serialize and deserialize correctly") {
    val date: PayableDueDate = PayableDueDate("2024-07-20")
    val json                 = Json.toJson(date)
    val expectedJson         = JsString("2024-07-20")

    json shouldEqual expectedJson
    json.as[PayableDueDate] shouldEqual date
  }

  test("TotalBalance should serialize and deserialize correctly") {
    val balance: TotalBalance = TotalBalance(BigDecimal(300.50))
    val json                  = Json.toJson(balance)
    val expectedJson          = JsNumber(300.50)

    json shouldEqual expectedJson
    json.as[TotalBalance] shouldEqual balance
  }

  test("PayableAmount should serialize and deserialize correctly") {
    val amount: PayableAmount = PayableAmount(BigDecimal(100.00))
    val json                  = Json.toJson(amount)
    val expectedJson          = JsNumber(100.00)

    json shouldEqual expectedJson
    json.as[PayableAmount] shouldEqual amount
  }

  test("PendingDueAmount should serialize and deserialize correctly") {
    val amount: PendingDueAmount = PendingDueAmount(BigDecimal(100.02))
    val json                     = Json.toJson(amount)
    val expectedJson             = JsNumber(100.02)

    json shouldEqual expectedJson
    json.as[PendingDueAmount] shouldEqual amount
  }

  test("OverdueAmount should serialize and deserialize correctly") {
    val amount: OverdueAmount = OverdueAmount(BigDecimal(100.03))
    val json                  = Json.toJson(amount)
    val expectedJson          = JsNumber(100.03)

    json shouldEqual expectedJson
    json.as[OverdueAmount] shouldEqual amount
  }
