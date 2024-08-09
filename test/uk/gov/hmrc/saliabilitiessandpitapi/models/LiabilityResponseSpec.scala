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
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.libs.json.*
import play.api.mvc.Codec
import uk.gov.hmrc.saliabilitiessandpitapi.models.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.*
import LiabilityResponse.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.LiabilityResponseSpec.*

object LiabilityResponseSpec:

  val balanceDetail: BalanceDetail = BalanceDetail(
    PayableAmount(BigDecimal(100.00)),
    PayableDueDate("2024-07-20"),
    PendingDueAmount(BigDecimal(100.02)),
    PendingDueDate("2024-08-20"),
    OverdueAmount(BigDecimal(100.03)),
    TotalBalance(BigDecimal(300.5))
  )

  val okResponse: LiabilityResponse = Ok(Seq(balanceDetail))
  val methodNotAllowedResponse: LiabilityResponse = MethodNotAllowed("Method not allowed")

class LiabilityResponseSpec extends AnyFunSuite with Matchers {

  test("LiabilityResponse.Ok should serialize to JSON correctly") {
    val expectedJson = Json.parse(
      """
        |{
        |  "balances": [
        |    {
        |      "payableAmount": 100.00,
        |      "payableDueDate": "2024-07-20",
        |      "pendingDueAmount": 100.02,
        |      "pendingDueDate": "2024-08-20",
        |      "overdueAmount": 100.03,
        |      "totalBalance": 300.5
        |    }
        |  ]
        |}
      """.stripMargin)

    Json.toJson(okResponse) shouldEqual expectedJson
  }

  test("LiabilityResponse.MethodNotAllowed should serialize to JSON correctly") {
    val expectedJson = Json.parse(
      """
        |{
        |  "description": "Method not allowed"
        |}
      """.stripMargin)

    Json.toJson(methodNotAllowedResponse) shouldEqual expectedJson
  }

  test("ContentTypeOf for LiabilityResponse should be JSON") {
    val contentType = implicitly[ContentTypeOf[LiabilityResponse]]

    contentType shouldEqual ContentTypeOf(Some(ContentTypes.JSON))
  }

  test("Writeable for LiabilityResponse should serialize data correctly") {
    implicit val codec: Codec = Codec.utf_8

    val jsonString = Json.toJson(okResponse).toString()
    val writeable = implicitly[Writeable[LiabilityResponse]]
    val result = writeable.transform(okResponse).utf8String

    result shouldEqual jsonString
  }
}