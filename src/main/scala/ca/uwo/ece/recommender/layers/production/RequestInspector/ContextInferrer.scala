package ca.uwo.ece.recommender.layers.production.RequestInspector

import ContextExtractor.{ContextualRequest, IncomingRequest}

/**
  * The ContextInferrer process the "raw" incoming request to make it a contextual request.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextInferrer {
  /**
    * Returns a contextual request specified by the ContextualRequest type.
    *
    * @param request contains the "raw" incoming request
    * @return the contextual request
    */
  def inferContextForRequest(request: IncomingRequest): ContextualRequest = {
    // ASSUMES CONTEXT IS ALREADY DEFINED IN INCOMING REQUEST
    request match {
      case contextualRequest: ContextualRequest => contextualRequest
      case _ => throw new ClassCastException
    }
  }
}
