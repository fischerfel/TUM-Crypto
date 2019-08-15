GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, skeySpec, gcmParameterSpec);
