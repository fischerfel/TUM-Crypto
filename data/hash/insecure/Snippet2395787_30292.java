import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

// Written by Stuart Davidson, www.spedge.com
public class JSONComm 
{   
private final String JSON_URL = "http://api.facebook.com/restserver.php";
private final String fbSecretKey = "xxx";
private final String fbApiKey = "xxx";
private final String fbApiId = "xxx";

private int callId = 0;

public int getNextCall() { callId++; return callId; }
public String getApiKey() { return fbApiKey; }
public String getApiId() { return fbApiId; }

public String getRestURL(HashMap<String, String> args)
{
    String url = JSON_URL + "?";
    for(String arg : args.keySet()) { url = url + arg + "=" + args.get(arg) + "&"; }

    String sig = getMD5Hash(args);
    url = url + "sig=" + sig;

    return url;
}

public String getMD5Hash(HashMap<String, String> args)
{   
    String message = "";

    Vector<String> v = new Vector<String>(args.keySet());
    Collections.sort(v);
    Iterator<String> it = v.iterator();

    while(it.hasNext()) 
    { 
        String tmp = it.next();
        message = message + tmp + "=" + args.get(tmp);
    }

    message = message + fbSecretKey;

    try{
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = message.getBytes(); 
        m.update(data,0,data.length);
        BigInteger i = new BigInteger(1,m.digest());
        return String.format("%1$032X", i).toLowerCase();
    }
    catch(NoSuchAlgorithmException nsae){ return ""; }
}
}
