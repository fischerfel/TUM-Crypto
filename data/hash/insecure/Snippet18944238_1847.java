String someString = "qwe";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String checksum = new BigInteger(1, messageDigest.digest(someString
                .getBytes())).toString(16);
        System.out.println(checksum);
