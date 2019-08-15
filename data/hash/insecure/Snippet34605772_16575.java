    MessageDigest md = MessageDigest.getInstance("SHA1");
       md.update(password.getBytes("UTF8"));
 //      Base64 base64 = new Base64();
       byte[] hash = md.digest();
       String sshaPassword = new String(Base64.encodeBase64(hash));
       return "{SSHA}" + sshaPassword;
