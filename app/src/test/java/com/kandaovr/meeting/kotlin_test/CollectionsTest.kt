package com.kandaovr.meeting.kotlin_test

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionsTest {

    // 辅助测试用的数据类
    data class Person(val name: String, val age: Int, val city: String)

    @Test
    fun run() {
        println("hello")


        // List
        val lists = listOf<Int>(1, 2, 4, 54)

        println(lists.asReversed())
        println(lists.sorted())

        val mList: MutableList<String> = mutableListOf("sd", "sd", "shkshk")
        println(mList)
    }

    /**
     * 测试 filter 和 map
     * filter: 用于根据条件筛选集合中的元素。
     * map: 用于将集合中的每个元素转换成另一种形式。
     *
     * --- 高频陷阱清单 ---
     * 1. **性能陷阱**: 对一个大集合连续调用 `filter` 和 `map` 会创建中间集合，造成额外开销。
     *    - **解决方案**: 对于大型集合，使用 `asSequence()` 将其转换为序列，这样所有操作都会被懒加载 (lazily evaluated)，最后只创建一个新集合。
     *      `largeList.asSequence().filter { ... }.map { ... }.toList()`
     *
     * 2. **顺序陷阱**: `filter` 和 `map` 的顺序很重要。先 `filter` 再 `map` 通常更高效，因为 `map` 操作会在一个更小的集合上执行。
     *    - **高效**: `list.filter { it.isActive }.map { it.name }`
     *    - **低效**: `list.map { it.name }.filter { it.isNotEmpty() }` (如果 name 的计算很昂贵)
     *
     * 3. **可空性陷阱**: 如果 `map` 操作可能返回 `null`，但你又不希望 `null` 出现在最终结果中。
     *    - **解决方案**: 使用 `mapNotNull`，它会自动过滤掉转换结果为 `null` 的元素。
     */
    @Test
    fun listFilterAndMapTest() {
        val people = listOf(
            Person("Alice", 29, "New York"),
            Person("Bob", 31, "London"),
            Person("Charlie", 25, "New York")
        )

        // 陷阱2演示：先 filter 再 map 更高效
        val namesOfPeopleInNewYork = people
            .filter { it.city == "New York" } // 先筛选，集合变小
            .map { it.name }                  // 再转换

        println("Filter/Map: Names of people in New York: $namesOfPeopleInNewYork")
        assertEquals(listOf("Alice", "Charlie"), namesOfPeopleInNewYork)

        // 陷阱3演示：使用 mapNotNull
        val dataWithNulls = listOf("1", "two", "3", "four", "5")
        val numbersOnly = dataWithNulls.mapNotNull { it.toIntOrNull() }
        println("Filter/Map: Parsed numbers with mapNotNull: $numbersOnly")
        assertEquals(listOf(1, 3, 5), numbersOnly)
    }

    /**
     * 测试 find 和 groupBy
     * find: 查找满足条件的第一个元素，如果找不到则返回 null。
     * groupBy: 根据指定的 Key 将集合分组，返回一个 Map。
     */
    @Test
    fun listFindAndGroupTest() {
        val people = listOf(
            Person("Alice", 29, "New York"),
            Person("Bob", 31, "London"),
            Person("Charlie", 25, "New York"),
            Person("David", 31, "London")
        )

        // find: 找到第一个年龄大于30的人
        val firstOver30 = people.find { it.age > 30 }
        println("Find/GroupBy: First person over 30: $firstOver30")
        assertEquals("Bob", firstOver30?.name)

        // groupBy: 按城市分组
        val peopleByCity = people.groupBy { it.city }
        println("Find/GroupBy: People grouped by city: $peopleByCity")
        assertEquals(2, peopleByCity["New York"]?.size)
        assertEquals(2, peopleByCity["London"]?.size)
    }

    /**
     * 测试 Set 的特性
     * Set 中的元素是唯一的，常用于去重。
     * union: 合并两个集合，并自动去重。
     * intersect: 获取两个集合的交集。
     */
    @Test
    fun setOperationsTest() {
        val set1 = setOf(1, 2, 3, 4, 5)
        val set2 = setOf(4, 5, 6, 7, 8)

        // union: 合集，并去重
        val unionSet = set1.union(set2)
        println("Set Ops: Union: $unionSet")
        assertEquals(setOf(1, 2, 3, 4, 5, 6, 7, 8), unionSet)

        // intersect: 交集
        val intersectSet = set1.intersect(set2)
        println("Set Ops: Intersect: $intersectSet")
        assertEquals(setOf(4, 5), intersectSet)

        // subtract: 差集 (set1 中有，但 set2 中没有的)
        val subtractSet = set1.subtract(set2)
        println("Set Ops: Subtract: $subtractSet")
        assertEquals(setOf(1, 2, 3), subtractSet)
    }

    /**
     * 测试 Map 的常用操作
     * Map 用于存储键值对。
     */
    @Test
    fun mapOperationsTest() {
        val cityPopulation = mutableMapOf("New York" to 8_400_000, "London" to 8_900_000, "Tokyo" to 13_900_000)

        // 获取和修改值
        cityPopulation["London"] = 9_000_000
        println("Map Ops: Updated London population: ${cityPopulation["London"]}")
        assertEquals(9_000_000, cityPopulation["London"])

        // 遍历
        for ((city, population) in cityPopulation) {
            println("Map Ops: $city has a population of $population")
        }

        // 使用 mapValues 转换值
        val cityPopulationInMillions = cityPopulation.mapValues { (_, v) -> "${v / 1_000_000}M" }
        println("Map Ops: Population in millions: $cityPopulationInMillions")
        assertEquals("9M", cityPopulationInMillions["London"])
    }

    /**
     * 测试 flatMap 和 flatten
     * flatten: 将一个包含集合的集合（二维集合）“压平”成一个一维集合。
     * flatMap: 对集合中的每个元素执行转换操作（通常是返回一个集合），然后将所有返回的集合“压平”并合并成一个新集合。
     *
     * --- 高频陷阱清单 ---
     * 1. **混淆陷阱**: `flatMap` 等价于先 `map` 再 `flatten`，理解这一点是关键。
     *    `listOfLists.flatMap { it }` 等价于 `listOfLists.flatten()`
     *
     * 2. **空集合陷阱**: 如果 `flatMap` 的 lambda 表达式为某个元素返回了一个空集合，那么这个元素在最终结果中就“消失”了。这既是特性也是陷阱，取决于你是否预料到它。
     *    `listOf("a", "", "b").flatMap { if (it.isNotEmpty()) listOf(it, it) else emptyList() }` // 结果是 [a, a, b, b]
     *
     * 3. **性能陷阱**: 和 `map` 一样，在大型集合上使用 `flatMap` 也会创建大量中间对象。对于性能敏感的场景，记得使用 `asSequence()`。
     *
     * 4. **类型转换陷阱**: `flatMap` 后的集合类型可能与你预期的不同，特别是当处理异构集合时，需要仔细确认返回的集合类型。
     */
    @Test
    fun flatMapAndFlattenTest() {
        // 1. flatten: 将二维集合压平成一维
        val listOfLists = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
        val flattenedList = listOfLists.flatten()
        println("flatten: Original: $listOfLists, Flattened: $flattenedList")
        assertEquals(listOf(1, 2, 3, 4, 5), flattenedList)

        // 2. flatMap: map + flatten 的组合
        val words = listOf("Hello", "World")
        val flatMappedChars = words.flatMap { it.toList() } // toList() 将字符串转换为 List<Char>
        println("flatMap: Original: $words, FlatMapped to chars: $flatMappedChars")
        assertEquals(listOf('H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'), flatMappedChars)

        println("\n--- 陷阱演示 ---")

        // 陷阱1 (混淆): flatMap { it } 等同于 flatten()
        val flatMappedIdentity = listOfLists.flatMap { it }
        println("Trap 1 (Confusion): flatten() result: $flattenedList")
        println("Trap 1 (Confusion): flatMap { it } result: $flatMappedIdentity")
        assertEquals(flattenedList, flatMappedIdentity)

        // 陷阱2 (空集合): 返回 emptyList() 的元素会从最终结果中“消失”
        val numbers = listOf(1, 2, 3, 4)
        val oddNumbersMultiplied = numbers.flatMap {
            if (it % 2 != 0) listOf(it * 10, it * 100) // 奇数返回一个包含两个元素的列表
            else emptyList() // 偶数返回空列表
        }
        println("Trap 2 (Empty List): Original: $numbers, FlatMapped odd numbers: $oddNumbersMultiplied")
        assertEquals(listOf(10, 100, 30, 300), oddNumbersMultiplied)

        // 陷阱4 (类型转换): 将对象列表转换为其内部的字符串列表
        data class Author(val name: String, val books: List<String>)
        val authors = listOf(
            Author("Author A", listOf("Book 1", "Book 2")),
            Author("Author B", listOf("Book 3", "Book 4", "Book 5"))
        )
        val allBooks = authors.flatMap { it.books }
        println("Trap 4 (Type Conversion): From List<Author> to a single List<String> of all books: $allBooks")
        assertEquals(listOf("Book 1", "Book 2", "Book 3", "Book 4", "Book 5"), allBooks)
    }
}