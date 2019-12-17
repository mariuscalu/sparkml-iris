package com.marius.iris.common

trait Constants {
  val INPUT_PATH = "src/main/resources/data.csv"
  val CLASSIFICATION_OUTPUT_PATH = "src/main/resources/output/classification_results.txt"
  val CLUSTERING_OUTPUT_PATH = "src/main/resources/output/clustering_results.txt"
  val FEATURE_COLUMN = "iris-features"
  val TYPE_COLUMN = "iris-type"
  val MASTER = "local"
  val PREDICTION = "prediction"
  val LABEL = "label"
  val CLUSTERING_APP_NAME = "iris-ml-clustering"
  val CLASSIFICATIOM_APP_NAME = "iris-ml-classification"

  val TRAINING_RATIO = 0.8
  val TEST_RATIO = 0.2

  val IMPURITY = Array("gini", "entropy")
  val NUM_TREES = Array(10, 20, 40)
  val MAX_DEPTH = Array(2, 5, 10)
}
