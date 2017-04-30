package ca.uwo.ece.recommender

import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor
import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor.ContextualRequest
import org.apache.spark.{SparkConf, SparkContext}
import scala.util.Random

/**
  * Created by dennisbachmann on 2017-04-28.
  */

object LiveRecommender {

  def setup() = {
    val conf = new SparkConf().setAppName("Live Recommender").set("spark.scheduler.mode", "FAIR")
    val sc = SparkContext.getOrCreate(conf)

    sc.setLogLevel("ERROR")
  }
  def instructions() = {
    println("Request format: (User ID, Item ID, Context)")
    println("To get recommendations for request: ContextExtractor.getRecommendationsForRequest(request)")
  }
  def getRecommendations() = {
    def generateRandom() = 1 + Random.nextInt(10)
    val user = s"User ID#$generateRandom"
    val item = s"Item ID#$generateRandom"
    val context = s"Context #$generateRandom"

    println("Getting recommendations for:")
    println(s"User: $user")
    println(s"Item: $item")
    println(s"Context: $context")
    val request: ContextualRequest = (user, item, context)

    val recommendations = ContextExtractor.getRecommendationsForRequest(request)

    if (recommendations.length > 0) {
      recommendations.foreach(recommendation =>
      println(s"Recommend item ("+recommendation._1+") with predicted rating of " + "%.3f".format(recommendation._2)))
    } else {
      println("No recommendation for this user")
    }
  }

  def getSeveralRecommendations(n: Int = 10) = {
    for(_ <- 1 to n) getRecommendations()
  }
}