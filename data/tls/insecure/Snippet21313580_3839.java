public class APNSHelper
{
private static SSLContext sslContext;
private static SSLSocketFactory sslSocketFactory;
private static SSLSocket sslSocket; 
private static DataOutputStream dataOutputStream;

private static final String KEYSTORE_TYPE = "PKCS12";
private static final String KEY_ALGORITHM = "sunx509";
private final static byte COMMAND = 0;

private static long lastSucessfulSocketUsageMillis = 0L;

private static void initSSL() throws Exception
{
    Logger.debug("ApnsHelper.initSSL() begin");
    try
    {
        FileInputStream stream = new FileInputStream(Constants.IOS_CERTIFICATE_PATH);
        final KeyStore lks = KeyStore.getInstance(KEYSTORE_TYPE);
        lks.load(stream, Constants.IOS_CERTIFICATE_PASSWORD.toCharArray());
        final KeyManagerFactory lkmf = KeyManagerFactory.getInstance(KEY_ALGORITHM);
        lkmf.init(lks, Constants.IOS_CERTIFICATE_PASSWORD.toCharArray());
        final TrustManagerFactory ltmf = TrustManagerFactory.getInstance(KEY_ALGORITHM);
        ltmf.init((KeyStore)null);
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(lkmf.getKeyManagers(), ltmf.getTrustManagers(), null);
        sslSocketFactory = sslContext.getSocketFactory();
    }
    catch(Exception e)
    {
        Logger.error("ApnsHelper.initSSL: " + e);
        throw e;
    }
    Logger.debug("ApnsHelper.initSSL() end");
}

private static Socket getSocket() throws Exception
{
    Logger.debug("ApnsHelper.getSocket() begin");

    if (sslSocketFactory == null) initSSL();

    if (sslSocket != null)
    {
        try
        {
            dataOutputStream.close();
            sslSocket.close();
        }
        catch(Exception e)
        {
            Logger.error("ApnsHelper.getSocket(): closing sslSocket: " + e);
        }
        dataOutputStream = null;
        sslSocket = null;
    }

    try
    {
        Socket infraSocket = null;
        if (Constants.IOS_PUSH_SOCKS_PROXY_HOST != null && Constants.IOS_PUSH_SOCKS_PROXY_HOST.length() > 0)
        {
            Logger.debug("ApnsHelper.getSocket(): with SOCKS PROXY=" + Constants.IOS_PUSH_SOCKS_PROXY_HOST + ":" + Constants.IOS_PUSH_SOCKS_PROXY_PORT);

            InetSocketAddress proxyAddr = new InetSocketAddress(Constants.IOS_PUSH_SOCKS_PROXY_HOST, Constants.IOS_PUSH_SOCKS_PROXY_PORT);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
            infraSocket = new Socket(proxy);
        }
        else
        {
            Logger.debug("ApnsHelper.getSocket(): NO proxy");

            // NO SOCKS version
            infraSocket = new Socket();
        }

        InetSocketAddress targetAddr = new InetSocketAddress(Constants.IOS_PUSH_GATEWAY_HOST, Constants.IOS_PUSH_GATEWAY_PORT);
        infraSocket.connect(targetAddr);
        sslSocket = (SSLSocket) sslSocketFactory.createSocket(infraSocket, Constants.IOS_PUSH_GATEWAY_HOST, Constants.IOS_PUSH_GATEWAY_PORT, true);

        Logger.debug("ApnsHelper.getSocket(): starting SSL handshake");
        sslSocket.startHandshake();
        Logger.debug("ApnsHelper.getSocket(): ended SSL handshake");
    }
    catch(Exception e)
    {
        Logger.error("ApnsHelper.getSocket(): " + e);
        throw e;
    }

    Logger.debug("ApnsHelper.getSocket() end");

    return sslSocket;
}

private static int hexValue(final char c) throws Exception
{
    if ('0' <= c && c <= '9') return (c - '0');
    if ('a' <= c && c <= 'f') return (c - 'a') + 10;
    if ('A' <= c && c <= 'F') return (c - 'A') + 10;

    Logger.error("Invalid hex char='" + c + "'");
    throw new Exception("Invalid hex char='" + c + "'");
}

public static synchronized void apnsPush(String mobileUIDID, String payLoad)
{
    Logger.debug("ApnsHelper.apnsPush(mobileUIDID=" + mobileUIDID + ", payLoad=" + payLoad+ ") begin");

    int ltries = 0;
    while( ltries < 3)
    {
        try
        {
            if (sslSocket == null || lastSucessfulSocketUsageMillis + Constants.IOS_PUSH_STALLED_MAX_TIME < System.currentTimeMillis())
            {
                getSocket();
            }
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(sslSocket.getOutputStream()));

            byte[] lpayLoad = payLoad.getBytes("UTF-8");
            final byte[] deviceId = new byte[mobileUIDID.length() / 2];
            for (int i = 0; i < deviceId.length; i++) deviceId[i] = (byte) ((hexValue(mobileUIDID.charAt(2*i)) * 16 + hexValue(mobileUIDID.charAt(2*i + 1))));

            dataOutputStream.writeByte(COMMAND);
            dataOutputStream.writeShort(deviceId.length);
            dataOutputStream.write(deviceId);
            dataOutputStream.writeShort(lpayLoad.length);
            dataOutputStream.write(lpayLoad);

            dataOutputStream.flush();
            lastSucessfulSocketUsageMillis = System.currentTimeMillis();
            Logger.debug("ApnsHelper.apnsPush(): sent message, mobileUIDID=" + mobileUIDID + ", payLoad=" + payLoad);
            break;
        }
        catch(Exception e)
        {
            Logger.error("ApnsHelper.apnsPush(): " + e);
            ltries++;
        }
    }
    Logger.debug("ApnsHelper.apnsPush(mobileUIDID=" + mobileUIDID + ", payLoad=" + payLoad+ ") end");
}
