Cipher oaepFromInit = Cipher.getInstance("RSA/ECB/OAEPPadding");
OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSpecified.DEFAULT);
oaepFromInit.init(Cipher.DECRYPT_MODE, privkey, oaepParams);
byte[] pt = oaepFromInit.doFinal(ct);
System.out.println(new String(pt, StandardCharsets.UTF_8));
