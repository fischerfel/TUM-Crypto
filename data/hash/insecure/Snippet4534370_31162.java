private void convertStringToSHA1()
{
        String sTimeStamp  = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS").format(new java.util.Date());
        String sStringToHash = String.format("%1$s\n%2$s", "Username",sTimeStamp);

        MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();

        cript.update(sStringToHash.getBytes("utf-8"));
        sStringToHash = new BigInteger(1, cript.digest()).toString(16);
}
