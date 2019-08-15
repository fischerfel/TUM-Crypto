    File PATH = Environment.getExternalStorageDirectory();
    File DATAFILE = new File(PATH + "/stuff/content/" + FILE);

    Context context = MyApp.getAppContext();
    MessageDigest md = MessageDigest.getInstance("MD5");
    FileInputStream fis = new FileInputStream(DATAFILE);
    BufferedInputStream in = new BufferedInputStream(fis);

   // generate MD5
    byte[] dataBytes = new byte[1024];
    int byteCount;
    while ((byteCount = in.read(dataBytes)) > 0) {
        md.update(dataBytes, 0, byteCount);
    }
    byte[] digest = md.digest();

    // convert to readable string
    String MD5;
    StringBuffer hexString = new StringBuffer();
    for (int i=0; i<digest.length; i++)
        hexString.append(Integer.toHexString(0xFF & digest[i]));
    MD5 = hexString.toString();

    Log.i("GENERATED MD5", MD5);
