import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

import play.api.inject.guice._
import play.api.{ApplicationLoader, Configuration}

class ApplicationLoaderConfig extends GuiceApplicationLoader() {

  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {

    // Decrypt secrets
    val decryptedConfig = context.initialConfiguration ++
      Configuration("config.to.descrypt.1" -> decryptDES(context.initialConfiguration.getString("config.to.descrypt.1").get)) ++
      Configuration("config.to.descrypt.2" -> decryptDES(context.initialConfiguration.getString("config.to.descrypt.2").get))

    initialBuilder
      .in(context.environment)
      .loadConfig(decryptedConfig)
      .overrides(overrides(context): _*)
  }

  private def decryptDES(secret: String): String = {
    val key = "12345678"
    val skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "DES")

    val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, skeySpec)

    new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(secret)))
  }
}
