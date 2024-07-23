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
  case Ok(balances: Seq[BalanceDetail])
  case Error(error: String, description: String)
  case InvalidInputNino(description: String)
  case MethodNotAllowed(description: String)
  case NotFound(description: String)
  case Unauthorized(description: String)
  case InternalServerError(description: String)

object LiabilityResponse:
  given OWrites[Ok]                                                 = writes[Ok]
  given OWrites[Error]                                              = writes[Error]
  given OWrites[InvalidInputNino]                                   = writes[InvalidInputNino]
  given OWrites[MethodNotAllowed]                                   = writes[MethodNotAllowed]
  given OWrites[NotFound]                                           = writes[NotFound]
  given OWrites[InternalServerError]                                = writes[InternalServerError]
  given OWrites[Unauthorized]                                       = writes[Unauthorized]
  given Writes[LiabilityResponse]                                   = Writes {
    case ok: Ok                                   => toJsObject(ok)
    case error: Error                             => toJsObject(error)
    case notFound: NotFound                       => toJsObject(notFound)
    case unauthorized: Unauthorized               => toJsObject(unauthorized)
    case invalidInputNino: InvalidInputNino       => toJsObject(invalidInputNino)
    case methodNotAllowed: MethodNotAllowed       => toJsObject(methodNotAllowed)
    case internalServerError: InternalServerError => toJsObject(internalServerError)
  }
  given contentTypeOf: ContentTypeOf[LiabilityResponse]             = ContentTypeOf(Some(ContentTypes.JSON))
  given writeable(using codec: Codec): Writeable[LiabilityResponse] =
    Writeable(data => codec.encode(toJson(data).toString))
