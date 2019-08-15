public static void calculate(Context context) {

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");

        ZipInputStream fis = get(context);
        System.out.println("fis: " + fis);

        byte[] dataBytes = new byte[1024];

        int nread = 0; 
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Digest(in hex format):: " + sb.toString());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<mdbytes.length;i++) {
            String hex=Integer.toHexString(0xff & mdbytes[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Digest(in hex format):: " + hexString.toString());

        if(fis!=null){
            fis.close();
        }
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static ZipInputStream get(Context context){

      // Get the path to the apk container.
    String apkPath = context.getApplicationInfo().sourceDir;
    JarFile containerJar = null;

    try {

        // Open the apk container as a jar..
        containerJar = new JarFile(apkPath);

        // Look for the "classes.dex" entry inside the container.
        ZipEntry zzz = containerJar.getEntry("classes.dex");

        // If this entry is present in the jar container 
        if (zzz != null) {

            System.out.println("long " + zzz.getCrc());

             // Get an Input Stream for the "classes.dex" entry
            InputStream in = containerJar.getInputStream(zzz);

             ZipInputStream zin = new ZipInputStream(in);

             return zin;
        }   

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (containerJar != null)
            try {
                containerJar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    return null;
}
