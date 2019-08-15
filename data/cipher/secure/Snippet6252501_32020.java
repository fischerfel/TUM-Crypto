final Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding", "SunJCE");
final SecretKey skeySpec = KeyGenerator.getInstance("AES")
        .generateKey();
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
System.out.println(Arrays.toString(cipher.doFinal(new byte[] { 0, 1, 2,
            3 })));
