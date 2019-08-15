import java.io._
import java.security.MessageDigest
import resource._
import scodec.bits.ByteVector
import scalaz._, Scalaz._
import scalaz.concurrent.Task
import scalaz.stream._
import scalaz.stream.io._

val f = "/a/b/myfile.bin"
val bufSize = 4096

val md = MessageDigest.getInstance("SHA-256")

def _digestResource(md: => MessageDigest): Sink[Task,ByteVector] =
      resource(Task.delay(md))(md => Task.delay(()))(
        md => Task.now((bytes: ByteVector) => Task.delay(md.update(bytes.toArray))))

Process.constant(4096).toSource
    .through(fileChunkR(f.getAbsolutePath, 4096))
    .to(_digestResource(md))
    .run
    .run

md.digest()
