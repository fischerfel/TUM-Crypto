package com.example.android.streaming.streaming.rtsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Base64;
import android.util.Log;

import com.example.android.streaming.StreamingApp;
import com.example.android.streaming.streaming.Session;
import com.example.android.streaming.BuildConfig;

public class RtspSocket extends Socket {
    public static final int RTSP_HEADER_LENGTH = 4;
    public static final int RTP_HEADER_LENGTH = 12;
    public static final int MTU = 1400;

    public static final int PAYLOAD_OFFSET = RTSP_HEADER_LENGTH + RTP_HEADER_LENGTH;
    public static final int RTP_OFFSET = RTSP_HEADER_LENGTH;

    private ConcurrentHashMap<String, String> headerMap = new ConcurrentHashMap<String, String>();

    static private final String kCRLF = "\r\n";

    // RTSP request format strings
    static private final String kOptions = "OPTIONS %s RTSP/1.0\r\n";
    static private final String kDescribe = "DESCRIBE %s RTSP/1.0\r\n";
    static private final String kAnnounce = "ANNOUNCE %s RTSP/1.0\r\n";
    static private final String kSetupPublish = "SETUP %s/trackid=%d RTSP/1.0\r\n";
    @SuppressWarnings("unused")
    static private final String kSetupPlay = "SETUP %s/trackid=%d RTSP/1.0\r\n";
    static private final String kRecord = "RECORD %s RTSP/1.0\r\n";
    static private final String kPlay = "PLAY %s RTSP/1.0\r\n";
    static private final String kTeardown = "TEARDOWN %s RTSP/1.0\r\n";

    // RTSP header format strings
    static private final String kCseq = "Cseq: %d\r\n";
    static private final String kContentLength = "Content-Length: %d\r\n";
    static private final String kContentType = "Content-Type: %s\r\n";
    static private final String kTransport = "Transport: RTP/AVP/%s;unicast;mode=%s;%s\r\n";
    static private final String kSession = "Session: %s\r\n";
    static private final String kRange = "range: %s\r\n";
    static private final String kAccept = "Accept: %s\r\n";
    static private final String kAuthBasic = "Authorization: Basic %s\r\n";
    static private final String kAuthDigest = "Authorization: Digest username=\"%s\",realm=\"%s\",nonce=\"%s\",uri=\"%s\",response=\"%s\"\r\n";

    // RTSP header keys
    static private final String kSessionKey = "Session";
    static private final String kWWWAuthKey = "WWW-Authenticate";

    byte header[] = new byte[RTSP_MAX_HEADER + 1];
    static private final int RTSP_MAX_HEADER = 4095;
    static private final int RTSP_MAX_BODY = 4095;

    static private final int RTSP_RESP_ERR = -6;
    // static private final int RTSP_RESP_ERR_SESSION = -7;
    static public final int RTSP_OK = 200;
    static private final int RTSP_BAD_USER_PASS = 401;

    static private final int SOCK_ERR_READ = -5;

    /* Number of channels including control ones. */
    private int channelCount = 0;

    /* RTSP negotiation cmd seq counter */
    private int seq = 0;

    private String authentication = null;
    private String session = null;

    private String path = null;
    private String url = null;
    private String user = null;
    private String pass = null;
    private String sdp = null;

    private byte[] buffer = new byte[MTU];

