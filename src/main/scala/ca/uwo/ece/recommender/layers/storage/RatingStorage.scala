package ca.uwo.ece.recommender.layers.storage

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * The RatingStorage ensures a standardized interface to access the ratings dataset.
  *
  * This interface will be used during the training phase to provide access to ratings.
  *
  * Created by dennisbachmann on 2017-04-28.
  */
object RatingStorage {
  /**
    * Represents an entry in the source.
    *
    * It has the following format: (Client ID, Item ID, Rating, Context).
    */
  type EntryInSource = (String, String, Double, String)

  /**
    * Returns the entries inside a text file.
    *
    * @param path contains the path of the file
    * @param clientIdentifierIndex contains the position of Client ID
    * @param itemIdentifierIndex contains the position of Item ID
    * @param contextualAttributeIndex contains the position of Contextual attribute
    * @param numberOfColumnsInFile contains the number of columns in the file
    * @param fieldSeparator contains the String used as field separator
    * @param defaultRating contains the default value for ratings in the file
    * @return the collection of ratings in the format specified by the EntryInSource type
    */
  def getDataFromFile(path:                  String,
                      clientIdentifierIndex:    Int = 0,
                      itemIdentifierIndex:      Int = 1,
                      contextualAttributeIndex: Int = 2,
                      numberOfColumnsInFile:    Int = 3,
                      fieldSeparator:        String = ",",
                      defaultRating:         Double = 1.0
                     ): RDD[EntryInSource] = {

    SparkContext
      .getOrCreate
      .textFile(path)
      .map(_.split(fieldSeparator, numberOfColumnsInFile))
      .map(row => {
        (row(clientIdentifierIndex), row(itemIdentifierIndex), defaultRating, row(contextualAttributeIndex))
      })
  }

}
