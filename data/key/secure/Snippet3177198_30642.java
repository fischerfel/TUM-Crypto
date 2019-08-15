      IvParameterSpec ivspec = new IvParameterSpec(this.iv); //this is already been converted from base64 to raw form.

Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM_MODE_PADDING, PROVIDER);
SecretKeySpec aeskeySpec = new SecretKeySpec(rawAesKey, AES);
aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec, ivspec);

return aesCipher.doFinal(rawEncryptedLicenseData);
