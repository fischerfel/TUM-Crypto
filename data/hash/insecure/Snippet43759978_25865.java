import java.security.MessageDigest
import scala.io.Source
import java.io.PrintWriter
import java.io.File

    object MD5 {
      def md5(file: String)= {
        val text=Source.fromFile(file)
        val s=text.mkString
        val hash = MessageDigest.getInstance("MD5").digest(s.getBytes)
        hash.map("%02x".format(_)).mkString
        }

     def main(args:Array[String])={
        val cipher=md5("InputFile")
        val pw = new PrintWriter(new File("OutputFile"))
        pw.write(cipher)
        pw.close
      }}
