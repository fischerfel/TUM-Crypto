import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import com.github.kondaurovdev.snippets.helper.{CryptoHelper, TryHelper}
import org.apache.commons.codec.binary.Base64

object Crypter {

  def apply(secret: String): Either[String, Crypter] = {
    for (
      s <- CryptoHelper.getSecretKey(secret).left.map(err => s"Can't get secretKeySpec: $err").right
    ) yield new Crypter(s)
  }

}

class Crypter(secretKey: SecretKeySpec) {

  def encrypt(input: String): Either[String, String] = {

    TryHelper.tryBlock({
      val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
      cipher.init(Cipher.ENCRYPT_MODE, secretKey)
      val encrypted = cipher.doFinal(input.getBytes("UTF-8"))
      Base64.encodeBase64String(encrypted)
    }, "Can't encrypt text")

  }

  //input = base64 encoded string
  def decrypt(input: String): Either[String, String] = {

    for (
      res <- {
        TryHelper.tryBlock({
          val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
          cipher.init(Cipher.DECRYPT_MODE, secretKey)
          val decrypted = cipher.doFinal(Base64.decodeBase64(input))
          new String(decrypted)
        }, "Error while decrypting")
      }.right
    ) yield res

  }


}

object CryptoHelper {

  def getSecretKey(myKey: String): Either[String, SecretKeySpec] = {
    TryHelper.tryBlock({
      var key = myKey.getBytes("UTF-8")
      val sha = MessageDigest.getInstance("SHA-1")
      key = sha.digest(key)
      key = util.Arrays.copyOf(key, 16) // use only first 128 bit
      new SecretKeySpec(key, "AES")
    }, "Can't build secretKey")
  }

}

object TryHelper {

  def tryBlock[R, E <: Throwable](block: => R, errPrefix: String = "", handle: errorPF = handlePF): Either[String, R] = {
    tryToEither(block).left.map(err => {
      var msg = err.getMessage
      if (errPrefix.nonEmpty) msg = s"$errPrefix: $msg"
      msg
    })
  }

}
