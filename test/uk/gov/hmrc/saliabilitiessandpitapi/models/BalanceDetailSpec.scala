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

import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.libs.json.*
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.saliabilitiessandpitapi.models.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.BalanceDetailSpec.*
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.*

object BalanceDetailSpec:
  val balanceDetailJson: JsValue = Json.parse("""
    {
      "totalBalance":300.5,
      "pendingDueDate":"2024-08-20",
      "pendingDueAmount":100.02,
      "payableAmount":100,
      "overdueAmount":100.03,
      "payableDueDate":"2024-07-20"
    }
  """)

  val balanceDetail: BalanceDetail = BalanceDetail(
    payableAmount = PayableAmount(BigDecimal(100.00)),
    payableDueDate = PayableDueDate("2024-07-20"),
    pendingDueAmount = PendingDueAmount(BigDecimal(100.02)),
    pendingDueDate = PendingDueDate("2024-08-20"),
    overdueAmount = OverdueAmount(BigDecimal(100.03)),
    totalBalance = TotalBalance(BigDecimal(300.5))
  )

class BalanceDetailSpec extends AnyFunSuite, Matchers:

  test("BalanceDetail should be serialized to JSON correctly") {
    val json = Json.toJson(balanceDetail)

    json shouldEqual balanceDetailJson
  }

  test("BalanceDetail should be deserialized from JSON correctly") {
    val result = balanceDetailJson.validate[BalanceDetail]
    result shouldEqual JsSuccess(balanceDetail)
  }

  test("Reads should handle single BalanceDetail and Seq[BalanceDetail] correctly") {
    val singleJson = Json.toJson(balanceDetail)
    val seqJson    = Json.toJson(Seq(balanceDetail))

    singleJson.validate[BalanceDetail | Seq[BalanceDetail]] shouldEqual JsSuccess(balanceDetail)
    seqJson.validate[BalanceDetail | Seq[BalanceDetail]] shouldEqual JsSuccess(Seq(balanceDetail))

    val invalidJson = Json.parse("""{ "invalid": "data" }""")
    invalidJson.validate[BalanceDetail | Seq[BalanceDetail]] shouldBe a[JsError]
  }

  test("HttpReads should parse successful response with BalanceDetail") {
    val response = mock[HttpResponse]
    when(response.status).thenReturn(200)
    when(response.json).thenReturn(balanceDetailJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Right(balanceDetail)
  }

  test("HttpReads should parse successful response with Seq[BalanceDetail]") {
    val seqJson  = Json.toJson(Seq(balanceDetail))
    val response = mock[HttpResponse]
    when(response.status).thenReturn(200)
    when(response.json).thenReturn(seqJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Right(Seq(balanceDetail))
  }

  test("HttpReads should parse error response") {
    val errorResponseJson = Json.parse("""{ "status": 400, "message": "Bad Request" }""")
    val response          = mock[HttpResponse]
    when(response.status).thenReturn(400)
    when(response.json).thenReturn(errorResponseJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Left(
      ErrorResponse(
        400,
        "Error parsing response",
        Some("List((/statusCode,List(JsonValidationError(List(error.path.missing),ArraySeq()))))"),
        None
      )
    )
  }

  test("BalanceDetail should return JsError when required fields are missing") {
    val invalidJson = Json.parse("""  {  "someOtherField": "unexpected value" } """)
    val result      = invalidJson.validate[BalanceDetail]

    result shouldEqual JsError(
      List(
        (JsPath \ "totalBalance", List(JsonValidationError("error.path.missing"))),
        (JsPath \ "overdueAmount", List(JsonValidationError("error.path.missing"))),
        (JsPath \ "pendingDueDate", List(JsonValidationError("error.path.missing"))),
        (JsPath \ "pendingDueAmount", List(JsonValidationError("error.path.missing"))),
        (JsPath \ "payableDueDate", List(JsonValidationError("error.path.missing"))),
        (JsPath \ "payableAmount", List(JsonValidationError("error.path.missing")))
      )
    )
  }

  test("HttpReads should return ErrorResponse when BalanceDetail JSON parsing fails") {
    val invalidJson = Json.parse("""{ "invalidField": "unexpected value" }""")
    val response    = mock[HttpResponse]
    when(response.status).thenReturn(200)
    when(response.json).thenReturn(invalidJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Left(
      ErrorResponse(
        200,
        "Error parsing response",
        Some(
          "List((,List(JsonValidationError(List(Unable to parse as BalanceDetail or Seq[BalanceDetail]),ArraySeq()))))"
        ),
        None
      )
    )
  }

  test("HttpReads should return ErrorResponse when error response JSON is valid") {
    val errorResponseJson = Json.parse("""{ "statusCode": 400, "message": "Bad Request" }""")
    val response          = mock[HttpResponse]
    when(response.status).thenReturn(400)
    when(response.json).thenReturn(errorResponseJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Left(ErrorResponse(400, "Bad Request"))
  }

  test("HttpReads should handle parse error response") {
    val invalidJson = Json.parse("""{ "invalid": "data" }""")
    val response    = mock[HttpResponse]
    when(response.status).thenReturn(400)
    when(response.json).thenReturn(invalidJson)

    val result = implicitly[HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]]]
      .read("GET", "foo", response)

    result shouldEqual Left(
      ErrorResponse(
        400,
        "Error parsing response",
        Some(
          "List((/message,List(JsonValidationError(List(error.path.missing),ArraySeq()))), (/statusCode,List(JsonValidationError(List(error.path.missing),ArraySeq()))))"
        ),
        None
      )
    )
  }
