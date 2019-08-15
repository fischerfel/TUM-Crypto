    Security.addProvider(new BouncyCastleProvider());

    byte[] key = Base64.getDecoder().decode("LLkRRMSAlD16lrfbRLdIELdj0U1+Uiap0ihQrRz7HSQ=");
    byte[] iv = Base64.getDecoder().decode("A23OFOSvsC4UyejA227d8g==");
    byte[] input = Base64.getDecoder().decode("D/e0UjAwBF+d8aVqZ0FpXA==");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
    byte[] output = cipher.doFinal(input);
    System.out.println("[" + new String(output) + "] - "+output.length);
