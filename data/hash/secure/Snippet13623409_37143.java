    // NEEDED if you are using a Java version without SHA-256    
    Security.addProvider(new BouncyCastleProvider());

    // then go as usual 
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    String text = "my string...";
    md.update(text.getBytes("UTF-8")); // or UTF-16 if needed
    byte[] digest = md.digest();
