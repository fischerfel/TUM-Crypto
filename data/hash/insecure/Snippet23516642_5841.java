public static String getTime()
{

    String localTime = "";

    Calendar cal = Calendar.getInstance();

    long mins = cal.getTimeInMillis()/10000;

    localTime = Long.toString(mins);
    System.out.println(localTime);
    byte [] digest = null;
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(localTime.getBytes());
        digest = md.digest();
        md.reset();

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return digest.toString();

}
