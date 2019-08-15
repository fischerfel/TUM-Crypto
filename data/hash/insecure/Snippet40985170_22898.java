public static void main(String args[]) throws Exception {

    final Runtime rt = Runtime.getRuntime();
    System.out.println("mem " + (rt.totalMemory() - rt.freeMemory())
            / (1024 * 1024) + " Mb");

    long time = System.currentTimeMillis();
    // windows command: cd to t:\ and run recursive dir
    Process p = rt.exec("cmd /c \"t: & dir /s /b   > filelist.txt\"");
    if (p.waitFor() != 0)
        throw new Exception("command has failed");
    System.out.println("done executing shell, took "
            + (System.currentTimeMillis() - time) + "ms");
    System.out.println();

    File f = new File("T:/filelist.txt");

    // load into hash set
    time = System.currentTimeMillis();
    Set<String> fileNames = new HashSet<String>(500000);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(f), StandardCharsets.UTF_8),
            50 * 1024 * 1024)) {
        for (String line = reader.readLine(); line != null; line = reader
                .readLine()) {
            fileNames.add(line);
        }
    }
    System.out.println(fileNames.size() + " file names loaded took "
            + (System.currentTimeMillis() - time) + "ms");
    System.gc();
    System.out.println("mem " + (rt.totalMemory() - rt.freeMemory())
            / (1024 * 1024) + " Mb");

    time = System.currentTimeMillis();
    // check files
    for (int i = 0; i < 70_000; i++) {
        StringBuilder fileToCheck = new StringBuilder();
        while (fileToCheck.length() < 256)
            fileToCheck.append(Double.toString(Math.random()));
        if (fileNames.contains(fileToCheck))
            System.out.println("to prevent optimization, never executes");
    }
    System.out.println();
    System.out.println("hash set 70K checks took "
            + (System.currentTimeMillis() - time) + "ms");
    System.gc();
    System.out.println("mem " + (rt.totalMemory() - rt.freeMemory())
            / (1024 * 1024) + " Mb");

    // Test memory/performance with MD5 hash set approach instead of full
    // names
    time = System.currentTimeMillis();
    Set<String> nameHashes = new HashSet<String>(50000);
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    for (String name : fileNames) {
        String nameMd5 = new String(md5.digest(name
                .getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        nameHashes.add(nameMd5);
    }
    System.out.println();
    System.out.println(fileNames.size() + " md5 hashes created, took "
            + (System.currentTimeMillis() - time) + "ms");
    fileNames.clear();
    fileNames = null;
    System.gc();
    Thread.sleep(100);
    System.gc();
    System.out.println("mem " + (rt.totalMemory() - rt.freeMemory())
            / (1024 * 1024) + " Mb");

    time = System.currentTimeMillis();
    // check files
    for (int i = 0; i < 70_000; i++) {
        StringBuilder fileToCheck = new StringBuilder();
        while (fileToCheck.length() < 256)
            fileToCheck.append(Double.toString(Math.random()));
        String md5ToCheck = new String(md5.digest(fileToCheck.toString()
                .getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        if (nameHashes.contains(md5ToCheck))
            System.out.println("to prevent optimization, never executes");
    }
    System.out.println("md5 hash set 70K checks took "
            + (System.currentTimeMillis() - time) + "ms");
    System.gc();
    System.out.println("mem " + (rt.totalMemory() - rt.freeMemory())
            / (1024 * 1024) + " Mb");
}
