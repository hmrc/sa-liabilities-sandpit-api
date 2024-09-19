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

import play.api.http.Status.BAD_REQUEST
import play.api.mvc.{ActionFilter, Request, Result, Results}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.NINOValidationAction.*
import uk.gov.hmrc.saliabilitiessandpitapi.http.ErrorResultCreator
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorResponse.InvalidInputNino

import scala.concurrent.Future
import scala.concurrent.Future.successful

trait NINOValidationAction extends ActionFilter[Request]:

  override protected def filter[A](request: Request[A]): Future[Option[Result]] = successful {
    request.lastSegment match {
      case Some(input: String) if ninoPattern matches input => None
      case _                                                => Some(InvalidInputNinoResult)
    }
  }

private object NINOValidationAction:
  private final val ninoPattern            = "^[A-Z]{2}[0-9]{6}[A-Z]{0,1}$".r
  private final val InvalidInputNinoResult = ErrorResultCreator(BAD_REQUEST)(InvalidInputNino)

  extension (request: Request[_]) {
    private def lastSegment: Option[String] = request.path
      .split("/")
      .filter(_.nonEmpty)
      .lastOption
  }
