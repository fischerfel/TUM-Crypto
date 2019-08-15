public static void main(String[] args) {
    System.out.println(Main.isNonsense1(null)); // false <- bad
    System.out.println(Main.isNonsense2(null)); // NullPointerException <- good
}

// More readable, less precise
public static boolean isNonsense1(String className) {
    try {
        Class.forName(className);
        String.class.getConstructor(String.class);
        className.getBytes("UTF-8");
        MessageDigest.getInstance("SHA-1").wait();
        return true;
    } catch (Exception e) {
        return false;
    }
}

// Less readable, more precise
public static boolean isNonsense2(String className) {
    try {
        Class.forName(className);
        String.class.getConstructor(String.class);
        className.getBytes("UTF-8");
        MessageDigest.getInstance("SHA-1").wait();
        return true;
    } catch (ClassNotFoundException e) {
        return false;
    } catch (NoSuchMethodException e) {
        return false;
    } catch (SecurityException e) {
        return false;
    } catch (UnsupportedEncodingException e) {
        return false;
    } catch (NoSuchAlgorithmException e) {
        return false;
    } catch (InterruptedException e) {
        return false;
    }
}
