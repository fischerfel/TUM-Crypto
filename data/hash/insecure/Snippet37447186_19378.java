public class Main extends Applet {

private String ip;
private String macLocal;
private String md5;
private InetAddress ipLocal;

@Override
public void init() {
    super.init(); //To change body of generated methods, choose Tools | Templates.
}



public String returnIp(){
    try {
        ipLocal = InetAddress.getLocalHost();
        ip = ipLocal.getHostAddress();
    } catch (UnknownHostException ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }
    return ip;
}
public String returnMac(){
    try {
        ipLocal = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ipLocal);
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
    }
            macLocal = sb.toString();
    } catch (SocketException ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnknownHostException ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }
    return macLocal;
}
public String returnMD5(){
    try {
        MessageDigest m=MessageDigest.getInstance("MD5");
        String ipMaisMac = new StringBuffer(retornaIp()).reverse().toString() + new StringBuffer(retornaMac()).reverse().toString() ;
        m.update(ipMaisMac.getBytes(),0,ipMaisMac.length());
        BigInteger i = new BigInteger(1, m.digest()); 
        md5 = String.format("%1$032X", i); 
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }
    return md5;
}
