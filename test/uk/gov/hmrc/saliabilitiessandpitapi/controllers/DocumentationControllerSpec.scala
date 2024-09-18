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

package uk.gov.hmrc.saliabilitiessandpitapi.controllers

import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Span}
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.NOT_FOUND
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{OK, route, writeableOf_AnyContentAsEmpty}

import scala.concurrent.Future

class DocumentationControllerSpec extends AnyWordSpecLike, GuiceOneAppPerSuite, Matchers, ScalaFutures:

  "API Documentation Endpoint" should {
    "return OK when requesting definition" in {
      val url: String = "/api/definition"

      val result: Option[Future[Result]] = route(app, FakeRequest("GET", url))
      val timeout: Timeout               = Timeout(Span(500, Millis))

      result match
        case None                               => fail(s"Route for $url did not match")
        case Some(futureResult: Future[Result]) => whenReady(futureResult, timeout)(_.header.status shouldBe OK)
    }

    "return OK when requesting specification" in {
      val url: String = s"/api/conf/1.0/application.yaml"

      val result: Option[Future[Result]] = route(app, FakeRequest("GET", url))

      result match
        case None                               => fail(s"Route for $url did not match")
        case Some(futureResult: Future[Result]) => whenReady(futureResult)(_.header.status shouldBe OK)
    }

    "return NOT_FOUND for invalid documentation endpoint" in {
      val url: String = s"/api/invalid-endpoint"

      val result: Option[Future[Result]] = route(app, FakeRequest("GET", url))

      result match
        case None                               => fail(s"Route for $url did not match")
        case Some(futureResult: Future[Result]) => whenReady(futureResult)(_.header.status shouldBe NOT_FOUND)
    }

    "return NOT_FOUND for missing specification file" in {
      val url: String = s"/api/conf/1.0/nonexistent.yaml"

      val result: Option[Future[Result]] = route(app, FakeRequest("GET", url))

      result match
        case None                               => fail(s"Route for $url did not match")
        case Some(futureResult: Future[Result]) => whenReady(futureResult)(_.header.status shouldBe NOT_FOUND)
    }

    "handle unsupported HTTP methods" in {
      val url: String = "/api/definition"

      val result: Option[Future[Result]] = route(app, FakeRequest("POST", url))

      result match
        case None                               => fail(s"Route for $url did not match")
        case Some(futureResult: Future[Result]) => whenReady(futureResult)(_.header.status shouldBe NOT_FOUND)
    }
  }
