MessageDigest md = MessageDigest.getInstance("SHA-256");
random.setSeed(md.digest(myString.getBytes())
