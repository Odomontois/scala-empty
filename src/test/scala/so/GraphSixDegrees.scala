///**
//  * Author: Oleg Nizhnik
//  * Date  : 02.12.2015
//  * Time  : 11:38
//  */
//package so
//import org.apache.spark.graphx.{EdgeDirection, Graph, Edge}
//import org.apache.spark.rdd.RDD
//
//object GraphSixDegrees {
//  import so.spark.Spark._
//
//  def main(args: Array[String]) {
//    val friends: RDD[Edge[Long]] = sc.textFile("D:\\docs\\oldian\\edges.txt").map(_.split(" ").map(_.toLong) match {
//      case Array(userId, friendId) => (userId, friendId)
//    }).zipWithUniqueId.map { case ((userId, friendId), edgeId) => Edge(userId, friendId, edgeId) }
//    val connections = Graph(sc.emptyRDD[(Long, Unit)], friends, ())
//
//    val twoDegree = connections.collectNeighborIds(EdgeDirection.Out)
//
//    twoDegree.toLocalIterator.foreach(println)
//  }
//
//
//}
