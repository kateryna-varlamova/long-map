# long-map

Finish development of class LongMapImpl, which implements a map with keys of type long. It has to be a hash table (like HashMap). Requirements:
* it should not use any known Map implementations; 
* it should use as less memory as possible and have adequate performance;
* the main aim is to see your codestyle and teststyle 

**Current implementation has the following complexity:**

| Method          | Complexity by memory | Complexity by time     |
|-----------------|----------------------|------------------------|
| put()           | O(1)                 | Best: O(1) Worst: O(N) |
| get()           | O(1)                 | B: O(1) W: O(N)        |
| remove()        | O(1)                 | B: O(1) W: O(N)        |
| isEmpty()       | O(1)                 | O(1)                   |
| containsKey()   | O(1)                 | B: O(1) W: O(N)        |
| containsValue() | O(1)                 | O(N)                   |
| keys()          | O(1)                 | O(N)                   |
| values()        | O(1)                 | O(N)                   |
| size()          | O(1)                 | O(1)                   |
| clear()         | O(1)                 | O(N)                   |

Taking into account that requirement was to use as less memory as possible, all operations has O(1) by memory.

To run JaCoCo code coverage report use command:
`mvn test jacoco:report`
