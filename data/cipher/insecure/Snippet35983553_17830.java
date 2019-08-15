public static void encryptMe(String  vidName)
{
    try {
        File rawData = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS), "HSDownloads");
        File fiss = new File(rawData, vidName);
        Log.d(TAG,"input file  "+fiss.toString());
        FileInputStream in = new FileInputStream(fiss);

        File encreptedFileDirectory = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS), "HSEncript");

        if(encreptedFileDirectory.exists())
        {

        }
        else
        {
            encreptedFileDirectory.mkdirs();
        }
        File outfile = new File(encreptedFileDirectory,vidName);
        OutputStream out= new FileOutputStream(outfile);

        byte[] skey = AppUtiles.generateKey("qwertyuiopasdfgh");
        SecretKeySpec key = new SecretKeySpec(skey, "AES");


        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);

        out = new CipherOutputStream(out, c);
        int count = 0;
        byte[] buffer = new byte[128*1024];
        while ((count = in.read(buffer)) >= 0)
        {
            out.write(buffer, 0, count);
            Log.d(TAG,"inside while loop encryptMe()"+(count = in.read(buffer)));
        }
       out.close();
    }
    catch(Exception e)
    {
        Log.d(TAG,"exception encryption >"+Log.getStackTraceString(e));
    }
    finally 
    {
        Log.d(TAG,"Inside finally   ");
    }
}
