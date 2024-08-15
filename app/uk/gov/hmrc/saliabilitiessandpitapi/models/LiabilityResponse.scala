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
  case Ok(balanceDetails: Seq[BalanceDetail])
  case MethodNotAllowed(description: String)

object LiabilityResponse:
  given OWrites[Ok]                                                 = writes[Ok]
  given OWrites[MethodNotAllowed]                                   = writes[MethodNotAllowed]
  given Writes[LiabilityResponse]                                   = Writes {
    case ok: Ok                             => toJsObject(ok)
    case methodNotAllowed: MethodNotAllowed => toJsObject(methodNotAllowed)
  }
  given contentTypeOf: ContentTypeOf[LiabilityResponse]             = ContentTypeOf(Some(ContentTypes.JSON))
  given writeable(using codec: Codec): Writeable[LiabilityResponse] =
    Writeable(data => codec.encode(toJson(data).toString))
