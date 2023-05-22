import kotlin.test.*

internal class Tests {

    @Test
    fun testPalindrome() {
        assert(isPalindrome("1232423"))

        assert(isPalindrome(""))

        assert(isPalindrome("a234a234a"))

        assert(isPalindrome("/4=23-a234234a"))

        assert(isPalindrome("a"))

        assert(isPalindrome("1"))

        assert(isPalindrome("132479328g"))

        assertFalse(isPalindrome("ab"))

        assertFalse(isPalindrome("abcd"))

        assertFalse(isPalindrome("---a///23/234b./"))
    }

    @Test
    fun testIteratorOfIterators() {
        val list1 = listOf(1,2,3,4,5,6,7,8,9)
        val list2 = listOf("a", "b", "c")
        val list3 = listOf(true, false, true)

        val iterators = listOf(list1.iterator(), list2.iterator(), list3.iterator())

        val iteratorOfIterators = IteratorOfIterators(iterators)
        while (iteratorOfIterators.hasNext()) {
            println(iteratorOfIterators.next())
        }
        assertFalse(iteratorOfIterators.hasNext())
    }
}
