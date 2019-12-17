package com.marius.iris.common

import org.apache.spark.sql.DataFrame

object DatasetSpliter extends Constants {

  /**
   * It splits the given dataset into training and test datasets
   * @param dataSet Iris Flowers Dataset
   * @return Training Dataset and Test Dataset
   */
  def splitDataset(dataSet: DataFrame): (DataFrame, DataFrame) = {
    val split = dataSet.randomSplit(Array(TRAINING_RATIO, TEST_RATIO))
    (split(0), split(1))
  }
}
