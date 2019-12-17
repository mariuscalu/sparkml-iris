package com.marius.iris

import com.marius.iris.common.IOHelper
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.tuning.ParamGridBuilder
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.tuning.TrainValidationSplit
import common.DatasetSpliter._

/**
 * Simple implementation of RandomForestClassifier algorithm for classifying the IRIS flowers
 */
object IrisClassification extends IOHelper {

  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf(true)
      .setAppName(CLASSIFICATIOM_APP_NAME)
      .setMaster(MASTER)
    val sc = new SparkContext(conf)
    implicit val sqlContext: SQLContext = new SQLContext(sc)
    
    val dataSet = loadDataset(INPUT_PATH)
    val (trainingData, testData) = splitDataset(dataSet)

    val indexer = new StringIndexer()
      .setInputCol(TYPE_COLUMN)
      .setOutputCol(LABEL)
    val classifier = new RandomForestClassifier()
      .setFeaturesCol(FEATURE_COLUMN)
    val pipeline = new Pipeline()
      .setStages(Array(indexer, classifier))

    val paramGrid = new ParamGridBuilder()
      .addGrid(classifier.maxDepth, MAX_DEPTH)
      .addGrid(classifier.numTrees, NUM_TREES)
      .addGrid(classifier.impurity, IMPURITY)
      .build()
    
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipeline)
      .setEvaluator(new MulticlassClassificationEvaluator())
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(TRAINING_RATIO)
    
    trainingData.cache()
    val model = trainValidationSplit.fit(trainingData)
    
    testData.cache()
    val testResults = model.transform(testData)

    val predAndLabels = testResults
        .select(PREDICTION, LABEL)
        .map { case Row(prediction: Double, label: Double) => 
          (prediction, label)
        }
    val metrics: MulticlassMetrics = new MulticlassMetrics(predAndLabels)
    writeClassificationMetrics(metrics)

    sc.stop()
  }
}