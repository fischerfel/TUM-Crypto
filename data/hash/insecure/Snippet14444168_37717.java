        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] output = md.digest("welcome1".getBytes("UTF-16LE"));
        for(byte out: output){
            System.out.printf("%x", out);
        }
        System.out.println();
