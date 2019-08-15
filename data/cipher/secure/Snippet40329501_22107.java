Security.addProvider(new BouncyCastleProvider());

Cipher rsa = Cipher.getInstance("RSA/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
rsa.init(Cipher.DECRYPT_MODE, kp.getPrivate());
byte[] signatureUsingCipher = rsa.doFinal("owlstead"
        .getBytes(StandardCharsets.UTF_8));
System.out.println(Hex.toHexString(signatureUsingCipher));
