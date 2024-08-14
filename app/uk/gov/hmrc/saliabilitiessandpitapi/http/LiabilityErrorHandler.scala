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

import play.api.Configuration
import play.api.mvc.{RequestHeader, Result, Results}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.backend.http.JsonErrorHandler
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorHandler.*
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorResponse.{InvalidInputNino, NinoNotFound}
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityHttpException.{InvalidPathParametersException, NinoNotFoundException}

import javax.inject.Inject
import scala.concurrent.Future.*
import scala.concurrent.{ExecutionContext, Future}

class LiabilityErrorHandler @Inject() (
  auditConnector: AuditConnector,
  httpAuditEvent: HttpAuditEvent,
  configuration: Configuration
)(implicit ec: ExecutionContext)
    extends JsonErrorHandler(auditConnector, httpAuditEvent, configuration):

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = exception match {
    case exception: LiabilityHttpException => successful(exception.toResult)
    case _                                 => super.onServerError(request, exception)
  }

private object LiabilityErrorHandler:
  extension (liabilityHttpException: LiabilityHttpException)
    inline def toResult: Result = liabilityHttpException match
      case NinoNotFoundException(message, responseCode)          => ErrorResultCreator(responseCode)(NinoNotFound)
      case InvalidPathParametersException(message, responseCode) => ErrorResultCreator(responseCode)(InvalidInputNino)
