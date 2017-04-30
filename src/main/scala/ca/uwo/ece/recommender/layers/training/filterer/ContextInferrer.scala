package ca.uwo.ece.recommender.layers.training.filterer

import ca.uwo.ece.recommender.layers.storage.RatingStorage.EntryInSource
import ca.uwo.ece.recommender.layers.training.filterer.ContextExtractor.ContextualRatingEntry
import org.apache.spark.rdd.RDD

/**
  * The ContextInferrer process the "raw" datasource to make it a contextual dataset.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextInferrer {
  /**
    * Returns a collection with contextual rating entries specified by the ContextualRatingEntry type.
    *
    * @param ratingData contains the "raw" dataset
    * @return the collection of contextual ratings
    */
  def inferContextForDataset(ratingData: RDD[EntryInSource]): RDD[ContextualRatingEntry] = {
    // ASSUMES CONTEXT IS ALREADY DEFINED IN INCOMING DATASET
    ratingData match {
      case contextualRatingEntry: RDD[ContextualRatingEntry] => contextualRatingEntry
      case _ => throw new ClassCastException
    }
  }
}
