String encryptedData = "C:\\EncryptedDataFile.data";
FileInputStream fis = null;
File file = new File(encryptedData);

//Convert file into array of bytes
byte[] encryptedDataBytes = new byte[(int) file.length()];

try 
{           
    // Read in array of bytes
    fis = new FileInputStream(file);
    fis.read(encryptedDataBytes);
    fis.close();     
} 
catch (FileNotFoundException ex) 
{
    ex.printStackTrace();
} 
catch (IOException ex) 
{
    ex.printStackTrace();
} 

// AES Key
byte[] decodedKey = Base64.getDecoder().decode("50rofsdb0TnQAQCb702wKz8m6XQeLNj6lamEvivKsh8=");

// decode the base64 encoded string
SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); // rebuild key using SecretKeySpec

// IV
byte[] initVecBytes = Base64.getDecoder().decode("OUXLZq4SpyhzNGIei0nerA==");

// Decrypt the cipher text        
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
IvParameterSpec ivParameterSpec = new IvParameterSpec(initVecBytes);
cipher.init(Cipher.DECRYPT_MODE, originalKey, ivParameterSpec);
byte[] original = cipher.doFinal(encryptedDataBytes);
String s = new String(original);
System.out.println(s);
