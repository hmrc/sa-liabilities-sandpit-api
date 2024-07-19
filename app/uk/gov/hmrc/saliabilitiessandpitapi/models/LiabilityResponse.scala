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
import play.api.mvc.Codec
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail

enum LiabilityResponse:
  case Ok(balances: Seq[BalanceDetail])
  case Error(error: String, description: String)
  case InvalidInputNino(description: String)

object LiabilityResponse:
  given Format[Ok] = Json.format[Ok]

  given Format[Error] = Json.format[Error]

  given Format[InvalidInputNino] = Json.format[InvalidInputNino]

  given contentTypeOf: ContentTypeOf[LiabilityResponse] = ContentTypeOf(Some(ContentTypes.JSON))

  given writeable(using codec: Codec): Writeable[LiabilityResponse] =
    Writeable(data => codec.encode(Json.toJson(data).toString()))

  given Format[LiabilityResponse] = new Format[LiabilityResponse]:
    def reads(json: JsValue): JsResult[LiabilityResponse] =
      (json \ "type").as[String] match
        case "Ok"               => summon[Format[Ok]].reads(json)
        case "Error"            => summon[Format[Error]].reads(json)
        case "InvalidInputNino" => summon[Format[InvalidInputNino]].reads(json)
        case _                  => JsError("Unknown type")

    def writes(o: LiabilityResponse): JsValue = o match
      case ok: Ok                             => summon[Format[Ok]].writes(ok).as[JsObject] + ("type"       -> JsString("Ok"))
      case error: Error                       => summon[Format[Error]].writes(error).as[JsObject] + ("type" -> JsString("Error"))
      case invalidInputNino: InvalidInputNino =>
        summon[Format[InvalidInputNino]].writes(invalidInputNino).as[JsObject] + ("type" -> JsString("Error"))
