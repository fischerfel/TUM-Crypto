public class PeerData {

    static public String Ip;

    static public InetAddress inetAddr;

    static SSLSocket SocTcpCtrl = null;
    static SSLSocketFactory sf = null;
    static TrustManager tm[] = { new PubKeyManager() };
    static SSLContext context = null;

    static InetSocketAddress socketAddress;
    static InputStream inStream;
    static OutputStream outStream;
    static Socket plainSoc = null;


    PeerData() {
        InitSsl();

        Data = new byte[128];
        PeerData.inetAddr = null;
    }

    @SuppressLint("TrulyRandom")
    static void InitSsl() {

        if( context == null ) {

            try {
                context = SSLContext.getInstance("TLSv1");  // TLSv1.2  TLSv1.1 TLSv1   TLS SSL     SSLv3
                DebugUtils.LOGD("SSL", String.format("Got context"));
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                //context.init(new KeyManager[] { km }, tm, new SecureRandom());
                context.init(null, tm, new SecureRandom());
                DebugUtils.LOGD("SSL", String.format("Context init"));
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            sf = context.getSocketFactory();
        }
    }

    void CreateAndConnect(InetAddress inetAddr) {

        int timeout = 10;
        int port = 1212;

        if( SocTcpCtrl != null ) {
            return;
        }

        this.inetAddr = inetAddr;

        Ip = inetAddr.getHostAddress().toString();

        DebugUtils.LOGD(TAG, String.format("Connecting to %s", Ip));

        SocketAddress sockAddr = new InetSocketAddress(Ip, port);

        plainSoc = new Socket();

        try {
            plainSoc.connect(sockAddr, timeout * 1000);
        } catch (IOException e2) {
            DebugUtils.LOGE(TAG, String.format("Failed connect"));
            SocTcpCtrl = null;
            return;
        }


        try {
            SocTcpCtrl = (SSLSocket) sf.createSocket(plainSoc, Ip, port, true);
        } catch (IOException e1) {

            try {
                plainSoc.close();
            } catch (IOException e) {
            }
            SocTcpCtrl = null;
            return;
        }


        try {
            SocTcpCtrl.setSoTimeout(timeout * 1000);
        } catch (SocketException e) {
            DebugUtils.LOGE(TAG, String.format("setSoTimeout failed0"));
            try {
                SocTcpCtrl.close();
            } catch (IOException e1) {
            }
            try {
                plainSoc.close();
                DebugUtils.LOGE(TAG, String.format("setSoTimeout failed2"));
            } catch (IOException e1) {
            }
            SocTcpCtrl = null;
            DebugUtils.LOGE(TAG, String.format("setSoTimeout failed3"));
            return;
        }

        try {
            inStream = SocTcpCtrl.getInputStream(); /////////   Throws exception on Marshmallow!!!! On Lollipop and below, it is fine.
        } catch (IOException e) {
            DebugUtils.LOGE(TAG, String.format("getInputStream failed %d", timeout));
            try {
                SocTcpCtrl.close();
            } catch (IOException e1) {
            }
            try {
                plainSoc.close();
            } catch (IOException e1) {
            }
            SocTcpCtrl = null;

            return;
        }

        try {
            outStream = SocTcpCtrl.getOutputStream();
        } catch (IOException e) {
            DebugUtils.LOGE(TAG, String.format("getInputStream failed %d", timeout));
            try {
                SocTcpCtrl.close();
            } catch (IOException e2) {
            }
            try {
                plainSoc.close();
            } catch (IOException e1) {
            }
            SocTcpCtrl = null;
            DebugUtils.LOGE(TAG, String.format("outStream failed"));
            return;
        }
    }
}
