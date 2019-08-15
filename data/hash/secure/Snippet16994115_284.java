    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest("ContrasenhaPassword".getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (int i: hash) {
            hexString.append(Integer.toHexString(0XFF & i));
        }
        String Hashed = new String(hexString);
        System.out.println(hexString);
        System.out.println(Hashed);
        // Below, MySQL Output for SHA2('ContrasenhaPassword',256)
        System.out.println("d17bf0da90f56b8fc627bac6523ffd284aa0d82c870e1a0428274de048f49d78");
        System.out.println(Hashed.equals(hexString));
        } catch (Exception e) {
        e.printStackTrace();
        }
