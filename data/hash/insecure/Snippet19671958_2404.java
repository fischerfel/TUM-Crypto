import netscape.javascript.JSObject;
import java.applet.*;
import java.security.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.math.BigInteger;

public class start extends Applet {

    private static final long serialVersionUID = 1L;
    JSObject win;

    public void main() {
        win = (JSObject)JSObject.getWindow(this);
    }

    public void init() {
        InetAddress ip;
        String hashtext = "a";
        try {
            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));    
            }

                    try {
                            String plaintext = sb.toString();
                            MessageDigest m = MessageDigest.getInstance("MD5");
                            m.reset();
                            m.update(plaintext.getBytes());
                            byte[] digest = m.digest();
                            BigInteger bigInt = new BigInteger(1,digest);
                            hashtext = bigInt.toString(16);
                            while(hashtext.length() < 32 ){
                                hashtext = "0"+hashtext;
                            }
                    } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                    }

                    try {
                        win.eval("alert('123');");
                    } catch (NullPointerException e) {
                            e.printStackTrace();
                    }

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }

    }

}
