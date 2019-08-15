        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
        }

        md.update("string".getBytes());
        byte[] digest = md.digest();
        System.out.println(digest);

        md.reset();

        md.update("string".getBytes());
        byte[] digest2 = md.digest();
        System.out.println(digest2);
