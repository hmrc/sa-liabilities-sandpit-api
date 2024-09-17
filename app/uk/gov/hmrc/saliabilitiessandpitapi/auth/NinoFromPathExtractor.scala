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

package uk.gov.hmrc.saliabilitiessandpitapi.auth

import play.api.mvc.Request
import uk.gov.hmrc.saliabilitiessandpitapi.auth.NinoFromPathExtractor.NinoPattern

import scala.util.matching.Regex

trait NinoFromPathExtractor:

  def extractNinoFromRequest(implicit request: Request[_]): Option[String] =
    NinoPattern findFirstMatchIn request.path map (_ group 1)

private[this] object NinoFromPathExtractor extends NinoFromPathExtractor:
  private val NinoPattern: Regex = """.*/nino/([A-Z]{2}[0-9]{6}[A-Z]{0,1}$)""".r
