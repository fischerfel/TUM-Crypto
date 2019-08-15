import java.security.MessageDigest
import java.nio._
import java.io._

import scala.annotation.tailrec

object MD5 {
  def main(args: Array[String]) =  {
    val str = "Data to pack"
    val md5Bytes = calcMd5(str)
    val md5Str = md5Bytes.map(b => "%02x".format(b)).mkString
    println(s"$str => $md5Str")

    // Get last 4-bytes
    val byteArr = md5Bytes.slice(12, 16)

    // Print it
    print(s"Last 4-bytes => ")
    byteArr.foreach { r =>
      print("%02X ".format(r))
    }
    println

  }  

  def calcMd5(str: String): Array[Byte] = {
    MessageDigest.getInstance("MD5")
    .digest(str.getBytes)
  }
}
