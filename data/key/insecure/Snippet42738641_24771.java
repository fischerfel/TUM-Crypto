    try {
        String english = "Rastapopoulos";
        String chinese = "桃草夹芥人蕉芥玉芥花荷子衣兰芥花";
        String transformationKey = "asdewqayxswedcvf";
        Key aesKey = new SecretKeySpec(transformationKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] encrypted1 = cipher.doFinal(english.getBytes("UTF-8"));
        String encoded1 = Base64.encodeBase64String(encrypted1);

        byte[] encrypted2 = cipher.doFinal(chinese.getBytes("UTF-8"));
        String encoded2 = Base64.encodeBase64String(encrypted2);

        System.out.println("Original length: " + english.length() + "\tEncrypted length: " + encoded1.length() + "\t" + encoded1);
        System.out.println("Original length: " + chinese.length() + "\tEncrypted length: " + encoded2.length() + "\t" + encoded2);
    } catch (Exception e) {
        e.printStackTrace();
    }
