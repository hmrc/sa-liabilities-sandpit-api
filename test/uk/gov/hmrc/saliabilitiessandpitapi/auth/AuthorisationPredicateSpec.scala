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

package uk.gov.hmrc.saliabilitiessandpitapi.auth

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.mvc.{AnyContentAsEmpty, Request}
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.{Enrolment, Nino}

class AuthorisationPredicateSpec extends AnyWordSpec, Matchers, AuthorisationPredicate, NinoFromPathExtractor:

  "AuthorisationPredicate" should {

    "generate a predicate with a valid NINO" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/QQ123456A")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, Some("QQ123456A")))
    }

    "generate a predicate with no NINO when not present in the path" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/some/other/path")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, None))
    }

    "generate a predicate with None NINO when the NINO is invalid" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/INVALID")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, None))
    }

    "handle a POST request with a valid NINO in the path" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("POST", "/nino/QQ123456A")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, Some("QQ123456A")))
    }

    "generate a predicate correctly when request path contains query parameters" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/QQ123456A?param=value")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, Some("QQ123456A")))
    }

    "generate a predicate with None NINO when NINO is in the path but does not match the expected pattern" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/ABC123456")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, None))
    }

    "handle request paths with NINO at the end" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/some/path/nino/QQ123456A")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, Some("QQ123456A")))
    }

    "generate a predicate with None NINO when the path is empty" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, None))
    }

    "generate a predicate with None NINO when the path contains only special characters" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/@@@/!!!/###")
      val predicateResult                          = predicate(request)

      predicateResult shouldBe (Enrolment("IR-SA") and Nino(hasNino = true, None))
    }
  }
