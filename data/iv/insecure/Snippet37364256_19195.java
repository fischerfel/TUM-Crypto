import javax.crypto.Cipher
import javax.crypto.spec._
import javax.xml.bind.DatatypeConverter

val cipher = Cipher.getInstance("Blowfish/CBC/NoPadding")

val key = new SecretKeySpec(DatatypeConverter.parseHexBinary("0123456789ABCDEF0123456789ABCDEF"), "Blowfish")

val specIv = new IvParameterSpec(DatatypeConverter.parseHexBinary("0000000000000000"))

cipher.init(Cipher.ENCRYPT_MODE, key, specIv)

val enc = cipher.doFinal("messages".getBytes("UTF-8"))

println(DatatypeConverter.printBase64Binary(enc))
