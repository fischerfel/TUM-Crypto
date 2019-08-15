import java.security.*
import javax.crypto.*
import javax.crypto.spec.*

public class Des {
    static encode = { String target ->
        def cipher = getCipher(Cipher.ENCRYPT_MODE)
        return cipher.doFinal(target.bytes).encodeBase64()
    }

    static decode = { String target ->
        println "DECODE TARGET :::: " + target
        def cipher = getCipher(Cipher.DECRYPT_MODE)
        def b64Char = target.decodeBase64()
        println "BASE64 CHAR :::: " + b64Char
        def ret = cipher.doFinal(b64Char)
        println "DECODE VALUE :::" + ret
        return ret
    }

    private static getCipher(mode) {
        def keySpec = new DESKeySpec(getKeySecretKey())
        def cipher = Cipher.getInstance("DES")
        def keyFactory = SecretKeyFactory.getInstance("DES")
        cipher.init(mode, keyFactory.generateSecret(keySpec))
        return cipher
    }

    private static getKeySecretKey() {
        "MySecret1234".getBytes("UTF-8")
    }

    static void main(args) {
        println encode("TheP4ssword")
    }
}
