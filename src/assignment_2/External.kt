package assignment_2

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * I copied this program from https://stackoverflow.com/questions/14915319/get-output-of-terminal-command-using-java
 */

fun main(){
    val processBuilder: ProcessBuilder = ProcessBuilder("uname","-a")
    processBuilder.redirectErrorStream(true)
    val process: Process = processBuilder.start()
    val inputStream: InputStream = process.inputStream
    val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
    var line:String?= null
    line =  bufferedReader.readLine()
    print(line)
}
