 def addHashingKey():DataFrame={
val sha1 = java.security.MessageDigest.getInstance("SHA-1")
val enCoder = new sun.misc.BASE64Encoder()
//enCoder.encode(sha1.digest(row.mkString.getBytes))
createDataFrame(df.map(row => {
        Row.fromSeq(row.toSeq ++ enCoder.encode(sha1.digest(row.mkString.getBytes)))
}), df.schema.add("hashing_id", StringType))

 }


def createDataFrame(rdd: RDD[Row], schema: StructType): DataFrame = {
sqlContext.createDataFrame(rdd, schema)
}
