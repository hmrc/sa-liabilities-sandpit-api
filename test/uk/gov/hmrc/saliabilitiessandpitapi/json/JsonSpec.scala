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

package uk.gov.hmrc.saliabilitiessandpitapi.json

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.*

class JsonSpec extends AnyFunSuite, Matchers:

  test("bigDecimalBasedWrites should correctly convert T to BigDecimal") {
    case class Foo(value: Int)
    val exampleWrites: Writes[Foo] = bigDecimalBasedWrites(_.value)

    val result = Json.toJson(Foo(123))(exampleWrites).as[BigDecimal]

    result shouldEqual BigDecimal(123)
  }

  test("bigDecimalBasedReads should correctly read BigDecimal from JsValue") {
    val bigDecimalReads: Reads[BigDecimal] = bigDecimalBasedReads
    val json                               = Json.toJson(BigDecimal(123.45))

    val result = json.validate[BigDecimal](bigDecimalReads).getOrElse(BigDecimal(0))

    result shouldEqual BigDecimal(123.45)
  }
