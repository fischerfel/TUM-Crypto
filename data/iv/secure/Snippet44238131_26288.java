KeyStore ks = KeyStore.getInstance("PKCS11", provider);
ks.load(null, password);
Key key = ks.getKey(keyId, null);
IvParameterSpec paramSpec = new IvParameterSpec(iv);
AlgorithmParameters algParams = AlgorithmParameters.getInstance("AES");
algParams.init(paramSpec);
Cipher ci = Cipher.getInstance("AES/CBC/NoPadding", provider);
ci.init(Cipher.DECRYPT_MODE, key, algParams);
ci.doFinal(dataToDecipher);
