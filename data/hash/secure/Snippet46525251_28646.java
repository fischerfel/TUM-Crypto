import java.security.MessageDigest
import java.util.Base64

val md = MessageDigest.getInstance("sha-256")
val inputBytes: Array[Byte] = "foobar".getBytes("UTF-8")
md.update(inputBytes)
val sha256d: Array[Byte] = md.digest()
val base64d: Array[Byte] = Base64.getEncoder().encode(sha256d)
new String(base64d, "UTF-8")

// outputs the following

res5: String = w6uP8Tcg6K2QR905Rms8iXTlksL6OD1KOWBxTK7wxPI=
