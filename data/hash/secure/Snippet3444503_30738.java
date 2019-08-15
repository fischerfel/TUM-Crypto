        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = "admin";
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        System.out.println(digest.toString());
