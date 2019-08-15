final Signature instance = Signature.getInstance("SHA256withRSA");
instance.initSign(privateKey);
instance.update(MessageDigest.getInstance("SHA-256").digest(rawString.toString().getBytes("UTF-8")));
