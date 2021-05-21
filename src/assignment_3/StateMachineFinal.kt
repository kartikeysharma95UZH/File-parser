package assignment_3

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

class StateMachineFinal {
    /**
     * Creating the variables
     */
    private val transitionList = mutableListOf<String>()
    private var startState = ""
    private var endStateList = mutableListOf<String>()
    private var mapStateAndDescription:HashMap<String, String> = HashMap()
    private var mapCurrentStateAndInputToNextState:HashMap<String, String> = HashMap()
    private var mapCurrentStateAndInputToDescription: HashMap<String,String> = HashMap()
    private var mapInputToParam:HashMap<String,String> = HashMap()

    fun takeFileInput(){
        val br = BufferedReader(InputStreamReader(System.`in`))
        val fileInput = br.readLine()
        readInput(fileInput)
    }

    fun readInput(fileInput:String){
        val inputStream: InputStream = File(fileInput).inputStream()
        /**
         * Detecting if the input by the user is a valid file or not.
         */
        if(fileInput.endsWith("gfl")){
            parseGFLFileAndCreateState(inputStream)
            createStatesAndTransactions(transitionList)
            implementStateMachine(startState,endStateList,mapStateAndDescription,mapCurrentStateAndInputToNextState,mapInputToParam,mapCurrentStateAndInputToDescription)
        }
        else if(fileInput.endsWith("machine")){
            parseMachineFileAndCreateState(inputStream)
            createStatesAndTransactions(transitionList)
            implementStateMachine(startState,endStateList,mapStateAndDescription,mapCurrentStateAndInputToNextState,mapInputToParam,mapCurrentStateAndInputToDescription)
        }
        else{
            println("Sorry! We are currently not processing this type of files")
            exitProcess(0)
        }


    }

    fun parseGFLFileAndCreateState(inputStream: InputStream){
        val tempFigureList = mutableListOf<String>()
        var stateKey = ""
        var parseFigureFlag = 0
        inputStream.bufferedReader().useLines { lines -> lines.forEach { if(!it.startsWith("#")) {
            if(it.startsWith("@")){
                if(parseFigureFlag == 1){
                    mapStateAndDescription[stateKey] = tempFigureList.joinToString("\n")
                    parseFigureFlag = 0
                }
                if(it.startsWith("@*")){
                    stateKey = it.substringAfter("*").substringBefore(":").substringBefore("#").trim()
                    startState = stateKey
                }
                else if(it.startsWith("@+")){
                    stateKey = it.substringAfter("+").substringBefore(":").substringBefore("#").trim()
                    endStateList.add(stateKey)
                }
                else{
                    stateKey = it.substringAfter("@").substringBefore(":").substringBefore("#").trim()
                }
                parseFigureFlag = 1
            }
            if(it.startsWith(">")){
                if(parseFigureFlag == 1){
                    mapStateAndDescription[stateKey] = tempFigureList.joinToString("\n")
                    tempFigureList.clear()
                    stateKey = ""
                    parseFigureFlag = 0
                }
                transitionList.add(it.substring(1))
            }
            if(it.startsWith(" ") && parseFigureFlag == 1){
                tempFigureList.add(it)
            }

        } } }
        mapStateAndDescription[stateKey] = tempFigureList.joinToString("\n")
        tempFigureList.clear()
        parseFigureFlag = 0
        transitionList.removeAll(Arrays.asList("",null))
        /**
         * Condition to determine the invalid file while parsing
         */
        if(startState == "" || endStateList.isEmpty()){
            println("Invalid configuration file. Please feed valid files")
            exitProcess(0)
        }
    }

    fun parseMachineFileAndCreateState(inputStream: InputStream) {
        var flagState = -1
        val lineList = mutableListOf<String>()
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach {
                if (!it.startsWith("#")) {
                    lineList.add(it)
                }
            }
        }
        lineList.removeAll(Arrays.asList("", null))

        /**
         * Using lit@ since continue is not allowed in the forEach loop
         */
        lineList.forEach lit@{
            if (it.equals("[States]", ignoreCase = false)) {
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
            if (flagState == 1) {
                var state = it.substringBefore(':').substringBefore('#').trim()
                val desr = it.substringAfter(':').substringBefore('#').trim()
                if (state.startsWith("*")) {
                    state = state.substringAfter("*")
                    startState = state
                }
                if (state.startsWith("+")) {
                    state = state.substringAfter("+")
                    endStateList.add(state)
                }
                mapStateAndDescription[state] = desr
            }
            else if(flagState == 0){
                transitionList.add(it)
            }


        }

        /**
         * Condition to determine the invalid file while parsing
         */
        if(startState == "" || endStateList.isEmpty()){
            println("Invalid configuration file. Please feed valid files")
            exitProcess(0)
        }
    }

    fun createStatesAndTransactions(list: MutableList<String>){
        list.forEach{
            /**
             * If flag state is 1, parse the transitions and populate 3 maps
             * Map 1 (I/P+currentState -> description)
             * Map 2 (I/P+currentState -> NextState)
             * Map 3 (Mapping input and corresponding parameter. Just a hack to determine if the parameter is not incorrect).
             */
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
                var combinedParam = ""
                for(i in 1..inputParamList.size-1){
                    combinedParam += inputParamList[i]+" "
                }
                combinedParam = combinedParam.substring(0,combinedParam.length-1)
                if(mapInputToParam.containsKey(inputParamList[0])){
                    var valFromKey = mapInputToParam[inputParamList[0]]
                    valFromKey += "_"+combinedParam
                    mapInputToParam[inputParamList[0]] = valFromKey.toString()
                }
                else {
                    mapInputToParam[inputParamList[0]] = combinedParam
                }
            }
            mapCurrentStateAndInputToDescription["$fromState-$currentInput"] = description
            mapCurrentStateAndInputToNextState["$fromState-$currentInput"] = toState



        }
//        println("mapInputToParam")
//        println(mapInputToParam)
//        println("mapCurrentStateAndInputToDescription")
//        println(mapCurrentStateAndInputToDescription)
//        println("mapCurrentStateAndInputToNextState")
//        println(mapCurrentStateAndInputToNextState)
    }
}

fun implementStateMachine(startState:String, endState: List<String>, mapStateAndDescription:HashMap<String, String>,
                       mapCurrentStateAndInputToNextState:HashMap<String, String>,mapInputToParam:HashMap<String, String>,
                       mapCurrentStateAndInputToDescription:HashMap<String, String>){
    var currentState = startState
    while(true){
        println("[$currentState]"+" \n "+ mapStateAndDescription[currentState])
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
                var combinedParam = ""
                for(i in 1..usrInputList.size-1){
                    combinedParam += usrInputList[i]+" "
                }
                combinedParam = combinedParam.substring(0,combinedParam.length-1)
                if (listOfParameters != null) {
                    if(!listOfParameters.contains(combinedParam)){
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

fun main(){
    val stateMachineInstance = StateMachineFinal()
    stateMachineInstance.takeFileInput()
}