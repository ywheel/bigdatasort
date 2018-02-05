## 0. 题

现在有1到1亿，这1亿个乱序正整数，给出时间复杂度最小的排序算法java代码，提交源码和实验结果时间。

考虑内存无限，和内存100M两种情况

## 1. 读题

先计算一下这1亿个数字的大小。int类型占用4字节，最大值为2,147,483,648, 因此可以存储最大值为1亿的正整数。
那么存储这一亿个数字的数组需要占用的内存为4亿，即381.5MB。

1. 数据产生：需要产生1到1亿，这1亿个正整数需要是乱序的，在这里理解为，生成的数据输出到一个文件中了，在此定义为一行一个数字。这里有一个问题，是否也需要考虑内存无限和内存100M的情况？按题目中的意思，需要考察的是排序算法在内存有限和无限的条件下的时间复杂度，因此，对于产生数据来说，先认为内存无限
2. 在内存无限的条件下，这1亿个正整数可以完全加载在内存中，因此考虑不需要借助外部存储的方式进行排序，数据全部加在到内存中后进行排序，那就需要区分几种排序算法，哪一种时间复杂度更低
3. 在内存只有100M的条件下，则只能借助外部存储进行

## 2. 解题

### 2.0 数据产生

在这里不限制内存使用，因此可考虑先生成一个一亿长度的数组，然后再搞乱它，搞乱的方式，是随机交换两个位置的数字。

具体的代码见：src/main/java/com/ywheel/bigdata/sort/prepare/DataGenerator.java

### 2.1 无限内存

考虑使用如下几种算法：
1. 冒泡排序：平均时间复杂度 O(n^2)
2. 插入排序：平均时间复杂度 O(n^2)
3. 合并排序：平均时间复杂度 O(n*(log n))
4. 快速排序：平均时间复杂度 O(n*(log n))
5. 选择排序：平均时间复杂度 O(n^2)

各种排序算法代码见： src/main/java/com/ywheel/bigdata/sort/algorithm包内。

### 2.2 100M内存

在只有100M内存的限制时，上诉算法均无法满足，光是读取整个数组，就得用381.5MB内存。
因此需要考虑类似mapreduce的方式，借助外部存储(如文件），将其先拆分成多个小文件，对每一个文件进行内存内排序后再输出为有序的文件，最后再将这些文件合并为一个全局有序的文件。

对于多个有序文件合并为一个全局有序文件，有两个解法：
1. 在拆分文件时，按照文件的行顺序，切分成多个小文件。因此，各个文件内容之间可以认为是独立无序的。对每个小文件单独排序后，合并时仍需要考虑排序问题。可使用堆来实现多个文件的合并排序，重建堆的时间复杂度为O(log n), 每次选择输出最小的数字后，再读取该数字对应的文件的下一行，并重建堆。
2. 在本题中，已知了数字的范围，是[1, 1亿]， 因此，在拆分文件时，可按照数字的取值进行切分。因此，拆分后各小文件之间是有序的，只需要完成对每个小文件内部的排序后，即实现了全局有序。各小文件只需要按照顺序合并在一起即可。

一亿个数字占381.5MB，那么可简单的分为10个文件，每个文件2500万个数字，在内存中占用38.15MB，完全可以使用2.1中的排序算法实现（如快排）。

两种方案的代码见： src/main/java/com/ywheel/bigdata/sort/TestSortWithLimitedMemory.java中的method_1和method_2。

## 3. 测试

### 3.1 测试不同数据量时排序算法耗时

1). 1000个数字

```
Total use 6(ms) to generate 1000 random number.
BubbleSort: Total use 10(ms) to sort 1000 random number.
InsertSort: Total use 4(ms) to sort 1000 random number.
MergeSort: Total use 1(ms) to sort 1000 random number.
QuickSort: Total use 1(ms) to sort 1000 random number.
SelectSort: Total use 8(ms) to sort 1000 random number.
```

2). 1w个数字

```
Total use 35(ms) to generate 10000 random number.
BubbleSort: Total use 487(ms) to sort 10000 random number.
InsertSort: Total use 66(ms) to sort 10000 random number.
MergeSort: Total use 6(ms) to sort 10000 random number.
QuickSort: Total use 8(ms) to sort 10000 random number.
SelectSort: Total use 163(ms) to sort 10000 random number.
```

3). 10w个数字

```
Total use 83(ms) to generate 100000 random number.
BubbleSort: Total use 24603(ms) to sort 100000 random number.
InsertSort: Total use 1258(ms) to sort 100000 random number.
MergeSort: Total use 33(ms) to sort 100000 random number.
QuickSort: Total use 42(ms) to sort 100000 random number.
SelectSort: Total use 5745(ms) to sort 100000 random number.
```

4). 100w个数字

此时其他排序方式已经很慢了，因此只保留了合并排序和快速排序。

```
Total use 368(ms) to generate 1000000 random number.
MergeSort: Total use 225(ms) to sort 1000000 random number.
QuickSort: Total use 260(ms) to sort 1000000 random number.
```

5) 1000w个数字

此时快速排序的运行时间比合并排序小了。

```
Total use 2359(ms) to generate 10000000 random number.
MergeSort: Total use 1857(ms) to sort 10000000 random number.
QuickSort: Total use 997(ms) to sort 10000000 random number.
```

6) 一亿个数字

```
Total use 24153(ms) to generate 100000000 random number.
MergeSort: Total use 25358(ms) to sort 100000000 random number.
QuickSort: Total use 12377(ms) to sort 100000000 random number.
```

### 3.2 测试内存限制

#### 3.2.1 无限内存

在这里设定2G内存，测试如下：

```
java -Xmx2048m -Xms2048m -classpath bigdata.sort-1.0-SNAPSHOT.jar com.ywheel.bigdata.sort.TestSortInMemory
Total use 21478(ms) to generate 100000000 random number.
MergeSort: Total use 23244(ms) to sort 100000000 random number.
QuickSort: Total use 12177(ms) to sort 100000000 random number.
```

#### 3.2.2 100M内存

完全使用内存来排序就已经不行了，在创建数组的阶段，就抛出了OOM：

```
java -Xmx100m -Xms100m -classpath bigdata.sort-1.0-SNAPSHOT.jar com.ywheel.bigdata.sort.TestSortInMemory
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at com.ywheel.bigdata.sort.prepare.DataGenerator.generateData(DataGenerator.java:41)
	at com.ywheel.bigdata.sort.prepare.DataGenerator.generateAndOutput(DataGenerator.java:30)
	at com.ywheel.bigdata.sort.TestSortInMemory.main(TestSortInMemory.java:42)
```

换成使用外部存储的排序方式，运行如下：

```
java -Xmx100m -Xms100m -classpath bigdata.sort-1.0-SNAPSHOT.jar com.ywheel.bigdata.sort.TestSortWithLimitedMemory
Method 1: Total use 78400(ms) to sort lots of random numbers.
Method 2: Total use 55868(ms) to sort lots of random numbers.
```

每个小文件中包含2500万个数字，按照之前的测试，选择最快的快速排序。方法2（即按照数字取值区间进行切分文件的方式）是最快的。