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

package uk.gov.hmrc.saliabilitiessandpitapi.http

import play.api.http.Status
import play.api.http.Status.BAD_REQUEST

import scala.util.control.NoStackTrace

sealed trait LiabilityHttpException extends RuntimeException with NoStackTrace:
  def message: String
  def responseCode: Int

object LiabilityHttpException:
  case class InvalidPathParametersException(
    message: String = "Invalid path parameters",
    responseCode: Int = BAD_REQUEST
  ) extends LiabilityHttpException

  case class NinoNotFoundException(
    message: String = "NINO not found",
    responseCode: Int = BAD_REQUEST
  ) extends LiabilityHttpException
