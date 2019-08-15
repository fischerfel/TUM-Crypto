public Videoplanner(Context ctxt, String logTag,Application app) throws NoSuchAlgorithmException 
{
    super(ctxt, logTag);
    TelephonyManager telephonyManager = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
    MessageDigest digester = MessageDigest.getInstance("SHA-1");
    byte[] digest = digester.digest(telephonyManager.getDeviceId().getBytes());
    hashedID = (new BigInteger(1, digest)).toString(16);
    serviceName = hashedID;
    context = ctxt;
    appl = app;

}
