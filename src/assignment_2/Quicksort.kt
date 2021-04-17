package assignment_2

class Quicksort<T>{

    fun <T: Comparable<T>> sort(list: MutableList<T>,low: Int, high: Int) {
        var pivot: Int
        if(low<high){
            pivot = partition(list, low, high)
            sort(list, low, pivot-1)
            sort(list, pivot+1, high)
        }


    }

    private fun <T: Comparable<T>> partition(list: MutableList<T>, low: Int, high: Int): Int {
        val x = list[high]
        var i = low-1
        for(index in low until high){
            if(list[index].compareTo(x)<0){
                i++
                val temp = list[index]
                list[index] = list[i]
                list[i] = temp
            }
        }

        val tmp = list[high]
        list[high] =  list[i+1]
        list[i+1] = tmp
        return i+1


    }

}

fun main(){
    /**
     * Quicksort for Double
     */
    var quicksortDouble = Quicksort<Double>()
    val listOfDouble = mutableListOf(1.5,7.3,6.5,1.5,3.0,5.2,4.0)
    quicksortDouble.sort(listOfDouble,0,listOfDouble.size-1)
    println(listOfDouble)

    /**
     * Quicksort for Integer
     */

    var quickSortInt = Quicksort<Int>()
    val listOfInt = mutableListOf(2,7,6,1,3,5,4)
    quickSortInt.sort(listOfInt,0,listOfInt.size-1)
    println(listOfInt)

    /**
     * Quicksort for Character
     */

    var quickSortChar = Quicksort<Char>()
    val listOfChar = mutableListOf('a','z','h','c','e')
    quickSortChar.sort(listOfChar,0,listOfChar.size-1)
    println(listOfChar)

    /**
     * Quicksort for String
     */
    var quickSortString = Quicksort<String>()
    val listOfString = mutableListOf<String>("abc","zxc","cat","dog")
    quickSortString.sort(listOfString,0,listOfString.size-1)
    println(listOfString)

}