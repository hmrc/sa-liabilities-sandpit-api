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

package uk.gov.hmrc.saliabilitiessandpitapi.controllers.stubs

import play.api.libs.json.Json
import play.api.libs.json.Json.*
import play.api.mvc.*
import play.api.test.Helpers
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.NINOValidationAction

import scala.concurrent.Future.*
import scala.concurrent.{ExecutionContext, Future}

object NINOValidationActionStubs:

  protected sealed trait MockedNINOValidationAction extends NINOValidationAction:
    given executionContext: ExecutionContext = Helpers.stubControllerComponents().executionContext

  case object ValidNINOValidationAction extends MockedNINOValidationAction:
    override protected def filter[A](request: Request[A]): Future[Option[Result]] = successful(None)

  case object FailingNINOValidationAction extends MockedNINOValidationAction:
    override protected def filter[A](request: Request[A]): Future[Option[Result]] =
      successful(Some(Results BadRequest obj("error" -> "Invalid NINO format.")))
