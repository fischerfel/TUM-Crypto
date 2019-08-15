   MessageDigest digest = MessageDigest.getInstance("SHA-1");
   digest.reset();
   digest.update(salt);
   byte[] input = digest.digest(password.getBytes("UTF-8"));
