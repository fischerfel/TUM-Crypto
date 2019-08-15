    KeySpec keySpec = new PBEKeySpec(EncryptionSettings.password, EncryptionSettings.salt, EncryptionSettings.hashingCount, EncryptionSettings.encryptionBitCount);
    SecretKey tmpKey = null;

    try
    {
        tmpKey = SecretKeyFactory.getInstance(EncryptionSettings.hashingAlgorithm).generateSecret(keySpec);
    }
    catch (final InvalidKeySpecException e)
    {
        Console.writeFatalError("Unable to generate key: invalid key specification");
    } 
    catch (final NoSuchAlgorithmException e)
    {
        Console.writeFatalError("Unable to generate key: encryption algorithm not supported - " + e.getMessage());
    }
