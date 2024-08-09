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
import uk.gov.hmrc.saliabilitiessandpitapi.http.LiabilityHttpException.{InvalidPathParametersException, NinoNotFoundException}

class LiabilityHttpExceptionSpec extends AnyFunSuite, Matchers:

  test("InvalidPathParametersException should have correct message and responseCode"):
    val exception = InvalidPathParametersException()

    exception.message shouldEqual "Invalid NINO format received."
    exception.responseCode shouldEqual 400

  test("NinoNotFoundException should have correct message and responseCode"):
    val exception = NinoNotFoundException()

    exception.message shouldEqual "The provided NINO was not found in the system."
    exception.responseCode shouldEqual 400

  test("InvalidPathParametersException should allow custom message and responseCode"):
    val customMessage = "Custom error message"
    val customResponseCode = 404

    val exception = InvalidPathParametersException(message = customMessage, responseCode = customResponseCode)

    exception.message shouldEqual customMessage
    exception.responseCode shouldEqual customResponseCode

  test("NinoNotFoundException should allow custom message and responseCode"):
    val customMessage = "Custom NINO not found message"
    val customResponseCode = 404

    val exception = NinoNotFoundException(message = customMessage, responseCode = customResponseCode)

    exception.message shouldEqual customMessage
    exception.responseCode shouldEqual customResponseCode
