# Overview 

The Self-Assessment (**SA**) Liabilities API has been designed to support financial software companies engaged as HMRC third parties. The API contains endpoints for retrieving customer liabilities related to their SA tax account, using a National Insurance number (**NINO**) as a unique identifier. 

## Charges

The amounts returned by the API fall into several categories.

| Charge | Description |
| --- | --- |
| Payable | Due date within the next 30 days. | 
| Pending | Due date more than 30 days in the future. |
| Overdue | Due date in the past. |
| Balance | The sum of all Payable, Pending and Overdue charges. | 

## Benefits

Integrating the API should introduce a more efficient filing and payment journey for third parties, with the customer able to view information and pay within the same space.

