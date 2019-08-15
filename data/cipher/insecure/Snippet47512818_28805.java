    Security.addProvider(new BouncyCastleProvider());
    SecretKey sKey = new SecretKeySpec(Hex.decode("8ff6d560edfd395f3a1cbee18bcce3ac"), "Twofish");
    Cipher cipher = Cipher.getInstance("Twofish/ECB/NoPadding","BC");
