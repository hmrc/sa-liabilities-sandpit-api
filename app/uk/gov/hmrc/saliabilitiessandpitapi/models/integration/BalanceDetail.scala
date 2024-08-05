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

package uk.gov.hmrc.saliabilitiessandpitapi.models.integration

import play.api.libs.json.*
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.saliabilitiessandpitapi.models.*

import scala.util.{Failure, Success, Try}

case class BalanceDetail(
  payableAmount: PayableAmount,
  payableDueDate: PayableDueDate,
  pendingDueAmount: PendingDueAmount,
  pendingDueDate: PendingDueDate,
  overdueAmount: OverdueAmount,
  totalBalance: TotalBalance
)

object BalanceDetail:
  given Format[BalanceDetail] = Json.format[BalanceDetail]

  given Reads[BalanceDetail | Seq[BalanceDetail]] = (js: JsValue) =>
    val single   = js.validate[BalanceDetail]
    val sequence = js.validate[Seq[BalanceDetail]]

    (single, sequence) match {
      case (JsSuccess(single, _), _) => JsSuccess(single)
      case (_, JsSuccess(seq, _))    => JsSuccess(seq)
      case (JsError(errors), _)      => JsError(errors)
      case _                         => JsError("Unable to parse as BalanceDetail or Seq[BalanceDetail]")
    }

  given HttpReads[Either[ErrorResponse, BalanceDetail | Seq[BalanceDetail]]] = (_, _, response: HttpResponse) =>
    Try {
      val json: JsValue = response.json
      val status: Int   = response.status
      if ((status / 100) != 2) json.validate[ErrorResponse] match {
        case JsSuccess(errorResponse, _) => Left(errorResponse)
        case JsError(errors)             => Left(ErrorResponse(status, "Error parsing response", Some(errors.toString)))
      }
      else
        json.validate[BalanceDetail | Seq[BalanceDetail]] match {
          case JsSuccess(result, _) => Right(result)
          case JsError(errors)      => Left(ErrorResponse(status, "Error parsing response", Some(errors.toString)))
        }
    } match {
      case Success(result)    => result
      case Failure(exception) => Left(ErrorResponse(500, s"Internal server error: ${exception.getMessage}"))
    }
