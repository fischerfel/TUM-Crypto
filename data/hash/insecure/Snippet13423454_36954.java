public String getComputerID(){
    InetAddress ip = InetAddress.getLocalHost();
    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
    byte[] mac = network.getHardwareAddress();
    String sn = getSerialNumber("C");
    String cpuId = getMotherboardSN();
    return MD5(mac + sn + cpuId);
}


public String MD5(String md5) {
try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
    }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
}

public String getSerialNumber(String drive) {
String result = "";
    try {
    File file = File.createTempFile("realhowto",".vbs");
    file.deleteOnExit();
    FileWriter fw = new java.io.FileWriter(file);

  StringBufferring vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                +"Set colDrives = objFSO.Drives\n"
                +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                +"Wscript.Echo objDrive.SerialNumber";  // see note
    fw.write(vbs);
    FileWriter.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input =
        new BufferedReader
        (new InputStreamReader(p.getInputStream()));
    String line;
    while ((line = input.readLine()) != null) {
        result += line;
    }
    input.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return result.trim();
}

public String getMotherboardSN() {
String result = "";
try {
File file = File.createTempFile("realhowto",".vbs");
file.deleteOnExit();
FileWriter fw = new java.io.FileWriter(file);

String vbs =
"Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
+ "Set colItems = objWMIService.ExecQuery _ \n"
+ " (\"Select * from Win32_BaseBoard\") \n"
+ "For Each objItem in colItems \n"
+ " Wscript.Echo objItem.SerialNumber \n"
+ " exit for ' do the first cpu only! \n"
+ "Next \n";

fw.write(vbs);
fw.close();
Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
BufferedReader input =
new BufferedReader
(new InputStreamReader(p.getInputStream()));
String line;
while ((line = input.readLine()) != null) {
result += line;
}
input.close();
}
catch(Exception e){
e.printStackTrace();
}
return result.trim();
}
