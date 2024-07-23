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

package uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions

import play.api.mvc.{Action, BaseController, Request, Result, Results}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.OptionAllowedAction._

trait OptionAllowedAction:
  self: BaseController =>

  val optionsEndpoint: Any => Action[_] = _ => Action(Results.Ok.withAllowHeaders(GET))

object OptionAllowedAction:
  private final val GET                                   = "GET"
  extension (result: Result)
    def withAllowHeaders(allowedMethods: String*): Result =
      result withHeaders "Allow" -> allowedMethods.mkString(", ")
