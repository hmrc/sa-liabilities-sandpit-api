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

import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.mvc.Results.Unauthorized
import play.api.mvc.*
import play.api.test.Helpers
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendHeaderCarrierProvider
import uk.gov.hmrc.saliabilitiessandpitapi.auth.{AuthorisationPredicate, NinoFromPathExtractor}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.AuthAction

import scala.concurrent.Future
import scala.concurrent.Future.*

object AuthActionStubs:

  private given controllerComponents: ControllerComponents = Helpers.stubControllerComponents()

  protected sealed trait MockedConnectorAuthAction
      extends AuthAction,
        BackendHeaderCarrierProvider,
        NinoFromPathExtractor,
        AuthorisationPredicate,
        AuthorisedFunctions:
    given authConnector: AuthConnector = mock[AuthConnector]

  case object SuccessfulAuthorisation extends MockedConnectorAuthAction:
    inline override def invokeBlock[A](using request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
      block(request)

  case object InsufficientEnrolmentsAuthorisation extends MockedConnectorAuthAction:
    inline override def invokeBlock[A](using request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
      successful(Unauthorized)
