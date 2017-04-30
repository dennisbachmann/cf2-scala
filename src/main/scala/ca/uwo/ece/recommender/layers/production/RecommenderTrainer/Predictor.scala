package ca.uwo.ece.recommender.layers.production.RecommenderTrainer

import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor.{ContextualRequest, Recommendation}
import ca.uwo.ece.recommender.utils.FileUtils
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.rdd.RDD

/**
  * The Predictor is responsible for querying the model for recommendations.
  *
  * It uses the mappings generated during the training phase to convert Client IDs and Item IDs into something the user
  * can understand.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object Predictor {
  /**
    * Defines the number of recommendations to generate.
    */
  final val NumberOfRecommendations = 5

  /**
    * Generates recommendations for the client specified in the request.
    *
    * @param request contains the contextualized request
    * @param model contains the model to be used when generating recommendations
    * @return the list of recommendations specified by Recommendation type
    */
  def getRecommendationsForRequestUsingModel(request: ContextualRequest, model: Option[MatrixFactorizationModel]): Array[Recommendation] = {
    var recommendations: Array[Recommendation] = Array[Recommendation]()

    val userMappingOption = getUserMapping
    val itemMappingOption = getItemMapping

    if (model.isDefined && userMappingOption.isDefined && itemMappingOption.isDefined) {
      val contextualModel: MatrixFactorizationModel = model.get
      val userMapping: collection.Map[String, Long] = userMappingOption.get.collectAsMap
      val itemMapping: collection.Map[Long, String] = itemMappingOption.get.collectAsMap

      val userIdentifier: String = request._1

      if (!userMapping.exists(_._1 == userIdentifier))
        return recommendations

      contextualModel.userFeatures.cache()
      contextualModel.productFeatures.cache()

      recommendations = contextualModel
        .recommendProducts(userMapping(userIdentifier).toInt, NumberOfRecommendations)
        .map(rating => (itemMapping(rating.product), rating.rating))
    }

    recommendations
  }

  /**
    * Helper method to retrieve the user mapping.
    *
    * @return the user mapping if it was successful or None otherwise
    */
  private def getUserMapping(): Option[RDD[(String, Long)]] = {
    val fileName = getMappingFileName("UserMapping")
    if (fileName.isDefined) {
      val sparkContext = SparkContext.getOrCreate
      return Some(sparkContext.objectFile[(String, Long)](fileName.get))
    }
    None
  }

  /**
    * Helper method to retrieve the item mapping.
    *
    * @return the item mapping if it was successful or None otherwise
    */
  private def getItemMapping(): Option[RDD[(Long, String)]] = {
    val fileName = getMappingFileName("ItemMapping")
    if (fileName.isDefined) {
      val sparkContext = SparkContext.getOrCreate
      return Some(sparkContext.objectFile[(Long, String)](fileName.get))
    }
    None
  }

  /**
    * Helper method to retrieve the mapping filename.
    *
    * @return the path for the mapping if it exists or None otherwise
    */
  private def getMappingFileName(name: String): Option[String] = {
    if (FileUtils.mappingExists(name)) {
      val fileName = FileUtils.fileNameForMapping(name)
      return Some(fileName)
    }
    None
  }
}
