package org.apache.spark.graphx

import java.io._
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.graphx.lib.PageRank
import org.apache.spark.rdd.RDD

object PageRankGraph extends Serializable {

  def hashId(str: String): Long = {

    val bytes = MessageDigest.getInstance("MD5").digest(str.getBytes(StandardCharsets.UTF_8))
    (bytes(0) & 0xFFL) |
      ((bytes(1) & 0xFFL) << 8) |
      ((bytes(2) & 0xFFL) << 16) |
      ((bytes(3) & 0xFFL) << 24) |
      ((bytes(4) & 0xFFL) << 32) |
      ((bytes(5) & 0xFFL) << 40) |
      ((bytes(6) & 0xFFL) << 48) |
      ((bytes(7) & 0xFFL) << 56)
  }

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName("WikipediaGraphXPageRank")
      .setMaster(args(5))
      .set("spark.executor.memory","1g")

    val sc = new SparkContext(sparkConf)
    val topics: RDD[String] = (sc.textFile(args(0))
      .map(line => line.split("\t")).map(parts => (parts.head)))

    val vertices = topics map (topic => hashId(topic) -> topic)
    val uniqueHashes = vertices.map(_._1).countByValue()
    val uniqueTopics = vertices.map(_._2).countByValue()
    uniqueHashes.size == uniqueTopics.size

    val linksall = (sc.textFile(args(0))).map(l => l.split("\t"))
    val links = for (l <- linksall; l2 <- l(2).split(",")) yield (l(0), l2)

    val edges = for (l <- links) yield Edge(hashId(l._1), hashId(l._2), 0)
    val graph = Graph(vertices, edges, "").cache()

    graph.vertices.count

    if (args(4).toInt == 1) graph.partitionBy(PartitionStrategy.RandomVertexCut)
    else if (args(4).toInt == 2) graph.partitionBy(PartitionStrategy.EdgePartition2D)
    else if (args(4).toInt == 3) graph.partitionBy(PartitionStrategy.CanonicalRandomVertexCut)
    else graph.partitionBy(PartitionStrategy.EdgePartition1D)

    var prGraph = PageRank.run(graph, args(2).toInt, args(3).toDouble)

    val titleAndPrGraph = graph.outerJoinVertices(prGraph.vertices) {
      (v, title, rank) => (rank.getOrElse(0.0), title)
    }

    val pw = new PrintWriter(new File(args(1)))

    titleAndPrGraph.vertices.top(100) {
      Ordering.by((entry: (VertexId, (Double, String))) => entry._2._1)
    }.foreach(t =>
      if(t._2._2 != "") {
      pw.write("\nTitle: " + t._2._2 + " : " + t._2._1 + "\n")
    })

    pw.close()
  }

}
