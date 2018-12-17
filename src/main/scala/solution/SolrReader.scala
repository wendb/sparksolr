package solution

import com.lucidworks.spark.util.SolrQuerySupport
import org.apache.spark.sql.SparkSession

object SolrReader {

  val sparkSession = SparkSession
    .builder()
    .appName("SparkSolr")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.speculation", "true")
    .getOrCreate()


  def main(args: Array[String]): Unit = {
    if (args.length < 3) throw new IllegalArgumentException("Must have at least 3 arguments!" +
      " First: Solr Zookeeper quorum" +
      " Second: source collection" +
      " Third: destination collection")
    val zkHost = args(0)
    val sourceCollection = args(1)
    val destCollection = args(2)

    val sourceOptions = Map(
      "zkHost" -> zkHost,
      "collection" -> sourceCollection
    )

    val destOptions = Map(
      "zkHost" -> zkHost,
      "collection" -> destCollection,
      "gen_uniq_key" -> "true"
    )

//    sparkSession.sparkContext.setLogLevel("DEBUG")

    val sourceDF = sparkSession.read.format("solr")
      .options(sourceOptions)
      .load
    sourceDF.printSchema()
    val sourceDocuments = SolrQuerySupport.getNumDocsFromSolr(sourceOptions.get("collection").mkString,sourceOptions.get("zkHost").mkString,None)
    println("Source Collection Documents: " + sourceDocuments )
    sourceDF.createOrReplaceTempView("collection")

//    val hdfsParquet = hdfsParentDir + "/" + sourceCollection + ".parquet"
//    sourceDF.write.format("parquet").save(hdfsParquet)
//    val destDF = sparkSession.read.parquet(hdfsParquet)

    val destDF = sparkSession.sql("SELECT * FROM collection")
    destDF.printSchema()
    destDF.write.format("solr")
      .options(destOptions)
      .mode(org.apache.spark.sql.SaveMode.Overwrite)
      .save

//    Can't check the dest collection doc counts immediately, because Solr server will commit later
//    val destDocuments = SolrQuerySupport.getNumDocsFromSolr(destOptions.get("collection").mkString,destOptions.get("zkHost").mkString,None)
//    println("Destination Collection Documents: " + destDocuments )
//    println(sourceDocuments == destDocuments)

    sparkSession.close()
  }

}
