String password = "pass";
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes(), 0, password.length());
        System.out.println(new BigInteger(1, md.digest()).toString(32));

        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
