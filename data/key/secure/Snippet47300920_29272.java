public static void performEncryption(String baseFolderLocation) 
{       
    String key = "This is a secret";

    System.out.println("Started Doing Encrypting...");
    List<File> filenames = getAllfilesInFolder(baseFolderLocation, new ArrayList<File>());
    int processedFilesCount = 0;

    for (File file : filenames) {
        try {

            Crypto.fileProcessor(Cipher.ENCRYPT_MODE, key, file.getAbsoluteFile(), file.getAbsoluteFile());

        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        processedFilesCount++;
    }

    System.out.println("Ended Encrypting...");
}

static void fileProcessor(int cipherMode, String key, File inputFile, File outputFile) {
    try {
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static List<File> getAllfilesInFolder(String directoryName, ArrayList<File> files) {
    File directory = new File(directoryName);

    // get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList) 
    {
        if (file.isFile()) 
        {
            files.add(file);
        }
        else if (file.isDirectory()) 
        {
            getAllfilesInFolder(file.getAbsolutePath(), files);
        }
    }

    return files;
}
