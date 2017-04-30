package ca.uwo.ece.recommender

import ca.uwo.ece.recommender.layers.training.filterer.ContextExtractor
import ca.uwo.ece.recommender.utils.FileUtils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dennisbachmann on 2017-04-28.
  */
object InitiateTraining {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Recommendation Training").set("spark.scheduler.mode", "FAIR")
    val sc = SparkContext.getOrCreate(conf)

    sc.setLogLevel("ERROR")

    val trainingFile = args(0)

    if (FileUtils.fileExists(trainingFile))
      ContextExtractor.initiateTrainingWithCSVDataset(path = trainingFile)
    else
      println("Couldn't find training file.")
  }
}
