import java.security.*

String nonce = '201703281329'
MessageDigest digest = MessageDigest.getInstance("SHA-1")
digest.update(nonce.getBytes("ASCII"))

byte[] passwordDigest = digest.digest() // byte[], not string

String hexString = passwordDigest.collect { String.format('%02x', it) }.join()

println hexString
