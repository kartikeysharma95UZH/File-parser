---hhiihiii haaa haaa---
/****
    Task 2 - Fizz Buzz Use case
    Author - Prasun Saurabh
 ****/

import java.util.Scanner
import kotlin.system.exitProcess

class Task2 {
    fun processing(){


        println("Please enter four positive integer\n" +
                "numbers (m n x y) separated by one or more blank spaces or type quit.")

        while(true) {
            val reader = Scanner(System.`in`)
            var intList: MutableList<Int> = mutableListOf()
            for (i in 0..3) {
                var value = reader.next()
                if (value.equals("quit", ignoreCase = true)) {
                    exitProcess(0)
                }

                if (value.toIntOrNull() == null) {
                    println("Invalid input, please try again")
                    break
                } else {
                    intList.add(value.toInt())
                }

            }

            if(intList.size==4){
                if (intList[0] >= intList[1] || intList[2] == intList[3]) {
                    println("Invalid input, please try again")
                    continue
                }

                val range1 = intList[0]
                val range2 = intList[1]
                val fizzVal = intList[2]
                val buzzVal = intList[3]

                for (range in range1..range2) {

                    if (range % fizzVal == 0 && range % buzzVal == 0) {
                        print("FizzBuzz ")
                    } else if (range % fizzVal == 0) {
                        print("Fizz ")
                    } else if (range % buzzVal == 0) {
                        print("Buzz ")
                    } else {
                        print("$range ")
                    }
                }
                println()
            }
            else{
                continue
            }
        }

    }
}

fun main(){

    val task2 = Task2()
    task2.processing();

}