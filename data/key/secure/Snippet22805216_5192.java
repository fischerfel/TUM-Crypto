//SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(raw), "AES");
