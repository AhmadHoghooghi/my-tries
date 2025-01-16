# Ideas while reading RDD Programming Guide
[link](https://spark.apache.org/docs/3.5.4/rdd-programming-guide.html)

## Read from a text file and do WC
- [x] done

Note
: RDDs support two types of operations:

- `transformations`, which create a new dataset from an existing one, and 
- `actions`, which return a value to the driver program after running a computation on the dataset.

# effect of cache or persist

`lineLengths.persist(StorageLevel.MEMORY_ONLY());`