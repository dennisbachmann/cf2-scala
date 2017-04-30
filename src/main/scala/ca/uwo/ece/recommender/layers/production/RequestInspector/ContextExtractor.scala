package ca.uwo.ece.recommender.layers.production.RequestInspector

import ca.uwo.ece.recommender.layers.production.RecommenderTrainer.ContextualModelChooser

/**
  * The ContextExtractor is responsible for initiating the recommendation process, as well as for identifying
  * the contextual attribute to be used as a filtering criterion.
  *
  * Granted that obtaining the contextual attribute may require the use of contextual inference,
  * the contextual request is delegated to the ContextInferrer.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextExtractor {
  /**
    * Represents an incoming request for recommendations.
    *
    * It has the following format: (Client ID, Item ID, Context).
    */
  type IncomingRequest = (String, String, String)

  /**
    * Represents a request for recommendations already contextualized.
    *
    * It has the following format: (Client ID, Item ID, Context).
    */
  type ContextualRequest = (String, String, String)

  /**
    * Represents the recommendation returned to the user.
    *
    * It has the following format: (Item ID, Predicted Rating)
    */
  type Recommendation = (String, Double)

  /**
    * Initiated the recommendation process triggered by an incoming request.
    *
    * @param request contains the incoming request
    * @return the list of recommendations specified by Recommendation type
    */
  def getRecommendationsForRequest(request: IncomingRequest): Array[Recommendation] = {
    val contextualizedRequest: ContextualRequest = ContextInferrer.inferContextForRequest(request)
    ContextualModelChooser.provideRecommendationsForRequest(contextualizedRequest)
  }
}
