package ca.uwo.ece.recommender.layers.training.filterer

import ca.uwo.ece.recommender.layers.training.RecommenderTrainer
import ca.uwo.ece.recommender.layers.training.filterer.ContextExtractor.ContextualRatingEntry
import ca.uwo.ece.recommender.utils.FileUtils
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

/**
  * The DataSplitter is responsible for splitting the rating dataset into contextual sub sets,
  * and invoking the RecommenderTrainer to train the models.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object DataSplitter {
  /**
    * Splits the dataset into contextual sub sets and invokes the RecommenderTrainer to train the contextual models.
    *
    * @param contextualData contains the whole contextual dataset
    */
  def splitAndTrainDataset(contextualData: RDD[ContextualRatingEntry]) = {
    val sparkContext = SparkContext.getOrCreate()

    val userMapping: RDD[(String, Long)] = contextualData.map(_._1).distinct.zipWithUniqueId
    val itemMapping: RDD[(String, Long)] = contextualData.map(_._2).distinct.zipWithUniqueId

    val broadcastUserMapping: Broadcast[Map[String, Long]] = sparkContext.broadcast(userMapping.collectAsMap.toMap)
    val broadcastItemMapping: Broadcast[Map[String, Long]] = sparkContext.broadcast(itemMapping.collectAsMap.toMap)

    val contextualAttributes: Array[String] = contextualData.map(entry => {entry._4}).distinct.collect

    contextualAttributes.foreach(context => {
      val ratings = contextualData.filter(_._4 == context).map(row => {
        Rating(
          broadcastUserMapping.value(row._1).toInt,
          broadcastItemMapping.value(row._2).toInt,
          row._3
        )
      })
      RecommenderTrainer.train(context, ratings)
    })
    saveMapping[(String, Long)](userMapping,             "UserMapping")
    saveMapping[(Long, String)](itemMapping.map(_.swap), "ItemMapping")

    contextualData.foreach(x => {
      broadcastUserMapping.value(x._1).toInt
    })
  }

  /**
    * Helper method to persist the mappings into a file.
    *
    * @param mapping contains the mapping to be persisted
    * @param name contains the name of the mapping
    * @tparam T represents the type of the mapping
    */
  private def saveMapping[T](mapping: RDD[T], name: String) = {
    if (FileUtils.mappingExists(name)) {
      FileUtils.deleteMapping(name)
    }
    val fileName = FileUtils.fileNameForMapping(name)
    mapping.saveAsObjectFile(fileName)
  }
}
