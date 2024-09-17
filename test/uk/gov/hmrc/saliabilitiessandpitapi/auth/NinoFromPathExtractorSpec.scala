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

class NinoFromPathExtractorSpec extends AnyWordSpec, Matchers, NinoFromPathExtractor:

  "NinoFromPathExtractor" should {

    "extract a valid NINO from the request path" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/QQ123456A")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe Some("QQ123456A")
    }

    "extract a NINO even if the request method is different" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("POST", "/nino/QQ123456A")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe Some("QQ123456A")
    }

    "return None when the request path contains an invalid NINO" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/INVALID")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }

    "return None when the request path does not contain a NINO" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/some/other/path")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }

    "return None when the path is correct but the NINO is missing" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/foo/bar/nino/")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }

    "return none when NINO is nested within the path" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/foo/bar/nino/QQ123456A/baz")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }

    "return None when valid NINO is nested within text" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/nino/abcQQ123456Axyz")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }

    "return None when the path contains similar segments but no NINO" in {
      val request: Request[AnyContentAsEmpty.type] = FakeRequest("GET", "/foo/bar/nninoo/QQ123456A")
      val nino: Option[String]                     = extractNinoFromRequest(request)

      nino shouldBe None
    }
  }
