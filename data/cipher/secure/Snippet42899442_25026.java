import java.security._
import java.security.spec.X509EncodedKeySpec
import javax.crypto._
import org.apache.commons.codec.binary.Base64
import scala.io.Source
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import java.util.logging.Logger

object RSA {
   def bytes2hex(bytes: Array[Byte]): String = {
    val hex = new StringBuilder()
    for (i <- 0 to bytes.length - 1) {
      val b = bytes(i)
      var negative = false
      if (b < 0) {
        negative = true
      }
      val inte = Math.abs(b)
      val temp = Integer.toHexString(inte & 0xFF)
      if (temp.length() == 1) {
        hex.append("0")
      }
      // hex.append(temp.toLowerCase())
      hex.append(temp)
    }
    hex.toString
  }



def decodePublicKey(encodedKey: String):Option[PublicKey] = { 
    this.decodePublicKey(
      (new Base64()).decode(encodedKey)
    )   
  }
def decodePublicKey(encodedKey: Array[Byte]): Option[PublicKey]= { 
    scala.util.control.Exception.allCatch.opt {
      val spec = new X509EncodedKeySpec(encodedKey)
      val factory = KeyFactory.getInstance("RSA")
      factory.generatePublic(spec)
    }   
  }

def encrypt(file: String,key:PublicKey): Array[Byte] = { 

val cipher = Cipher.getInstance("RSA")

cipher.init(Cipher.ENCRYPT_MODE, key)

val text = Source.fromFile(file)

val list=text.toList

val blocks=list.grouped(501)

val iter=blocks.formatted()


val words=iter.getBytes

cipher.doFinal(words)
}

def main(args:Array[String]):Unit={
val publicKey=decodePublicKey("--4096bits RSA public keys--")

val cipher = encrypt("E:\\plaintext.txt",publicKey.get)

println("Cipher is "+  bytes2hex(cipher))

}
  }`
