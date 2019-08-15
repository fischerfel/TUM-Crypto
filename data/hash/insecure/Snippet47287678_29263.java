    MessageDigest   hash = MessageDigest.getInstance("SHA1");
    byte[] aShared = hash.digest(aKeyAgree.generateSecret());
    byte[] bShared = hash.digest(bKeyAgree.generateSecret());
    System.out.println(Arrays.toString(aKeyAgree.generateSecret()));
