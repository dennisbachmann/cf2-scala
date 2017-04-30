package ca.uwo.ece.recommender.layers.training

import ca.uwo.ece.recommender.layers.storage.ContextualModelStorage
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD

/**
  * The RecommenderTrainer is responsible for training and sustaining the contextual collaborative filtering models.
  *
  * After training takes place, the model is forwarded to the ContextualModelStorage to be stored and will be used
  * during the production phase.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object RecommenderTrainer {
  /**
    * Contain the training parameters required by Spark's ALS.trainImplicit method
    */
  private final val Parameters = Map[String,Double](
    "alpha" -> 1500.0,
    "lambda" -> 100.0,
    "numberOfIterations" -> 10.toDouble,
    "numberOfFeatures" -> 20.toDouble
  )

  /**
    * Trains the model representing a context using the provided ratings
    * @param context contains the context of the model and will be used to give a name to the model
    * @param ratings contains the ratings of the context
    */
  def train(context: String, ratings: RDD[Rating]) = {
    val model = ALS.trainImplicit(
      ratings,
      Parameters("numberOfFeatures").toInt,
      Parameters("numberOfIterations").toInt,
      Parameters("lambda"),
      Parameters("alpha")
    )
    ContextualModelStorage.storeModelWithContextualAttribute(model,context)
  }

}
