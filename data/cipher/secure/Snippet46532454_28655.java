 // Decode the modified public key into a byte[]
            byte[] publicKeyByteArray = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));

            // Create a PublicKey from the byte array
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByteArray);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            // Get an instance of the Cipher and perform the encryption
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(ccNum.getBytes(StandardCharsets.UTF_8));

            // Get the encrypted value as a Base64-encoded String
            String encodeToStr = Base64.getEncoder().encodeToString(cipherText);

            // Print out the encoded, encrypted string
            System.out.println("Encrypted and Encoded String: " + encodeToStr);
