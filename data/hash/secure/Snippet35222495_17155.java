  override def main(args: Array[String]) {
    val filePath = "/Users/joe/Softwares/data/FoodFacts.csv"//args(0)

    val file = new File(filePath)
    println(file.getAbsolutePath)
    // read 1MB of file as a stream
    val fileSource = SynchronousFileSource(file, 1 * 1024 * 1024)
    val shaFlow = fileSource.map(chunk => {
      println(s"the string obtained is ${chunk.toString}")
    })
    shaFlow.to(Sink.foreach(println(_))).run // fails with a null pointer

    def sha256(s: String) = {
      val  messageDigest = MessageDigest.getInstance("SHA-256")
      messageDigest.digest(s.getBytes("UTF-8"))
    }
  }
