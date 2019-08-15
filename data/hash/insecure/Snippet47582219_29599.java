public static void main(String[] args) {

    try {
        File file1 = new File("D:\\tmp\\tests\\logs\\test.log");
        File file2 = new File("D:\\tmp\\tests\\logs\\test-cp.log");

        if (!file1.exists() || !file2.exists()) {
            System.out.println("One of the file not found.");
            return;
        }
        if (file1.length() != file2.length()) {
            System.out
                    .println("Files are not identical - not equal length.");
            return;
        }

        long f1Length = file1.length();
        long f2Length = file2.length();

        System.out.println("Check Digest method:");
        FileInputStream fis1 = new FileInputStream(file1);
        DigestInputStream dgStream1 = new DigestInputStream(fis1,
                MessageDigest.getInstance("MD5"));
        FileInputStream fis2 = new FileInputStream(file2);
        DigestInputStream dgStream2 = new DigestInputStream(fis2,
                MessageDigest.getInstance("MD5"));
        // most expensive is dgStream1.getMessageDigest() so do it only at last read
        dgStream1.on(false);
        dgStream2.on(false);

        long f1ReadTotal = 0;
        long f2ReadTotal = 0;

        long start = System.nanoTime();

        int read = 0;
        byte[] buff = new byte[1024 * 128];
        do {
            if ((f1Length - f1ReadTotal) < (1024 * 128)) {
                // last read 
                dgStream1.on(true);
            }
            read = dgStream1.read(buff);
            f1ReadTotal += read > 0 ? read : 0;
        } while (read > 0);

        read = 0;
        do {
            if ((f2Length - f2ReadTotal) < (1024 * 128)) {
                // last read
                dgStream2.on(true);
            }
            read = dgStream2.read(buff);
            f2ReadTotal += read > 0 ? read : 0;
        } while (read > 0);

        long runTime = System.nanoTime() - start;
        if (Arrays.equals(dgStream1.getMessageDigest().digest(), dgStream2
                .getMessageDigest().digest())) {
            System.out.println("Files are identical. completed in "
                    + (runTime / 1000000) + " ms. [" + runTime + " ns.]");
        } else {
            System.out.println("Files are not identical. completed in "
                    + (runTime / 1000000) + " ms. [" + runTime + " ns.]");
        }

        fis1.close();
        fis2.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

}
