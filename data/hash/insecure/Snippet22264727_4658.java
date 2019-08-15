public static String encrypteInput(String input) {
        String output = null;
        input = input + ((int) Math.random()) % 1000;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            output = new String(md5.digest(input.getBytes()));
        } catch (Exception e) {
            output = "";
        }
        return output;
}
