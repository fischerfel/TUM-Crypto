public static boolean isNonsense1(String className) {
    if(slassname==null) throw new IllegalArgumentException("className must not be null");
    try {
        Class.forName(className);
        String.class.getConstructor(String.class);
        className.getBytes("UTF-8");
        MessageDigest.getInstance("SHA-1").wait();
        return true;
    } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException("provided class " + className + " not found");
    } catch (Exception e) {
        return false;
    }
}
