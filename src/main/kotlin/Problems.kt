fun isPalindrome(s: String): Boolean {
    var i = 0
    var j = s.length - 1
    while (i < j) {
        while (i < s.length - 1 && !s[i].isLetter())
            ++i
        while (j > 0 && !s[j].isLetter())
            --j
        if (i < j && s[i] != s[j])
            return false
        ++i
        --j
    }
    return true
}


fun createClassToFindRobots(){
    //TODO: делал реализацию данного класса на практике у доски (класс должен считать кол-во роботов и возвращать их кол-во в моменте
}
