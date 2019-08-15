String passForTheKey = ks2.getKey(keyAlias, keyPass.toCharArray()).toString();
KeySpec key = new PBEKeySpec(passForTheKey.toCharArray());
SecretKey sKey2 = skFactory.generateSecret(key);
