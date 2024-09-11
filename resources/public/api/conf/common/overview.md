# Overview 

The Self-Assessment (**SA**) Liabilities Payment Details API has been designed to support financial software companies engaged as HMRC third parties. The API contains endpoints for retrieving customer payment charges related to their SA tax account, using a National Insurance number (**NINO**) as a unique identifier. 

## Charges

The charges returned by the API fall into several categories.

| Charge | Description |
| --- | --- |
| Payable | Due date is within the next 30 days. | 
| Pending | Due date is more than 30 days in the future. |
| Overdue | Due date has been breached and is in the past. |
| Balance | The sum of all Payable, Pending and Overdue charges. | 

## Benefits

Third parties who integrate with the API should benefit from several improvements.

* Reducing the number of unallocated or incorrectly allocated payments, freeing up time and resource currently used to rectify and process misallocations. 
* A more efficient filing and payment journey, with the customer able to view information and pay within the same space.

