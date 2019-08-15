//Grab IV from message
byte[] iv = new byte[ivLength];
System.arraycopy(encryptedMessage, nonSecretPayloadLength, iv, 0, iv.length);

Cipher aes = Cipher.getInstance("AES/CBC/PKCS7Padding");
// MISSING: create IvParameterSpec
IvParameterSpec ivSpec = new IvParameterSpec(iv);
iv = ivSpec.getIV();

aes.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivSpec);

//byte[] decoded = aes.doFinal(Base64.decodeBase64(encryptedMessage));

ByteArrayOutputStream decrypterStream = new ByteArrayOutputStream();
DataOutputStream binaryWriter = new DataOutputStream(decrypterStream);

        binaryWriter.write(
                encryptedMessage,
                nonSecretPayloadLength + iv.length,
                encryptedMessage.length - nonSecretPayloadLength - iv.length - sentTag.length
        );
