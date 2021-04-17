package assignment_2
/**
 * Author - Prasun Saurabh
 *
 *  * Note - I was unable to implement the method to call the program like ./Program-name argument through command line and
 * hence I implemented one trick like program is running but when I pass the argument in the form of
 * ./lottery -n <numbers> -c <numbers>
 *
 * IMPORTANT
 * Sample input -> "./lottery -n 560 -c 8", "./lottery --numbers 600 -c 4"
 *
 */
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.pow

class Lottery{
    fun input(){
        val br = BufferedReader(InputStreamReader(System.`in`))
        var input: String? = null
        try {
            var argNumber = 0
            var argChoice = 0
            var argPot = 1000000
            var programName = ""
            input = br.readLine()
            val list = input.split(" ")
            if(list.size%2==0){
                throw IOException("Invalid number of arguments")
            }
            val index = 0
            if(list[index].startsWith("./")){
                programName = input.substringAfter("./").substringBefore(" ")
            }
            for (index in 1 until list.size step 2){
                if(list[index].startsWith("--numbers") || list[index].startsWith("-n")){
                    argNumber = list[index+1].toInt()
                }
                if(list[index].startsWith("--choices") || list[index].startsWith("-c")){
                    argChoice = list[index+1].toInt()
                }
                if(list[index].startsWith("--pot")){
                    argPot = list[index+1].toInt()
                }
            }
//            println("programName -> $programName , argNumber -> $argNumber, argChoice -> $argChoice , argPot -> $argPot")

            if(!programName.equals("lottery",ignoreCase = true)){
                throw IOException("No Such program name")
            }
            if(argChoice <= 0 || argChoice <= 0 || argChoice>argNumber){
                throw IOException("Invalid arguments")
            }

            println("Enter $argChoice numbers separated by any number of spaces")
            var listOfChoices = mutableListOf<Int>()
            var inputFromUser: List<String>
            while (true){
                var numFlag = 0
                inputFromUser = br.readLine().split(" ")
                if (inputFromUser.size>argChoice){
                    println("Invalid Input")
                    continue
                }
                val inputFromUserSet = inputFromUser.toHashSet()
                for (inp in inputFromUser){
                    if(inp.toInt()>argNumber){
                        println("Invalid Input")
                        numFlag = 1
                        break
                    }
                }

                if (numFlag == 1)
                    continue
                if(inputFromUserSet.size == argChoice){
                    break
                }
                else{
                    println("Invalid Input")
                }
            }

            for(tempInput in inputFromUser){
                listOfChoices.add(tempInput.toInt())
            }
            
            println("Shuffling...")
//            var listOfDrawnNumbers = mutableListOf<Int>()
            Thread.sleep(5000)
            val listOfDrawnNumbers = List(argChoice) { kotlin.random.Random.nextInt(1,argNumber) }
            var countOfCorrectGuess = 0
            for(drawnNumber in listOfDrawnNumbers){
                print(" $drawnNumber")
                if(listOfChoices.contains(drawnNumber))
                {
                    countOfCorrectGuess++
                }
                Thread.sleep(2000)
            }
            println(0)
            val prize = argPot.toDouble().pow(((countOfCorrectGuess / argChoice).toDouble()))
            println("You got $countOfCorrectGuess out of $argChoice correct guesses and win $prize beer tokens!")

        } catch (e: IOException) {
            print(e)
        }

    }
}

fun main(){
    val lottery = Lottery()
    lottery.input()
}