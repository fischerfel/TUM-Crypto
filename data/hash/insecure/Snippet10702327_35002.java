public static boolean isNonsense2(String className) {
    try {
        Class.forName(className);
        String.class.getConstructor(String.class);
        className.getBytes("UTF-8");
        MessageDigest.getInstance("SHA-1").wait();
        return true;
    } catch (Exception e) {
        if (e instanceof ClassNotFoundException
                || e instanceof NoSuchMethodException
                || e instanceof SecurityException
                || e instanceof UnsupportedEncodingException
                || e instanceof NoSuchAlgorithmException
                || e instanceof InterruptedException) {
            return false;
        } else {
            throw e;
        }
    }
}
