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

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.mvc.Result
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityErrorResponse.NinoNotFound

import java.*
import java.util.UUID
import java.util.UUID.fromString

class ErrorResultCreatorTestSpec extends AnyFunSuite, Matchers:

  test("LiabilityStatus should create a Result with the correct status code") {
    val statusCode: Int                       = 400
    val errorResponse: LiabilityErrorResponse = NinoNotFound

    val result: Result = ErrorResultCreator(statusCode)(errorResponse)

    result.header.status shouldEqual statusCode
  }

  test("LiabilityStatus should include a CorrelationId header in the Result") {
    val statusCode: Int                       = 400
    val errorResponse: LiabilityErrorResponse = NinoNotFound

    val result: Result = ErrorResultCreator(statusCode)(errorResponse)

    result.header.headers get "CorrelationId" match
      case Some(correlationId: String) => fromString(correlationId) shouldBe a[UUID]
      case None                        => fail("CorrelationId header is missing")
  }
