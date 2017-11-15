# StatisticsService

A scalable service that provides statistics for transactions. It takes `timestamp` and `amount` of transaction as json payload eg:
```
POST /transactions
{
"amount": 3.5,
"timestamp": 12890212
}
```

Statistics for the configured interval should be given by
```
GET /statistics
```
Output:
```
{
	sum: 9 //- Total sum of transaction value.
	avg: 4 //- Average amount of transaction value
	max: 5 //- Maximum transaction value
	min: 2 //- Minimum transaction value
	count: 5 // Total number of transactions
}
```

Requirements:
- should scale
- should be threadsafe
- should be reliable over a longer period of time.
- No database (including in-memory databases). 
- The endpoints have to execute in constant time and memory (O(1))

___

Run tests with `./gradlew test`
Run with `./gradlew bootRun`
___
