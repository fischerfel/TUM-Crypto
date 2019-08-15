import javax.crypto.*
import javax.crypto.spec.*

def key1 = new SecretKeySpec("C67DDB0CE47D27FA".decodeHex(), "DES")
def key2 = new SecretKeySpec("F6F32ECA5C99E8AF".decodeHex(), "DES")
def plaintext = ("ff00" + "000000000000").decodeHex() // manually zero pad

def c1 = Cipher.getInstance("DES/CBC/NoPadding")
c1.init(Cipher.ENCRYPT_MODE, key1, new IvParameterSpec(new byte[8]))
def cipherText1 = c1.doFinal(plaintext) 

def c2 = Cipher.getInstance("DES/CBC/NoPadding")
c2.init(Cipher.DECRYPT_MODE, key2, new IvParameterSpec(new byte[8]))
def cipherText2 = c2.doFinal(cipherText1)

def c3 = Cipher.getInstance("DES/ECB/NoPadding")
c3.init(Cipher.ENCRYPT_MODE, key1)
def cipherText3 = c3.doFinal(cipherText2)

assert cipherText3.encodeHex().toString() == "4bc0479d7889cf8e"
