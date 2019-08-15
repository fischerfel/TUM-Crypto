Cipher cipher = Cipher.getInstance("RSA");
Key publicKey = KeyFactory.getInstance("RSA").generatePublic(
    new X509EncodedKeySpec(
        ("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAYQCaoQtG7ZOxDBkE" +
        "DHhw+4IPL1mR4MyXndS4lfp6STDJrVbE9TjB9TA9MMobM0z3RJZj9" +
        "oSpC+lt6uIy7aETQZ6EfuJ2ESKN/e8WWlIP6Wy6+LDYrKIkEuY1uf" +
        "VxHaHSA3E= pi@inevent.us").getBytes()
    )
);

cipher.init(Cipher.ENCRYPT_MODE, publicKey);

return new String(cipher.doFinal(message.getBytes()));  
