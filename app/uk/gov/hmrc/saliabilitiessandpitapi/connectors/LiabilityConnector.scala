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

import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.backend.http.ErrorResponse
import uk.gov.hmrc.saliabilitiessandpitapi.config.AppConfig
import uk.gov.hmrc.saliabilitiessandpitapi.models.integration.BalanceDetail

import scala.concurrent.Future

trait LiabilityConnector extends WithExecutionContext with Recoverable:
  val config: AppConfig
  val client: HttpClientV2
  protected given hc: HeaderCarrier = HeaderCarrier()
  protected given service: String   = config.integrationService

  val fetchAllBalances: String => Future[Either[ErrorResponse, Seq[BalanceDetail]]] = nino =>
    client
      .get(url"$service/balance/$nino")
      .execute[Either[ErrorResponse, Seq[BalanceDetail]]]
      .recoverWith(recoverable)