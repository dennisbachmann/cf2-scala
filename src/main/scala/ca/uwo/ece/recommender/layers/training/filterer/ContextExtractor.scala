package ca.uwo.ece.recommender.layers.training.filterer

import ca.uwo.ece.recommender.layers.storage.RatingStorage
import org.apache.spark.rdd.RDD
import ca.uwo.ece.recommender.layers.storage.RatingStorage.EntryInSource

/**
  * The ContextExtractor is responsible for initiating the training process, as well as for identifying
  * the contextual attribute to be used as a filtering criterion.
  *
  * Granted that obtaining the contextual attribute may require the use of contextual inference,
  * the contextual dataset is delegated to the ContextInferrer.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextExtractor {
  /**
    * Represents a contextualized entry in the source.
    *
    * It has the following format: (Client ID, Item ID, Rating, Context).
    */
  type ContextualRatingEntry = (String, String, Double, String)

  /**
    * Initiates the training phase using a CSV file.
    *
    * @param path contains the path of the CSV file.
    */
  def initiateTrainingWithCSVDataset(path: String) = {
    val ratingData:     RDD[EntryInSource] = RatingStorage.getDataFromFile(path)
    val contextualData: RDD[ContextualRatingEntry] = ContextInferrer.inferContextForDataset(ratingData)
    DataSplitter.splitAndTrainDataset(contextualData)
  }
}
