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

package uk.gov.hmrc.saliabilitiessandpitapi.connectors

import play.api.Logging
import uk.gov.hmrc.http.{JsValidationException, UpstreamErrorResponse}

import scala.concurrent.Future

private[this] trait Recoverable extends Logging:

  def recoverable[T]: PartialFunction[Throwable, Future[T]] =
    case e: UpstreamErrorResponse =>
      logger.warn(s"Received error status ${e.statusCode} from upstream")
      Future.failed(e)
    case e: JsValidationException =>
      logger.warn(s"Unable to parse the content of a response from downstream")
      Future.failed(e)
    case e: Throwable             =>
      logger.warn(s"Received an error from downstream")
      Future.failed(e)
