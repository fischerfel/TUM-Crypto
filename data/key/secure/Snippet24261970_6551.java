byte[] convertedKeyToByte = providedKey.getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(convertedKeyToByte , 0, convertedKeyToByte .length,
            AESalgorithm);
