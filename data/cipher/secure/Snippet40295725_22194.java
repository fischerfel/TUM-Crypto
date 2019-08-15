import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets

FlowFile flowFile = session.get()

if (!flowFile) {
    return
}

try {
    // Get the raw values of the attributes
    String normalAttribute = flowFile.getAttribute('Normal Attribute')
    String sensitiveAttribute = flowFile.getAttribute('Sensitive Attribute')

    // Instantiate an encryption cipher
    // Lots of additional code could go here to generate a random key, derive a key from a password, read from a file or keyring, etc.
    String keyHex = "0123456789ABCDEFFEDCBA9876543210" // * 2 for 256-bit encryption
    SecretKey key = new SecretKeySpec(keyHex.getBytes(StandardCharsets.UTF_8), "AES")
    IvParameterSpec iv = new IvParameterSpec(keyHex[0..&lt;16].getBytes(StandardCharsets.UTF_8))

    Cipher aesGcmEncCipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
    aesGcmEncCipher.init(Cipher.ENCRYPT_MODE, key, iv)

    String encryptedNormalAttribute = Base64.encoder.encodeToString(aesGcmEncCipher.doFinal(normalAttribute.bytes))
    String encryptedSensitiveAttribute = Base64.encoder.encodeToString(aesGcmEncCipher.doFinal(sensitiveAttribute.bytes))

    // Add a new attribute with the encrypted normal attribute
    flowFile = session.putAttribute(flowFile, 'Normal Attribute (encrypted)', encryptedNormalAttribute)

    // Replace the sensitive attribute inline with the cipher text
    flowFile = session.putAttribute(flowFile, 'Sensitive Attribute', encryptedSensitiveAttribute)
    session.transfer(flowFile, REL_SUCCESS)
} catch (Exception e) {
    log.error("There was an error encrypting the attributes: ${e.getMessage()}")
    session.transfer(flowFile, REL_FAILURE)
}