    public RtspSocket() {
        super();
        try {
            setTcpNoDelay(true);
            setSoTimeout(60000);
        } catch (SocketException e) {
            Log.e(StreamingApp.TAG, "Failed to set socket params.");
        }
        buffer[RTSP_HEADER_LENGTH] = (byte) Integer.parseInt("10000000", 2);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public static final void setLong(byte[] buffer, long n, int begin, int end) {
        for (end--; end >= begin; end--) {
            buffer[end] = (byte) (n % 256);
            n >>= 8;
        }
    }

    public void setSequence(int seq) {
        setLong(buffer, seq, RTP_OFFSET + 2, RTP_OFFSET + 4);
    }

    public void setSSRC(int ssrc) {
        setLong(buffer, ssrc, RTP_OFFSET + 8, RTP_OFFSET + 12);
    }

    public void setPayload(int payload) {
        buffer[RTP_OFFSET + 1] = (byte) (payload & 0x7f);
    }

    public void setRtpTimestamp(long timestamp) {
        setLong(buffer, timestamp, RTP_OFFSET + 4, RTP_OFFSET + 8);
    }

    /** Sends the RTP packet over the network */
    private void send(int length, int stream) throws IOException {
        buffer[0] = '$';
        buffer[1] = (byte) stream;
        setLong(buffer, length, 2, 4);
        OutputStream s = getOutputStream();
        s.write(buffer, 0, length + RTSP_HEADER_LENGTH);
        s.flush();
    }

    public void sendReport(int length, int ssrc, int stream) throws IOException {
        setPayload(200);
        setLong(buffer, ssrc, RTP_OFFSET + 4, RTP_OFFSET + 8);
        send(length + RTP_HEADER_LENGTH, stream);
    }

    public void sendData(int length, int ssrc, int seq, int payload, int stream, boolean last) throws IOException {
        setSSRC(ssrc);
        setSequence(seq);
        setPayload(payload);
        buffer[RTP_OFFSET + 1] |= (((last ? 1 : 0) & 0x01) << 7);
        send(length + RTP_HEADER_LENGTH, stream);
    }

    public int getChannelCount() {
        return channelCount;
    }

    private void write(String request) throws IOException {
        try {
            String asci = new String(request.getBytes(), "US-ASCII");
            OutputStream out = getOutputStream();
            out.write(asci.getBytes());
        } catch (IOException e) {
            throw new IOException("Error writing to socket.");
        }
    }

    private String read() throws IOException {
        String response = null;
        try {
            InputStream in = getInputStream();
            int i = 0, len = 0, crlf_count = 0;
            boolean parsedHeader = false;

            for (; i < RTSP_MAX_BODY && !parsedHeader && len > -1; i++) {
                len = in.read(header, i, 1);
                if (header[i] == '\r' || header[i] == '\n') {
                    crlf_count++;
                    if (crlf_count == 4)
                        parsedHeader = true;
                } else {
                    crlf_count = 0;
                }
            }
            if (len != -1) {
                len = i;
                header[len] = '\0';
                response = new String(header, 0, len, "US-ASCII");
            }
        } catch (IOException e) {
            throw new IOException("Connection timed out. Check your network settings.");
        }
        return response;
    }

    private int parseResponse(String response) {
        String[] lines = response.split(kCRLF);
        String[] items = response.split(" ");
        String tempString, key, value;

        headerMap.clear();
        if (items.length < 2)
            return RTSP_RESP_ERR;
        int responseCode = RTSP_RESP_ERR;
        try {
            responseCode = Integer.parseInt(items[1]);
        } catch (Exception e) {
            Log.w(StreamingApp.TAG, e.getMessage());
            Log.w(StreamingApp.TAG, response);
        }
        if (responseCode == RTSP_RESP_ERR)
            return responseCode;

        // Parse response header into key value pairs.
        for (int i = 1; i < lines.length; i++) {
            tempString = lines[i];

            if (tempString.length() == 0)
                break;

            int idx = tempString.indexOf(":");

            if (idx == -1)
                continue;

            key = tempString.substring(0, idx);
            value = tempString.substring(idx + 1);
            headerMap.put(key, value);
        }

        tempString = headerMap.get(kSessionKey);
        if (tempString != null) {
            // Parse session
            items = tempString.split(";");
            tempString = items[0];
            session = tempString.trim();
        }

        return responseCode;
    }

    private void generateBasicAuth() throws UnsupportedEncodingException {
        String userpass = String.format("%s:%s", user, pass);
        authentication = String.format(kAuthBasic, Base64.encodeToString(userpass.getBytes("US-ASCII"), Base64.DEFAULT));
    }

    public static String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(), 0, s.length());
            String hash = new BigInteger(1, digest.digest()).toString(16);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    static private final int CC_MD5_DIGEST_LENGTH = 16;

    private String md5HexDigest(String input) {
        byte digest[] = md5(input).getBytes();
        String result = new String();
        for (int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
            result = result.concat(String.format("%02x", digest[i]));
        return result;
    }

    private void generateDigestAuth(String method) {
        String nonce, realm;
        String ha1, ha2, response;

        // WWW-Authenticate: Digest realm="Streaming Server",
        // nonce="206351b944cb28fe37a0794848c2e36f"
        String wwwauth = headerMap.get(kWWWAuthKey);
        int idx = wwwauth.indexOf("Digest");
        String authReq = wwwauth.substring(idx + "Digest".length() + 1);

        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, String.format("Auth Req: %s", authReq));

        String[] split = authReq.split(",");
        realm = split[0];
        nonce = split[1];

        split = realm.split("=");
        realm = split[1];
        realm = realm.substring(1, 1 + realm.length() - 2);

        split = nonce.split("=");
        nonce = split[1];
        nonce = nonce.substring(1, 1 + nonce.length() - 2);

        if (BuildConfig.DEBUG) {
            Log.d(StreamingApp.TAG, String.format("realm=%s", realm));
            Log.d(StreamingApp.TAG, String.format("nonce=%s", nonce));
        }

        ha1 = md5HexDigest(String.format("%s:%s:%s", user, realm, pass));
        ha2 = md5HexDigest(String.format("%s:%s", method, url));
        response = md5HexDigest(String.format("%s:%s:%s", ha1, nonce, ha2));
        authentication = md5HexDigest(String.format(kAuthDigest, user, realm, nonce, url, response));
    }

