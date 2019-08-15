scala> import javax.crypto.Cipher
scala> import javax.crypto.spec.SecretKeySpec
scala> val cipher = Cipher.getInstance("Blowfish")
scala> cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("0123456789abcdef".getBytes("utf-8"), "Blowfish"))
scala> javax.xml.bind.DatatypeConverter.printBase64Binary(cipher.doFinal("message".getBytes("utf-8")))
res7: String = K679Jz06jmc=
