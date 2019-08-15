public static String getApkFileDigest(Context context) {
        String apkPath = context.getPackageCodePath();
        try {
            byte[] hashed= getDigest(new FileInputStream(apkPath), "SHA-256");
            return Base64.encodeToString(hashed, Base64.DEFAULT);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static final int BUFFER_SIZE = 2048;

    public static byte[] getDigest(InputStream in, String algorithm) throws Throwable {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try {
            DigestInputStream dis = new DigestInputStream(in, md);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (dis.read(buffer) != -1) {
            }
            dis.close();
        } finally {
            in.close();
        }
        return md.digest();
    }
