import play.api.Logger

import sun.misc.BASE64Encoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.codahale.jerkson.Json._

object S3Policy {

  val AWS_ACCESS_KEY_ID = "<your access key ID goes here>"
  val AWS_SECRET = "<your secret goes here>"
  val AWS_ALGO = "HmacSHA1"

  val policy = {
    val policyMap = Map(
      "expiration" -> "2014-01-01T12:00:00.000Z'",
      "conditions" -> List(
        Map("bucket" -> "<your bucket goes here>"),
        Map("success_action_status" -> "201"),
        Map("acl" -> "public-read"),
        Array("starts-with", "$key", "uploads/"),
        Array("starts-with", "$Content-Type", ""),
        Array("starts-with", "$x-amz-meta-clientid", ""),
        List("content-length-range", 0, 5242880)
      )
    )
    val generated = generate(policyMap)
    Logger.debug("AWS S3 POLICY: "+generated)
    generated
  }

  val policyEncoded = (new BASE64Encoder()).encode(policy.getBytes("UTF-8")).replaceAll("\n", "").replaceAll("\r","")

  val policySignature = signAndBase64Encode(policyEncoded, AWS_ALGO)

  /**
   * method to sign an AWS request
   */
  def signAndBase64Encode(stringToSign: String, algo: String) = {
    try {
      val mac = Mac.getInstance(algo)
      mac.init(new SecretKeySpec(AWS_SECRET.getBytes("UTF-8"), algo))
      val signature = mac.doFinal(stringToSign.getBytes("UTF-8"))
      val encoded = (new BASE64Encoder()).encode(signature)

      Logger.debug("AWS S3 SIGNATURE: "+encoded)
      encoded

    } catch {
      case e => Logger.error("Unable to calculate a request signature: " + e.getMessage(), e); "ERRORSTRINGNOTCALCULATED"
    }
  }

}
