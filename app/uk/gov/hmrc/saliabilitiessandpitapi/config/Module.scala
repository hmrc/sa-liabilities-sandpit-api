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

package uk.gov.hmrc.saliabilitiessandpitapi.config

import com.google.inject.AbstractModule
import uk.gov.hmrc.auth.core.{AuthConnector, PlayAuthConnector}
import uk.gov.hmrc.play.bootstrap.auth.DefaultAuthConnector
import uk.gov.hmrc.saliabilitiessandpitapi.connectors.{DefaultLiabilityConnector, LiabilityConnector}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.actions.{AuthAction, DefaultAuthAction, DefaultNINOValidationAction, NINOValidationAction}
import uk.gov.hmrc.saliabilitiessandpitapi.controllers.{DocumentationController, LiabilityController}
import uk.gov.hmrc.saliabilitiessandpitapi.service.{DefaultLiabilityService, LiabilityService}

class Module extends AbstractModule:

  override def configure(): Unit =
    bind(classOf[AppConfig]).asEagerSingleton()
    bind(classOf[LiabilityController]).asEagerSingleton()
    bind(classOf[DocumentationController]).asEagerSingleton()
    bind(classOf[NINOValidationAction]).to(classOf[DefaultNINOValidationAction]).asEagerSingleton()
    bind(classOf[LiabilityConnector]).to(classOf[DefaultLiabilityConnector]).asEagerSingleton()
    bind(classOf[LiabilityService]).to(classOf[DefaultLiabilityService]).asEagerSingleton()
    bind(classOf[AuthConnector]).to(classOf[DefaultAuthConnector]).asEagerSingleton()
    bind(classOf[AuthAction]).to(classOf[DefaultAuthAction]).asEagerSingleton()
