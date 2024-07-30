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

import uk.gov.hmrc.auth.core.AuthorisedFunctions
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.saliabilitiessandpitapi.config.AppConfig
import uk.gov.hmrc.saliabilitiessandpitapi.connectors.AuthConnector

trait AuthAction extends AuthorisedFunctions {
  val appConfig: AppConfig
  val httpClientV2_test: HttpClientV2

  protected given hc: HeaderCarrier = HeaderCarrier()

  override def authConnector = new AuthConnector:
    override val config: AppConfig = appConfig
    override val client: HttpClientV2 = httpClientV2_test
    override val serviceUrl: String   = config.authorisationService

    override def httpClientV2: HttpClientV2 = httpClientV2_test
}
