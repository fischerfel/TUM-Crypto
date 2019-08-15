import akka.stream.io._
import java.io.File
import scala.concurrent.Future
import akka.stream.scaladsl._
import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import java.security.MessageDigest

object LargeFile extends App{
  implicit val system = ActorSystem("Sys")
  import system.dispatcher
  implicit val materializer = ActorFlowMaterializer()

   val file = new File("<path to large file>")

   val fileSource = SynchronousFileSource(file, 65536)

   val shaFlow = fileSource.map(chunk => sha256(chunk.toString))

   shaFlow.to(Sink.foreach(println(_))).run//TODO - Convert the byte[] and sum them using fold

   def sha256(s: String) = {
     val  messageDigest = MessageDigest.getInstance("SHA-256")
     messageDigest.digest(s.getBytes("UTF-8"))
   }
}
