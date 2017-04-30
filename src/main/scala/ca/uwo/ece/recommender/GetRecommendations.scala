package ca.uwo.ece.recommender

import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor
import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor.IncomingRequest
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dennisbachmann on 2017-04-28.
  */
object GetRecommendations {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Recommendation Training").set("spark.scheduler.mode", "FAIR")
    val sc = SparkContext.getOrCreate(conf)

    sc.setLogLevel("ERROR")

    val request: IncomingRequest = (args(0), args(1), args(2))
    ContextExtractor.getRecommendationsForRequest(request).foreach(println)
  }
}
