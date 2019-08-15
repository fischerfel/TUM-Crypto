public class Security {

public static void main(String[] args) throws NoSuchAlgorithmException,UnknownHostException, SocketException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, UnsupportedEncodingException
{
    String algorithm = "DESede";
    ObjectCrypter obj = null;
    Key symKey = KeyGenerator.getInstance(algorithm).generateKey();
    Cipher c = Cipher.getInstance(algorithm);
    String serial = getSerialNumber();
    String mac = getMacAddress();


    serial = getSerialNumber();
    mac = getMacAddress();
    byte[] encryptionBytes1 = obj.encryptF(serial,symKey,c);
    System.out.println("Serial: " + serial);
    System.out.println("Encr: " + encryptionBytes1);
    System.out.println("Decr: " + obj.decryptF(encryptionBytes1, symKey, c));
    byte[] encryptionBytes2 = obj.encryptF(mac,symKey,c);
    System.out.println("MAC: " + mac);
    System.out.println("Encr: " + encryptionBytes2);
    System.out.println("Decr: " + obj.decryptF(encryptionBytes2, symKey, c));


    System.out.println("EncryptionBytes: "+encryptionBytes1);
    System.out.println("Array EncBytes: "+Arrays.toString(encryptionBytes1));
    String ts = encryptionBytes1.toString();
    System.out.println("TesString: "+ts);
    System.out.println("TesString ConvBytes: "+ts.getBytes("ISO-8859-1"));
    System.out.println("TesString ConvBytes2: "+ts.getBytes("ISO-8859-1"));
    System.out.println("ts array: "+Arrays.toString(ts.getBytes("ISO-8859-1")));
    byte[] tsec = ts.getBytes("ISO-8859-1");
    System.out.println("tsec array: "+Arrays.toString(tsec));
    System.out.println("esTrEncrypt: "+tsec);
    System.out.println("esTrEncryptBytes1: "+tsec.toString().getBytes("ISO-8859-1"));
    System.out.println("esTRarray1: "+Arrays.toString(tsec));
    System.out.println("esTrEncryptBytes2: "+tsec.toString().getBytes("ISO-8859-1"));
    System.out.println("esTRarray1: "+Arrays.toString(tsec));
    System.out.println("esTrEncryptBytes3: "+tsec.toString().getBytes("ISO-8859-1"));
    System.out.println("esTRarray1: "+Arrays.toString(tsec));
    String decoded = new String(encryptionBytes1, "ISO-8859-1");
    System.out.println("Decoded: "+decoded);
    byte[] encoded = decoded.getBytes("ISO-8859-1");
    System.out.println("Encoded: "+encoded);
    System.out.println("ArrayEncoded: "+Arrays.toString(encoded));
    String decrypted = obj.decryptF(encoded, symKey, c);
    System.out.println("decrypted: "+decrypted);

    serial = getSerialNumber();
    mac = getMacAddress();
    byte[] encryptionBytes12 = obj.encryptF(serial,symKey,c);
    System.out.println("Serial: " + serial);
    System.out.println("Encr: " + encryptionBytes12);
    System.out.println("Decr: " + obj.decryptF(encryptionBytes1, symKey, c));
    byte[] encryptionBytes22 = obj.encryptF(mac,symKey,c);
    System.out.println("MAC: " + mac);
    System.out.println("Encr: " + encryptionBytes22);
    System.out.println("Decr: " + obj.decryptF(encryptionBytes2, symKey, c));
}//end test
public static String generateData() throws NoSuchAlgorithmException, NoSuchPaddingException, UnknownHostException, SocketException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
{
    String part1 = null, part2 = null;
    String algorithm = "DESede";
    ObjectCrypter obj = null;
    Key symKey = KeyGenerator.getInstance(algorithm).generateKey();
    Cipher c = Cipher.getInstance(algorithm);
    String serial = getSerialNumber();
    String mac = getMacAddress();
    byte[] encryptionBytes = obj.encryptF(serial, symKey, c);
    part1 = encryptionBytes.toString();
    byte[] encryptionBytes2 = obj.encryptF(mac, symKey, c);
    part2 = encryptionBytes2.toString();
    part1 = sliceString(part1);
    part2 = sliceString(part2);
    return part1+part2;
}//end generateData


public static boolean checkLicense(String license) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnknownHostException, SocketException, UnsupportedEncodingException
{
    String part1 = null, part2 = null;
    String genSerial = null, genMac = null;
    if (license.length() == 16)
    {
        part1 = "[B@" + license.substring(0, 8);
        part2 = "[B@" + license.substring(8, license.length());
    }//end if
    else if (license.length() == 15)
    {
        part1 = "[B@" + license.substring(0, 7);
        part2 = "[B@" + license.substring(7, license.length());
    }//end if
    else
    {
        return false;
    }//end else


    byte[] bpart1 = part1.getBytes("ISO-8859-1");
    byte[] bpart2 = part2.getBytes("ISO-8859-1");

    System.out.println("bytes: "+bpart1 + "\t" + bpart2);

    System.out.println("parts: "+part1 + "\t" + part2);
    String algorithm = "DESede";
    ObjectCrypter obj = null;
    Key symKey = KeyGenerator.getInstance(algorithm).generateKey();
    Cipher c = Cipher.getInstance(algorithm);
    genSerial = sliceString(obj.decryptF(bpart1, symKey, c));
    genMac = sliceString(obj.decryptF(bpart2, symKey, c));
    System.out.println(genSerial + "\t" + genMac);
    System.out.println(getSerialNumber() + "\t" + getMacAddress());
    if (genSerial == getSerialNumber() && genMac == getMacAddress())
    {
        return true;
    }//end if
    else
    {
        return false;
    }//end else
}//end checkLicense
public static String sliceString(String arg)
{
    return arg.substring(3);
}//end sliceString

public static String getSerialNumber()
{
    String output = "";
    try 
    { 
        Process p=Runtime.getRuntime().exec("wmic baseboard get serialnumber"); 
        //p.waitFor(); 
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line = "";
        int index = 0;
        while((line = reader.readLine()) != null)
        {
            if (line.length() > 0)
            {
                output = line;
            }//end if
        }//end while
    } 
    catch(IOException e1) {}
    return output;
}//end extractMBSerialNumber

public static String getMacAddress() throws UnknownHostException, SocketException
{
    InetAddress ip;
    ip = InetAddress.getLocalHost();

    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
    byte[] mac = network.getHardwareAddress();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
    }
    return sb.toString();
}
