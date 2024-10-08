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

import controllers.Assets
import play.api.mvc.Action
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.DefinitionAction.{FILE, ROOT_FOLDER}

private[controllers] trait DefinitionAction:
  val assets: Assets
  val definition: Action[_] = assets.at(ROOT_FOLDER, FILE)

private object DefinitionAction:
  private final val ROOT_FOLDER = "/public/api"
  private final val FILE        = "definition.json"
