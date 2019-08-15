private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
private var SECRET_KEY_FAC_ALGORITHM = "PBKDF2WithHmacSHA1"
private val SECRET_KEY_SPEC_ALGORITHM = "AES"

private val cipher = Cipher.getInstance(TRANSFORMATION)
private val random = SecureRandom()

private val KEY_BITS_LENGTH = 256
private val IV_BYTES_LENGTH = cipher.blockSize
private val SALT_BYTES_LENGTH = KEY_BITS_LENGTH / 8
private val ITERATIONS = 10000
