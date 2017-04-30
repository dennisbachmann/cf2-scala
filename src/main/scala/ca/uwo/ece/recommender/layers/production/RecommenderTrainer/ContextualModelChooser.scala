package ca.uwo.ece.recommender.layers.production.RecommenderTrainer

import ca.uwo.ece.recommender.layers.production.RequestInspector.ContextExtractor.{ContextualRequest, Recommendation}
import ca.uwo.ece.recommender.layers.storage.ContextualModelStorage

/**
  * The ContextualModelChooser is responsible for retrieving the correct contextual model from
  * the ContextualModelStorage and invoking the Predictor to obtain the list of recommendations.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextualModelChooser {
  /**
    * Retrieves the contextual model and invokes the Predictor to obtain the list of recommendations.
    *
    * @param request contains the contextualized request
    * @return the list of recommendations specified by Recommendation type
    */
  def provideRecommendationsForRequest(request: ContextualRequest): Array[Recommendation] = {
    val context: String = request._3
    val model = ContextualModelStorage.retrieveModelWithContextualAttribute(context)

    Predictor.getRecommendationsForRequestUsingModel(request, model)
  }
}
