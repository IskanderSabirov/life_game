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
}
