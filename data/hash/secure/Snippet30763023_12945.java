import java.File
import java.FileInputStream
import java.security.MessageDigest

val file = new File("test.in")
val is = new FileInputStream(file)

val md = MessageDigest.getInstance("SHA-256")

val bytes = Array.fill[Byte](65536)(0)

Stream
    .continually((is.read(bytes),bytes))
    .takeWhile(_._1 != -1)
    .foreach{ x => md.update(x._2,0,x._1) }

println(md.digest())
// prinln(md.digest().map("%02X" format _).mkString) // if you want hex string
