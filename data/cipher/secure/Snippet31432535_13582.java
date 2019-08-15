import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher

ext {
    KEY = "mysecretkey".padRight(16).getBytes("UTF-8")
    SERVERKEY = "serverkey"
    IV = "1234".padRight(16).getBytes("UTF-8")
    PROVIDER = "SunJCE"
}

task encIt << {
    SecretKeySpec key = new SecretKeySpec(KEY, "AES")
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", PROVIDER)
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV))
    def encBytes = cipher.doFinal(SERVERKEY.bytes)
    def out = file('obf.enc')
    out.delete()
    out << encBytes
}

task decIt << {
    def cipherText = file('obf.enc').bytes
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", PROVIDER)
    SecretKeySpec key = new SecretKeySpec(KEY, "AES")
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV))
    println new String(cipher.doFinal(cipherText), "UTF-8")
}
