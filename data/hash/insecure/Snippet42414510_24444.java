public static void compareChecksumForLocalAndHdfsFile(String sourceHdfsFilePath, String sourceLocalFilepath, Map<String, String> hdfsConfigMap)
        throws Exception {
    System.setProperty("HADOOP_USER_NAME", hdfsConfigMap.get(Constants.USERNAME));
    System.setProperty("hadoop.home.dir", "/tmp");
    Configuration hdfsConfig = new Configuration();
    hdfsConfig.set(Constants.USERNAME, hdfsConfigMap.get(Constants.USERNAME));
    hdfsConfig.set("fsURI", hdfsConfigMap.get("fsURI"));
    FileSystem hdfs = FileSystem.get(new URI(hdfsConfigMap.get("fsURI")), hdfsConfig);
    Path inputPath = new Path(hdfsConfigMap.get("fsURI") + "/" + sourceHdfsFilePath);
    InputStream is = hdfs.open(inputPath);
    String localChecksum = getMD5Checksum(new FileInputStream(sourceLocalFilepath));
    String hdfsChecksum = getMD5Checksum(is);
    if (null != hdfsChecksum || null != localChecksum) {
        System.out.println("HDFS Checksum : " + hdfsChecksum.toString() + "\t" + hdfsChecksum.length());
        System.out.println("Local Checksum : " + localChecksum.toString() + "\t" + localChecksum.length());

        if (hdfsChecksum.toString().equals(localChecksum.toString())) {
            System.out.println("Equal");
        } else {
            System.out.println("UnEqual");
        }
    } else {
        System.out.println("Null");
        System.out.println("HDFS : " + hdfsChecksum);
        System.out.println("Local : " + localChecksum);
    }
}

public static byte[] createChecksum(String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
}

// see this How-to for a faster way to convert
// a byte array to a HEX string
public static String getMD5Checksum(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    String result = "";

    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}
