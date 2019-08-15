import java.io.{FileOutputStream, FileInputStream, File}
import java.util.concurrent.Executors
import org.apache.commons.io.IOUtils
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PipeTest extends FlatSpec {

  def md5sum(data: Array[Byte]) = {
    import java.security.MessageDigest
    MessageDigest.getInstance("MD5").digest(data).map("%02x".format(_)).mkString
  }

  "Pipe" should "block here" in {
    val pipe = new File("/tmp/mypipe")
    val srcData = new File("/tmp/random.10m")
    val md5 = "8e0a24d1d47264919f9d47f5223c913e"
    val executor = Executors.newSingleThreadExecutor()
    executor.execute(new Runnable {
      def run() {
        (1 to 10).foreach {
          id =>
            val fis = new FileInputStream(pipe)
            assert(md5 === md5sum(IOUtils.toByteArray(fis)))
            fis.close()
        }
      }
    })
    (1 to 10).foreach {
      id =>
        val is = new FileInputStream(srcData)
        val os = new FileOutputStream(pipe)
        IOUtils.copyLarge(is, os)
        os.flush()
        os.close()
        is.close()
        Thread.sleep(200)
    }
  }

}
