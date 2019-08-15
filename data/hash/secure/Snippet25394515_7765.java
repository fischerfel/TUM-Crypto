import scalaz.stream._
import java.security.MessageDigest

val f = "/a/b/myfile.bin"
val bufSize = 4096

val digest = MessageDigest.getInstance("SHA-256")

Process.constant(bufSize).toSource
  .through(io.fileChunkR(f, bufSize))
