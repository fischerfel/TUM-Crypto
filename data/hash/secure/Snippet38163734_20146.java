    // shared secret hashed to key from your example
    String shared_secret = "LyQnklSrxsk3Ch2+AHi9HoDW@//x1LwM123QP/ln";
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(shared_secret.getBytes("UTF-8"));

    JsonWebEncryption jwe = new JsonWebEncryption();

    // A256GCMKW for key wrap
    jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256GCMKW);

    // A256GCM for content encryption
    jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);

    // the key (from above)
    jwe.setKey(new SecretKeySpec(digest, "AES"));

    // whatever content you want to encrypt
    jwe.setPayload("some important content to be encrypted and integrity protected");

    // Produce the JWE compact serialization, which is where the actual encryption is done.
    // The JWE compact serialization consists of five base64url encoded parts
    // combined with a dot ('.') character in the general format of
    // <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication tag>
    String serializedJwe = jwe.getCompactSerialization();


    // Do something with the JWE. Like send it to some other party over the clouds
    // and through the interwebs.
    System.out.println("JWE compact serialization: " + serializedJwe);

    // That other party, the receiver, can then use JsonWebEncryption to decrypt the message.
    JsonWebEncryption receiverJwe = new JsonWebEncryption();

    // Set the compact serialization on new Json Web Encryption object
    receiverJwe.setCompactSerialization(serializedJwe);

    // Symmetric encryption, like we are doing here, requires that both parties have the same key.
    // The key will have had to have been securely exchanged out-of-band somehow.
    receiverJwe.setKey(new SecretKeySpec(digest, "AES"));

    // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
    String plaintext = receiverJwe.getPlaintextString();

    // And do whatever you need to do with the clear text message.
    System.out.println("plaintext: " + plaintext);
