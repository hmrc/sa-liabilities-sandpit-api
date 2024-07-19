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

package uk.gov.hmrc.saliabilitiessandpitapi

import play.api.libs.json.{Reads, Writes}
import uk.gov.hmrc.saliabilitiessandpitapi.json.{StringBasedJsonOps, bigDecimalBasedReads, bigDecimalBasedWrites}

package object models:

  opaque type PendingDueDate   = String
  opaque type PayableDueDate   = String
  opaque type TotalBalance     = BigDecimal
  opaque type PayableAmount    = BigDecimal
  opaque type PendingDueAmount = BigDecimal
  opaque type OverdueAmount    = BigDecimal

  object PendingDueDate extends StringBasedJsonOps[PendingDueDate]:
    def apply(value: String): PendingDueDate            = value
    def valueOf(pendingDueDate: PendingDueDate): String = pendingDueDate

  object PayableDueDate extends StringBasedJsonOps[PayableDueDate]:
    def apply(value: String): PayableDueDate            = value
    def valueOf(payableDueDate: PayableDueDate): String = payableDueDate

  object TotalBalance:
    def apply(value: BigDecimal): TotalBalance = value
    given Writes[TotalBalance]                 = bigDecimalBasedWrites(apply)
    given Reads[TotalBalance]                  = bigDecimalBasedReads

  object PayableAmount:
    def apply(value: BigDecimal): PayableAmount = value
    given Writes[PayableAmount]                 = bigDecimalBasedWrites(apply)
    given Reads[PayableAmount]                  = bigDecimalBasedReads

  object PendingDueAmount:
    def apply(value: BigDecimal): PendingDueAmount = value
    given Writes[PendingDueAmount]                 = bigDecimalBasedWrites(apply)
    given Reads[PendingDueAmount]                  = bigDecimalBasedReads

  object OverdueAmount:
    def apply(value: BigDecimal): OverdueAmount = value
    given Writes[OverdueAmount]                 = bigDecimalBasedWrites(apply)
    given Reads[OverdueAmount]                  = bigDecimalBasedReads
