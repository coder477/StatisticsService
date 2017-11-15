# StatisticsService ![https://travis-ci.org/raj-saxena/StatisticsService](https://travis-ci.org/raj-saxena/StatisticsService.svg?branch=master)

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


___

## Scribbling thoughts on approach:
Need a collection that returns data of every 60 sec.
 - threadsafe, locks maybe?
 - updated on every upload
 - sum, avg, max, min, count


 * Need to maintain a data structure(cache) that maintains a sliding window of 60 sec.
 * Need to come up with right add/remove and stats calculation strategy.

### When to update (add/remove/calculate)?
* On every upload?
	- Adv: If uploads are less frequent, less writes to the cache.
	- Dis: 
		- If uploads are frequent, multiple uploads in a second could cause unnecessary cycles of computation that can be batched every second.
		- Gets might read stale data if there wasn't any update in last 60 sec.
* During get
	- Adv: Computation only done when needed.
	- Dis: 
		- Collection might keep growing in absence of Gets.
		- Multiple gets in the same second will cause unnecessary recomputation.
* Schedule update every second
	- Unnecessary calculations in absence of real update. (Dirty flags, maybe..)
* A way in between:
	- A Map with 60 buckets to hold transactions within that second, kind of like a time based sliding window.
		- Add/remove in cache on POST.
		- calculate on GET
	- Guava cache for UploadInfo
    	- how it works - https://stackoverflow.com/questions/10144194/how-does-guava-expire-entries-in-its-cachebuilder
    	- Thread safety - https://stackoverflow.com/questions/11124856/using-guava-for-high-performance-thread-safe-caching#11125407
    - [x] A `ConcurrentNavigableMap` with list of transactions mapped to second wise keys.
    	- Clear expired entries from map after every x seconds
