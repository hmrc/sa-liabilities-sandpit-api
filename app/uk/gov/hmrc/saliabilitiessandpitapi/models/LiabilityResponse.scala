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

package uk.gov.hmrc.saliabilitiessandpitapi.models;

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.libs.json.*
import play.api.libs.json.Json.*
import play.api.mvc.Codec
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail

enum LiabilityResponse:
  case Ok(balances: Seq[BalanceDetail]) extends LiabilityResponse, SuccessResponse
  case Error(error: String, description: String) extends LiabilityResponse, FailureResponse
  case InvalidInputNino(description: String) extends LiabilityResponse, FailureResponse
  case MethodNotAllowed(description: String) extends LiabilityResponse, FailureResponse
  case NotFound(description: String) extends LiabilityResponse, FailureResponse
  case Unauthorized(description: String) extends LiabilityResponse, FailureResponse
  case InternalServerError(description: String) extends LiabilityResponse, FailureResponse

object LiabilityResponse:
  given Format[Ok]                                                  = format[Ok]
  given Format[Error]                                               = format[Error]
  given Format[InvalidInputNino]                                    = format[InvalidInputNino]
  given Format[MethodNotAllowed]                                    = format[MethodNotAllowed]
  given Format[NotFound]                                            = format[NotFound]
  given Format[InternalServerError]                                 = format[InternalServerError]
  given Format[Unauthorized]                                        = format[Unauthorized]
  given contentTypeOf: ContentTypeOf[LiabilityResponse]             = ContentTypeOf(Some(ContentTypes.JSON))
  given writeable(using codec: Codec): Writeable[LiabilityResponse] =
    Writeable(data => codec.encode(toJson(data).toString()))

  given Format[LiabilityResponse] = new Format[LiabilityResponse]:
    def reads(json: JsValue): JsResult[LiabilityResponse] =
      (json \ "type").as[String] match
        case "Ok"                  => fromJson[Ok](json)
        case "Error"               => fromJson[Error](json)
        case "InvalidInputNino"    => fromJson[InvalidInputNino](json)
        case "MethodNotAllowed"    => fromJson[MethodNotAllowed](json)
        case "NotFound"            => fromJson[NotFound](json)
        case "Unauthorized"        => fromJson[Unauthorized](json)
        case "InternalServerError" => fromJson[InternalServerError](json)
        case _                     => JsError("Unknown type")

    def writes(o: LiabilityResponse): JsValue = o match
      case ok: Ok                 => toJson(ok).as[JsObject] + ("type" -> JsString("Ok"))
      case e: Error               => toJson(e).as[JsObject] + ("type"  -> JsString("Error"))
      case e: InvalidInputNino    => toJson(e).as[JsObject] + ("type"  -> JsString("InvalidInputNino"))
      case e: MethodNotAllowed    => toJson(e).as[JsObject] + ("type"  -> JsString("MethodNotAllowed"))
      case e: NotFound            => toJson(e).as[JsObject] + ("type"  -> JsString("NotFound"))
      case e: Unauthorized        => toJson(e).as[JsObject] + ("type"  -> JsString("Unauthorized"))
      case e: InternalServerError => toJson(e).as[JsObject] + ("type"  -> JsString("InternalServerError"))
