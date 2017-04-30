package ca.uwo.ece.recommender.layers.storage

import ca.uwo.ece.recommender.utils.FileUtils
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

/**
  * The ContextualModelStorage functions as the interface between the training phase
  * and the production phase, handling the storage of and access to all the contextual collaborative
  * filtering models trained during the training phase.
  *
  * These models will later be used by the RecommenderEngine to provide customized recommendations.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object ContextualModelStorage {

  /**
    * Helper method to assist in saving the model into a file.
    *
    * @param model contains the model to be saved
    * @param name contains the name of the model
    */
  private def saveModelToFile(model:MatrixFactorizationModel, name:String) = {
    val sparkContext = SparkContext.getOrCreate()
    val fileName = FileUtils.fileNameForModel(name)
    model.save(sparkContext, fileName)
  }

  /**
    * Helper method to assist in loading the model from a file.
    *
    * @param name contains the name of the model
    * @return the model loaded from the file
    */
  private def loadModelFromFile(name:String): MatrixFactorizationModel = {
    val sparkContext = SparkContext.getOrCreate()
    val fileName = FileUtils.fileNameForModel(name)
    MatrixFactorizationModel.load(sparkContext, fileName)
  }

  /**
    * Stores the provided model using its context as name.
    *
    * @param model contains the model to be saved
    * @param context contains the context of the model
    */
  def storeModelWithContextualAttribute(model: MatrixFactorizationModel, context: String) = {
    if (FileUtils.modelExists(context)) {
      FileUtils.deleteModel(context)
    }
    saveModelToFile(model, context)
  }

  /**
    * Retrieves the contextual model.
    *
    * @param context contains the context of the requested model
    * @return the contextual model
    */
  def retrieveModelWithContextualAttribute(context:String): Option[MatrixFactorizationModel] = {
    var model:MatrixFactorizationModel = null
    if (FileUtils.modelExists(context)) {
      model = loadModelFromFile(context)
    }
    Some(model)
  }
}
