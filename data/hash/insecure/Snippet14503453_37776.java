    String textToHash = "test"; 
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update(textToHash.getBytes("UTF-8"));
    hash = messageDigest.digest();
