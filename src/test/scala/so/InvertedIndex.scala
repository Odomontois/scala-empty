///**
//  * Author: Oleg Nizhnik
//  * Date  : 01.12.2015
//  * Time  : 9:50
//  */
//package so
//import so.spark.Spark._
//import org.apache.spark.rdd.RDD
//object InvertedIndex {
//  val txtFilePath = "D:\\docs\\oldian\\input.csv"
//  val txtFile = sc.textFile(txtFilePath)
//
//  val data = txtFile.map(line => line.split(",").map(elem => elem.trim()))
//
//  val header = sc.broadcast(data.first())
//
//  val cells = data.zipWithIndex().filter(_._2 > 0).flatMap { case (row, index) =>
//    row.zip(header.value).map { case (value, column) => value ->(column, index) }
//  }
//
//  val index: RDD[(String, Vector[(String, Long)])] =
//    cells.aggregateByKey(Vector.empty[(String, Long)])(_ :+ _, _ ++ _)
//
//val cellsVerbose = data.zipWithIndex().flatMap {
//  case (row, 1) => IndexedSeq.empty // skipping header row
//  case (row, index) => row.zip(header.value).map {
//    case (value, column) => value ->(column, index)
//  }
//}
//
//  val indexVerbose: RDD[(String, Vector[(String, Long)])] =
//    cellsVerbose.aggregateByKey(zeroValue = Vector.empty[(String, Long)])(
//      seqOp = (keys, key) => keys :+ key,
//      combOp = (keysA, keysB) => keysA ++ keysB)
//}
