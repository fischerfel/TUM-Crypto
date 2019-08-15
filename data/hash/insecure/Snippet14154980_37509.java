import java.security.MessageDigest
import org.apache.commons.codec.binary.Hex
import org.specs2.mutable.Specification

class PasswordSpec extends Specification {

    "Password" should {
        "match" in {

            val password = "password"

            val hexEncodedPasswordHash = "0fed560a9928b50761ebec5aa97c815999e6def0"
            val hexEncodedSalt = "2ba345d5f2880fae25de9ec7a78d38ae"

            val charset = "UTF-8"
            val codec = new Hex(charset)

            val md = MessageDigest.getInstance("SHA-1")
            md.reset()
            md.update(password.getBytes(charset))
            md.update(codec.decode(hexEncodedSalt.getBytes(charset)))
            val hashBytes = md.digest()
            val hexEncodedHash = new String(codec.encode(hashBytes), charset)

            hexEncodedHash mustEqual(hexEncodedPasswordHash)

        }
    }
}
