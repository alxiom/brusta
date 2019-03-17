/**
  * Created by Alex on 17/03/2019.
  */

import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import sys.process._

class SummitService {

  private val projectPath = System.getProperty("user.dir").split("/").dropRight(1).mkString("/")
  private val headerFilePath = s"${projectPath}/launcher/ModelIO.hpp"
  private val configFilePath = s"${projectPath}/burner/flint/server.config"

  private val headerText = Seq.empty[String]
  private val configText = Seq("""modelPath=${PWD}""", """libPath=${PWD}/burner-0.1.0/lib""")

  private def writeText(savePath: String, textSeq: Seq[String]): Unit = {
    val saveFile = Files.newBufferedWriter(Paths.get(savePath), Charset.forName("UTF8"))
    saveFile.write(textSeq.mkString("\n").trim)
    saveFile.close()
  }

  def apply(parameterMap: Map[String, String]): Unit = {
    val headerTextInRequest = headerText :+ s"""#define OUTPUT_SIZE ${parameterMap("outputFeatureSize")}"""
    val configTextInRequest = configText ++
      Seq(s"""port=${parameterMap("port")}""", s"""memory=${parameterMap("memory")}""")
    val modelType = parameterMap("type")
    val (headerTextFinal, configTextFinal) = modelType match {
      case "dnn" =>
        val headerTextInType = headerTextInRequest :+
          s"""#define INPUT_FEATURE_SIZE ${parameterMap("inputFeatureSize")}"""
        val configTextInType = configTextInRequest :+ s"""inputDim=${parameterMap("inputFeatureSize")}"""
        (headerTextInType, configTextInType)
      case "rnn" =>
        val headerTextInType = headerTextInRequest ++
          Seq(
            s"""#define INPUT_FEATURE_SIZE ${parameterMap("inputFeatureSize")}""",
            s"""#define INPUT_SEQUENCE_SIZE ${parameterMap("inputSequenceSize")}"""
          )
        val configTextInType = configTextInRequest :+
          s"""inputDim=${parameterMap("inputFeatureSize").toInt * parameterMap("inputSequenceSize").toInt}"""
        (headerTextInType, configTextInType)
    }
    writeText(headerFilePath, headerTextFinal)
    writeText(configFilePath, configTextFinal)
    s"cp ${projectPath}/launcher/Model-${modelType.toUpperCase}.cpp ${projectPath}/launcher/Model.cpp".!
    s"${projectPath}/lever/ignite.sh".!
  }

}
