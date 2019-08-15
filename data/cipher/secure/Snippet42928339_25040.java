Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
c.init(Cipher.ENCRYPT_MODE, getSecretKey(context),new GCMParameterSpec(128,Base64.decode(myGeneratedIV, Base64.DEFAULT)));
