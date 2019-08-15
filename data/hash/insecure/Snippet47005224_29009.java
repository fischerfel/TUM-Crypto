    UDF1 url_md5 = new UDF1<String,String>() {
        @Override
        public String call(String x) throws Exception {
            if (Objects.isNull(x)) return "";
            byte[] md5 = MessageDigest.getInstance("MD5").digest(x.getBytes());
            return BaseEncoding.base16().lowerCase().encode(md5);
        }
    };

spark.sqlContext().udf().register("url_md5", url_md5, DataTypes.StringType);

df.withColumn("url_md5", org.apache.spark.sql.functions.callUDF("url_md5", col("url")))
