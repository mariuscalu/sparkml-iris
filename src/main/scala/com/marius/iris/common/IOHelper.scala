package com.marius.iris.common

import java.io.{BufferedWriter, File, FileWriter}

import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.{DataFrame, SQLContext}

trait IOHelper extends Constants {

  /**
   * It writes the results for the classification algorithm to a file on disk
   * @param metrics
   */
  def writeClassificationMetrics(metrics: MulticlassMetrics): Unit = {
    val outputFile = new File(CLASSIFICATION_OUTPUT_PATH)
    val writer = new BufferedWriter(new FileWriter(outputFile))

    val precision = s"precision : ${metrics.precision}"
    val weightedPrecision = s"weightedPrecision : ${metrics.weightedPrecision}"
    val fMeasure  = s"fMeasure : ${metrics.fMeasure}"
    val weightedFMeasure = s"weightedFMeasure : ${metrics.weightedFMeasure}"
    val recall = s"recall : ${metrics.recall}"
    val weightedRecall = s"weightedRecall : ${metrics.weightedRecall}"
    val weightedFalsePositiveRate = s"weightedFalsePositiveRate : ${metrics.weightedFalsePositiveRate}"
    val weightedTruePositiveRate = s"weightedTruePositiveRate : ${metrics.weightedTruePositiveRate}"
    val content = List(precision, weightedPrecision, fMeasure, weightedFMeasure, recall, weightedRecall
      , weightedFalsePositiveRate, weightedTruePositiveRate)

    content.foreach(metric => {
      writer.write(metric)
      writer.write("\n")
    })
    writer.close()
  }

  /**
   * It writes the results for the clustering algorithm to a file on disk
   * @param clusterCenters
   * @param wssse
   */
  def writeClusteringMetrics(clusterCenters: Array[linalg.Vector], wssse: Double): Unit = {
    val outputFile = new File(CLUSTERING_OUTPUT_PATH)
    val writer = new BufferedWriter(new FileWriter(outputFile))
    writer.write(s"Within Set Sum of Squared Errors : $wssse")
    writer.write("\n")
    writer.write("Clusters :")
    writer.write("\n")
    clusterCenters.foreach(cluster => {
      writer.write(cluster.toString)
      writer.write("\n")
    })
    writer.close()
  }

  /**
   * It loads into memory the dataset from the csv file
   * @param filePath
   * @param sqlContext
   * @return
   */
  def loadDataset(filePath: String)(implicit sqlContext: SQLContext): DataFrame = {
    val irisData = sqlContext.sparkContext.textFile(filePath).flatMap { text =>
      text.split("\n").toList.map(_.split(",")).collect {
        case Array(sepalLength, sepalWidth, petalLength, petalWidth, irisType) =>
          (Vectors.dense(sepalLength.toDouble, sepalWidth.toDouble, petalLength.toDouble, petalWidth.toDouble), irisType)
      }
    }
    sqlContext.createDataFrame(irisData).toDF(FEATURE_COLUMN, TYPE_COLUMN)
  }
}