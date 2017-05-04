package ca.uwo.ece.recommender.utils

import java.io.FileNotFoundException

import scala.reflect.io.File

/**
  * Created by dennisbachmann on 2017-04-28.
  */
object FileUtils {

  // TODO: Make this object HDFS friendly

  /*
   * Helpers for Model Files
   */
  /**
    * Returns the expected filename of a contextual model.
    * This helpers specifies the location to store the models and remove non-alphanumeric characters from its name.
    *
    * By default, the conventional location to store the models is the cf2-scala/models folder.
    *
    * @param name contains the name of the model
    * @return the expected filename of a contextual model
    */
  def fileNameForModel(name: String): String = {
    val path = "models"
    if (fileExists(path)) {
      val sanitizedName = name.replaceAll("[^A-Za-z0-9]", "")
      s"$path/$sanitizedName"
    } else {
      throw new FileNotFoundException(s"The folder '$path' is missing")
    }
  }

  /**
    * Returns true if a model with certain name exists.
    *
    * @param name contains the name of the model
    * @return true if a model with a certain name exists
    */
  def modelExists(name: String): Boolean = {
    val fileName = fileNameForModel(name)
    fileExists(fileName)
  }

  /**
    * Deletes the file containing a contextual model with a certain name.
    *
    * @param name contains the name of the model
    * @return true if deletion was successful
    */
  def deleteModel(name: String): Boolean = {
    val fileName = fileNameForModel(name)
    deleteFile(fileName)
  }

  /*
   * Helpers for Mapping Files
   */

  /**
    * Returns the expected filename of a mapping file.
    * This helpers specifies the location to store the mappings for contextual models.
    *
    * By default, the conventional location to store the models is the cf2-scala/models/mappings folder.
    *
    * @param name contains the name of the model
    * @return the expected filename of a contextual model
    */
  def fileNameForMapping(name: String): String = {
    val path = "models/mappings"
    if (fileExists(path)) {
      s"$path/$name"
    }
    else {
      throw new FileNotFoundException(s"The folder '$path' is missing")
    }
  }

  /**
    * Returns true if a mapping with certain name exists.
    *
    * @param name contains the name of the mapping
    * @return true if a mapping with a certain name exists
    */
  def mappingExists(name:String): Boolean = {
    val fileName = fileNameForMapping(name)
    fileExists(fileName)
  }

  /**
    * Deletes the file containing a mapping with a certain name.
    *
    * @param name contains the name of the mapping
    * @return true if deletion was successful
    */
  def deleteMapping(name: String): Boolean = {
    val fileName = fileNameForMapping(name)
    deleteFile(fileName)
  }

  /*
   * Helpers for Standard Files
   */

  /**
    * Returns the file of a mapping having the provided name.
    *
    * @param name contains the name of the file
    * @return the requested file
    */
  def fileForName(name: String): File = {
    File(name)
  }

  /**
    * Returns true if a file with certain name exists.
    *
    * @param name contains the name of the file
    * @return true if file exists
    */
  def fileExists(name: String): Boolean = {
    val file = fileForName(name)
    file.isDirectory || file.isFile
  }

  /**
    * Deletes recursively the file with the provided name.
    *
    * @param name contains the name of the file
    * @return true if deletion was successful
    */
  def deleteFile(name: String): Boolean = {
    val file = fileForName(name)
    file.deleteRecursively()
  }
}
