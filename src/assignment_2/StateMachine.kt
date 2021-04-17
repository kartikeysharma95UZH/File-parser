package assignment_2

/**
 * Author - Prasun Saurabh
 *
 * Note - I was unable to implement the method to call the program like ./Program-name argument through command line and
 * hence I need to explicitly pass the file name.
 */
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess

class StateMachine {
    fun takeInputAndParse(){
        val br = BufferedReader(InputStreamReader(System.`in`))
        val fileInput = br.readLine()
        readInput(fileInput)
    }

    fun readInput(fileLocation:String){
        val inputStream: InputStream = File(fileLocation).inputStream()
        val lineList = mutableListOf<String>()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { if(!it.startsWith("#")) {
            lineList.add(it)
        } } }
        lineList.removeAll(Arrays.asList("",null))

        createStatesAndTransactions(lineList)
    }

    fun createStatesAndTransactions(list: MutableList<String>){
        /** Flag for parsing state and transition **/
        var flagState = 1
        var startState = ""
        var endState = mutableListOf<String>()
        var mapStateAndDescription:HashMap<String, String> = HashMap()
        var mapCurrentStateAndInputToNextState:HashMap<String, String> = HashMap()
        var mapCurrentStateAndInputToDescription: HashMap<String,String> = HashMap()
        var mapInputToParam:HashMap<String,String> = HashMap()
        /**
         * Parsing the file for states and transitions
         */
        list.forEach lit@{
            if(it.equals("[States]",ignoreCase = false)){
                flagState = 1
                return@lit
            }
            if(it.equals("[Transitions]",ignoreCase = false)){
                flagState = 0
                return@lit
            }
            /**
             * If flag state is 1, parse the States and populate the map (State -> Description)
             */
            if(flagState == 1){
                var state = it.substringBefore(':').substringBefore('#').trim()
                val desr = it.substringAfter(':').substringBefore('#').trim()
                if(state.startsWith("*")){
                    state = state.substringAfter("*")
                    startState = state
                }
                if(state.startsWith("+")){
                    state = state.substringAfter("+")
                    endState.add(state)
                }
                mapStateAndDescription[state] = desr
            }
            /**
             * If flag state is 1, parse the transitions and populate 3 maps
             * Map 1 (I/P+currentState -> description)
             * Map 2 (I/P+currentState -> NextState)
             * Map 3 (Mapping input and corresponding parameter. Just a hack to determine if the parameter is not incorrect).
             */
            if(flagState == 0){
                val transition = it.substringBefore(':').substringBefore('#').trim()
                val description = it.substringAfter(':').substringBefore('#').trim()
                val fromState = transition.substringBefore("(").trim()
                val toState = transition.substringAfter(")").trim()
                val currentInput = transition.substringAfter("(").substringBefore(")").trim()
                val inputParamList = currentInput.split(" ")
                if(fromState=="" || toState == "" || currentInput == ""){
                    println("Invalid configuration file. Please feed valid files")
                    exitProcess(0)
                }
                if (inputParamList.size == 1){
                    if(mapInputToParam.containsKey(inputParamList[0])){
                        var valFromKey = mapInputToParam[inputParamList[0]]
                        valFromKey += "_"+"NA"
                        mapInputToParam[inputParamList[0]] = valFromKey.toString()
                    }
                    else {
                        mapInputToParam[inputParamList[0]] = "NA"
                    }
                }
                else{
                    if(mapInputToParam.containsKey(inputParamList[0])){
                        var valFromKey = mapInputToParam[inputParamList[0]]
                        valFromKey += "_"+inputParamList[1]
                        mapInputToParam[inputParamList[0]] = valFromKey.toString()
                    }
                    else {
                        mapInputToParam[inputParamList[0]] = inputParamList[1]
                    }
                }
                mapCurrentStateAndInputToDescription["$fromState-$currentInput"] = description
                mapCurrentStateAndInputToNextState["$fromState-$currentInput"] = toState
            }



        }
        /**
         * Condition to determine the invalid file while parsing
         */
        if(startState == "" || endState.isEmpty()){
            println("Invalid configuration file. Please feed valid files")
            exitProcess(0)
        }



        var currentState = startState
        while(true){
            println("[$currentState]"+" "+ mapStateAndDescription[currentState])
            if(endState.contains(currentState))
            {
                exitProcess(0)
            }
            val br = BufferedReader(InputStreamReader(System.`in`))
            val usrInput = br.readLine()
            val usrInputList = usrInput.split(" ")
            if(mapInputToParam.containsKey(usrInputList[0])){
                val listOfParameters = mapInputToParam[usrInputList[0]]?.split("_")
                if(usrInputList.size == 1){
                    if (listOfParameters != null) {
                        if(!listOfParameters.contains("NA")){
                            println("! invalid parameters, please try again")
                            continue
                        }
                    }
                }
                else{
                    if (listOfParameters != null) {
                        if(!listOfParameters.contains(usrInputList[1])){
                            println("! invalid parameters, please try again")
                            continue
                        }
                    }

                }
            }
            else{
                println("! invalid input, please try again")
                continue
            }
            val keyForCSAndIpToDesr = "$currentState-$usrInput"

            if(mapCurrentStateAndInputToDescription.containsKey(keyForCSAndIpToDesr))
                println("> "+mapCurrentStateAndInputToDescription[keyForCSAndIpToDesr])
            else{
                println("! invalid input, please try again")
                continue
            }
            currentState = mapCurrentStateAndInputToNextState[keyForCSAndIpToDesr].toString()

        }
    }
}

fun main(){
    val stateMachineInstance = StateMachine()
    stateMachineInstance.takeInputAndParse()
}