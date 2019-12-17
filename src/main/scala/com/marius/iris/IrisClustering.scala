package com.marius.iris

import com.marius.iris.common.IOHelper
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.clustering.KMeans
import common.DatasetSpliter._

/**
 * Simple implementation of KMeans algorithm for clustering the IRIS flowers
 */
object IrisClustering extends IOHelper {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf(true)
      .setAppName(CLUSTERING_APP_NAME)
      .setMaster(MASTER)
    val sc = new SparkContext(conf)
    implicit val sqlContext: SQLContext = new SQLContext(sc)

    val dataSet = loadDataset(INPUT_PATH)
    val (trainingData, testData) = splitDataset(dataSet)

    val kmeans = new KMeans()
      .setK(3)
      .setFeaturesCol(FEATURE_COLUMN)

    trainingData.cache()
    val model = kmeans.fit(trainingData)

    testData.cache()
    val predictions = model.transform(testData)

    val wssse: Double = model.computeCost(dataSet)
    writeClusteringMetrics(model.clusterCenters, wssse)

    sc.stop()
  }
}