public class FileLogger {

//file and folder name
public static String LOG_FILE_NAME = "my_log.txt";
public static String LOG_FOLDER_NAME = "my_log_folder";

static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");

//My secret key, 16 bytes = 128 bit
static byte[] key = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};

//Appends to a log file, using encryption
public static void appendToLog(Context context, Object msg) {

    String msgStr;
    String timestamp = "t:" + formatter.format(new java.util.Date());

    msgStr = msg + "|" + timestamp + "\n";

    File sdcard = Environment.getExternalStorageDirectory();
    File dir = new File(sdcard.getAbsolutePath() + "/" + LOG_FOLDER_NAME);
    if (!dir.exists()) {
        dir.mkdir();
    }

    File encryptedFile = new File(dir, LOG_FILE_NAME);

    try {

        //Encryption using my key above defined
        Key secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] outputBytes = cipher.doFinal(msgStr.getBytes());

        //Writing to the file using append mode
        FileOutputStream outputStream = new FileOutputStream(encryptedFile, true);
        outputStream.write(outputBytes);
        outputStream.close();


    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

}

}
