val md = MessageDigest.getInstance("MD5")
val input = new FileInputStream("foo.txt")
val buffer = new Array[ Byte ]( 1024 )
Stream.continually(input.read(buffer))
  .takeWhile(_ != -1)
  .foreach(md.update(buffer, 0, _))
md.digest
