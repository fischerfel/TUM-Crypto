public final class NetworkUtils {
public static final byte CONNECTION_OFFLINE = 1;
public static final byte CONNECTION_WIFI = 2;
public static final byte CONNECTION_ROAMING = 3;
public static final byte CONNECTION_SLOW = 4;
public static final byte CONNECTION_FAST = 5;

private static String sUserId;

private NetworkUtils() {}


/**
 * Check if the device is connected to the internet (mobile network or
 * WIFI).
 */
public static boolean isOnline(Context _context) {
    boolean online = false;

    TelephonyManager tmanager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
    if (tmanager != null) {
        if (tmanager.getDataState() == TelephonyManager.DATA_CONNECTED) {
            // Mobile network
            online = true;
        } else {
            // WIFI
            ConnectivityManager cmanager = (ConnectivityManager) _context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmanager != null) {
                NetworkInfo info = cmanager.getActiveNetworkInfo();
                if (info != null)
                    online = info.isConnected();
            }
        }
    }

    return online;
}

/**
 * Get the User Agent String in the format
 * AppName + AppVersion + Model + ReleaseVersion + Locale
 */
public static String getUserAgentString(Context _c, String _appName) {
    if(_appName == null)
        _appName = "";

    String agent = _appName + " " + BackendUtil.getAppVersionString(_c) + " (" + Build.MODEL + "; Android "
            + Build.VERSION.RELEASE + "; " + Locale.getDefault() + ")";

    if(agent.startsWith(" "))
        agent = agent.substring(1);

    return agent;
}

/**
 * Evaluate the current network connection and return the
 * corresponding type, e.g. CONNECTION_WIFI.
 */
public static byte getCurrentNetworkType(Context _context){
    NetworkInfo netInfo = ((ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

    if(netInfo == null)
        return CONNECTION_OFFLINE;

    if(netInfo.getType() == ConnectivityManager.TYPE_WIFI)
        return CONNECTION_WIFI;

    if(netInfo.isRoaming())
        return CONNECTION_ROAMING;

    if(!(netInfo.getType() == ConnectivityManager.TYPE_MOBILE 
            &&  (netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS 
              || netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA
              || netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA
              || netInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA 
              || netInfo.getSubtype() == 13 // NETWORK_TYPE_LTE
              || netInfo.getSubtype() == 15))) // NETWORK_TYPE_HSPAP  
         {

        return CONNECTION_SLOW;
    }

    return CONNECTION_FAST;
}


/**
 * Return the current IP adresse of the device or null if it could not be
 * found.
 */
public static String getIpAdress() {
    String result = null;
    try {
        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
            NetworkInterface iface = interfaces.nextElement();
            for (Enumeration<InetAddress> adresses = iface.getInetAddresses(); adresses.hasMoreElements();) {
                InetAddress ip = adresses.nextElement();
                if (!ip.isLoopbackAddress())
                    result = ip.getHostAddress();
            }
        }
    } catch (SocketException _e) {
        LL.error("Could not find device's ip adress");
    }
    return result;
}


/**
 * Return a MD5 hash of the device id.
 */
public static synchronized String getUserId(Context _context) {
    if (sUserId == null) {
        TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getDeviceId();
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(id.getBytes());
            byte[] digest = digester.digest();

            // Convert to hex string
            BigInteger converter = new BigInteger(1, digest);
            String md5 = converter.toString(16);
            while (md5.length() < 32)
                md5 = "0" + md5;
            sUserId = md5;
        } catch (NoSuchAlgorithmException _e) {
            LL.error("Could not find MD5");
        }
    }
    return sUserId;
}
