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

import play.api.mvc.{Action, AnyContent, BaseController, Request}
import uk.gov.hmrc.saliabilitiessandpitapi.connectors.WithExecutionContext
import uk.gov.hmrc.saliabilitiessandpitapi.service.LiabilityService

private[controllers] trait LiabilityAction(using auth: AuthAction, validation: NINOValidationAction)
    extends WithExecutionContext {
  self: BaseController =>

  val liabilityService: LiabilityService

  def getLiabilityByNino(nino: String): Action[AnyContent] = auth andThen validation async {
    implicit request: Request[_] => liabilityService getLiability nino map (Ok(_))
  }
}
