      def main(args: Array[String]) {
        val pKeyFile = new java.io.File("/path/to/file-cert.pfx")
        val pKeyPassword = "password"
        val keyStore = java.security.KeyStore.getInstance("JKS")

        val kmf = javax.net.ssl.KeyManagerFactory.getInstance(javax.net.ssl.KeyManagerFactory.getDefaultAlgorithm())
        val keyInput = new java.io.FileInputStream(pKeyFile)
        keyStore.load(keyInput, pKeyPassword.toCharArray())
        keyInput.close()
        kmf.init(keyStore, pKeyPassword.toCharArray())

        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom())

        val conf = new SiteToSiteClient
          .Builder()
          .sslContext(sslContext)
          .url("https://urlOfNifi:9090/nifi/")
          .portName("Spark_Test")
          .buildConfig()


        val config = new SparkConf().setAppName("Nifi_Spark_Data")
        val sc = new SparkContext(config)   
        val ssc = new StreamingContext(sc, Seconds(10))

        val lines = ssc.receiverStream(new NiFiReceiver(conf, StorageLevel.MEMORY_ONLY))

        val text = lines.map(dataPacket => new String(dataPacket.getContent, StandardCharsets.UTF_8))

        text.print()
        ssc.start()
        ssc.awaitTermination()  
   }
}