    private int options() throws IOException {
        seq++;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kOptions, url));
        request.append(String.format(kCseq, seq));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- OPTIONS Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- OPTIONS Response ---\n\n" + response);
        return parseResponse(response);
    }

    @SuppressWarnings("unused")
    private int describe() throws IOException {
        seq++;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kDescribe, url));
        request.append(String.format(kAccept, "application/sdp"));
        request.append(String.format(kCseq, seq));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- DESCRIBE Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- DESCRIBE Response ---\n\n" + response);
        return parseResponse(response);
    }

    private int recurseDepth = 0;

    private int announce() throws IOException {
        seq++;
        recurseDepth = 0;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kAnnounce, url));
        request.append(String.format(kCseq, seq));
        request.append(String.format(kContentLength, sdp.length()));
        request.append(String.format(kContentType, "application/sdp"));
        request.append(kCRLF);
        if (sdp.length() > 0)
            request.append(sdp);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- ANNOUNCE Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- ANNOUNCE Response ---\n\n" + response);

        int ret = parseResponse(response);
        if (ret == RTSP_BAD_USER_PASS && recurseDepth == 0) {
            String wwwauth = headerMap.get(kWWWAuthKey);
            if (wwwauth != null) {
                if (BuildConfig.DEBUG)
                    Log.d(StreamingApp.TAG, String.format("WWW Auth Value: %s", wwwauth));
                int idx = wwwauth.indexOf("Basic");
                recurseDepth++;

                if (idx != -1) {
                    generateBasicAuth();
                } else {
                    // We are assuming Digest here.
                    generateDigestAuth("ANNOUNCE");
                }

                ret = announce();
                recurseDepth--;
            }
        }
        return ret;
    }

    private int setup(int trackId) throws IOException {
        seq++;
        recurseDepth = 0;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kSetupPublish, url, trackId));
        request.append(String.format(kCseq, seq));

        /* One channel for rtp (data) and one for rtcp (control) */
        String tempString = String.format(Locale.getDefault(), "interleaved=%d-%d", channelCount++, channelCount++);

        request.append(String.format(kTransport, "TCP", "record", tempString));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- SETUP Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- SETUP Response ---\n\n" + response);

        int ret = parseResponse(response);
        if (ret == RTSP_BAD_USER_PASS && recurseDepth == 0) {
            String wwwauth = headerMap.get(kWWWAuthKey);
            if (wwwauth != null) {
                if (BuildConfig.DEBUG)
                    Log.d(StreamingApp.TAG, String.format("WWW Auth Value: %s", wwwauth));
                int idx = wwwauth.indexOf("Basic");
                recurseDepth++;

                if (idx != -1) {
                    generateBasicAuth();
                } else {
                    // We are assuming Digest here.
                    generateDigestAuth("SETUP");
                }

                ret = setup(trackId);
                authentication = null;
                recurseDepth--;
            }
        }
        return ret;
    }

    private int record() throws IOException {
        seq++;
        recurseDepth = 0;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kRecord, url));
        request.append(String.format(kCseq, seq));
        request.append(String.format(kRange, "npt=0.000-"));
        if (authentication != null)
            request.append(authentication);
        if (session != null)
            request.append(String.format(kSession, session));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- RECORD Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- RECORD Response ---\n\n" + response);
        int ret = parseResponse(response);
        if (ret == RTSP_BAD_USER_PASS && recurseDepth == 0) {
            String wwwauth = headerMap.get(kWWWAuthKey);
            if (wwwauth != null) {
                if (BuildConfig.DEBUG)
                    Log.d(StreamingApp.TAG, String.format("WWW Auth Value: %s", wwwauth));
                int idx = wwwauth.indexOf("Basic");
                recurseDepth++;

                if (idx != -1) {
                    generateBasicAuth();
                } else {
                    // We are assuming Digest here.
                    generateDigestAuth("RECORD");
                }

                ret = record();
                authentication = null;
                recurseDepth--;
            }
        }
        return ret;
    }

    @SuppressWarnings("unused")
    private int play() throws IOException {
        seq++;
        recurseDepth = 0;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kPlay, url));
        request.append(String.format(kCseq, seq));
        request.append(String.format(kRange, "npt=0.000-"));
        if (authentication != null)
            request.append(authentication);
        if (session != null)
            request.append(String.format(kSession, session));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- PLAY Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- PLAY Response ---\n\n" + response);
        int ret = parseResponse(response);
        if (ret == RTSP_BAD_USER_PASS && recurseDepth == 0) {
            String wwwauth = headerMap.get(kWWWAuthKey);
            if (wwwauth != null) {
                if (BuildConfig.DEBUG)
                    Log.d(StreamingApp.TAG, String.format("WWW Auth Value: %s", wwwauth));
                int idx = wwwauth.indexOf("Basic");
                recurseDepth++;

                if (idx != -1) {
                    generateBasicAuth();
                } else {
                    // We are assuming Digest here.
                    generateDigestAuth("PLAY");
                }

                ret = record();
                authentication = null;
                recurseDepth--;
            }
        }
        return ret;
    }

    private int teardown() throws IOException {
        seq++;
        recurseDepth = 0;
        StringBuilder request = new StringBuilder();
        request.append(String.format(kTeardown, url));
        request.append(String.format(kCseq, seq));
        if (authentication != null)
            request.append(authentication);
        if (session != null)
            request.append(String.format(kSession, session));
        request.append(kCRLF);
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- TEARDOWN Request ---\n\n" + request);
        write(request.toString());
        String response = read();
        if (response == null)
            return SOCK_ERR_READ;
        if (BuildConfig.DEBUG)
            Log.d(StreamingApp.TAG, "--- TEARDOWN Response ---\n\n" + response);
        int ret = parseResponse(response);
        if (ret == RTSP_BAD_USER_PASS && recurseDepth == 0) {
            String wwwauth = headerMap.get(kWWWAuthKey);
            if (wwwauth != null) {
                if (BuildConfig.DEBUG)
                    Log.d(StreamingApp.TAG, String.format("WWW Auth Value: %s", wwwauth));
                int idx = wwwauth.indexOf("Basic");
                recurseDepth++;

                if (idx != -1) {
                    generateBasicAuth();
                } else {
                    // We are assuming Digest here.
                    generateDigestAuth("TEARDOWN");
                }

                ret = record();
                authentication = null;
                recurseDepth--;
            }
        }
        return ret;
    }

    public void connect(String dest, int port, Session session) throws IOException {
        int trackId = 1;
        int responseCode;

        if (isConnected())
            return;

        if (!session.hasAudioTrack() && !session.hasVideoTrack())
            throw new IOException("No tracks found in session.");

        InetSocketAddress addr = null;
        try {
            addr = new InetSocketAddress(dest, port);
        } catch (Exception e) {
            throw new IOException("Failed to resolve rtsp server address.");
        }

        this.sdp = session.getSDP();
        this.user = session.getUser();
        this.pass = session.getPass();
        this.path = session.getPath();
        this.url = String.format("rtsp://%s:%d%s", dest, addr.getPort(), this.path);

        try {
            super.connect(addr);
        } catch (IOException e) {
            throw new IOException("Failed to connect rtsp server.");
        }

        responseCode = announce();
        if (responseCode != RTSP_OK) {
            close();
            throw new IOException("RTSP announce failed: " + responseCode);
        }

        responseCode = options();
        if (responseCode != RTSP_OK) {
            close();
            throw new IOException("RTSP options failed: " + responseCode);
        }

        /* Setup audio */
        if (session.hasAudioTrack()) {
            session.getAudioTrack().setStreamId(channelCount);
            responseCode = setup(trackId++);
            if (responseCode != RTSP_OK) {
                close();
                throw new IOException("RTSP video failed: " + responseCode);
            }
        }

        /* Setup video */
        if (session.hasVideoTrack()) {
            session.getVideoTrack().setStreamId(channelCount);
            responseCode = setup(trackId++);
            if (responseCode != RTSP_OK) {
                close();
                throw new IOException("RTSP audio setup failed: " + responseCode);
            }
        }

        responseCode = record();
        if (responseCode != RTSP_OK) {
            close();
            throw new IOException("RTSP record failed: " + responseCode);
        }
    }

    public void close() throws IOException {
        if (!isConnected())
            return;
        teardown();
        super.close();
    }
}
