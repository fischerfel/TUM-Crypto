public class Speech2Text extends WatsonService {
private static final Logger logger = LoggerFactory           .getLogger(Speech2Text.class);
public static void main(String[] args) throws FileNotFoundException,           UnsupportedEncodingException, InterruptedException {
    Speech2Text s2t = new Speech2Text();
    s2t.httpClient();
    // try {
    // s2t.webSocketClient();
    // } catch (URISyntaxException e) {
    // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
}
public void httpClient() throws FileNotFoundException,UnsupportedEncodingException {
    logger.info("Running http client");
    final Stopwatch stopwatch = Stopwatch.createStarted();
    SpeechToText service = new SpeechToText();
    service.setUsernameAndPassword("XXXXXX","XXXXX");
    List<SpeechModel> models = service.getModels();
    for (SpeechModel model : models) {
        logger.info(model.getName());
    }
    SpeechSession session = service.createSession("en-US_NarrowbandModel");
    System.out.println(session.toString());
    SessionStatus status = service.getRecognizeStatus(session);
    logger.info(status.getModel());
    logger.info(service.getEndPoint());
    File audio = new File("/home/baaron/watson-bluemix/answer_06.wav");
    Map params = new HashMap();
    params.put("audio", audio);
    params.put("content_type", "audio/wav");
    params.put("continuous", "true");
    params.put("session_id", session.getSessionId());
    logger.info(service.getEndPoint());
    SpeechResults transcript = service.recognize(params);
    PrintWriter writer = new PrintWriter("/home/baaron/watson-bluemix/PCCJPApart1test.transcript",   "UTF-8");
    writer.println(transcript.toString());
    SessionStatus status1 = service.getRecognizeStatus(session.getSessionId());
    System.out.println(status1);
    service.deleteSession(session.getSessionId());
    writer.close();
    stopwatch.stop();
    logger.info("Processing took: " + stopwatch + ".");
}
public void webSocketClient() throws URISyntaxException, IOException,
        InterruptedException {
    logger.info("Running web socket client");
    String encoding = new String(Base64.encodeBase64String("XXXXXXXXXX".getBytes()));
    HttpPost httppost = new HttpPost(                "https://stream.watsonplatform.net/authorization/api/v1/token?url=https://stream.watsonplatform.net/speech-to-text/api");
    httppost.setHeader("Authorization", "Basic " + encoding);
    System.out.println("executing request " + httppost.getRequestLine());
    DefaultHttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(httppost);
    HttpEntity entity = response.getEntity();
    logger.info(response.getStatusLine().getReasonPhrase());
    WebSocketImpl.DEBUG = true;
    BufferedReader reader = new BufferedReader(new InputStreamReader(                entity.getContent()));
    StringBuilder out = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        out.append(line);
    }
    String token = out.toString();
    final WebSocketClient client = new WebSocketClient(
            new URI("wss://stream.watsonplatform.net/speech-to-text-beta/api/v1/recognize?watson-token=" + token)) {
        @Override
        public void onMessage(String message) {
            JSONObject obj = new JSONObject(message);
            // String channel = obj.getString("channel");
        }
        @Override
        public void onOpen(ServerHandshake handshake) {
            System.out.println("opened connection");
        }
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("closed connection");
        }
        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    };
    // open websocket
    SSLContext sslContext = null;
    try {
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null); 
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
    client.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(
            sslContext));
    logger.info("CONNECTED: " + client.connectBlocking());
    JSONObject obj = new JSONObject();
    obj.put("action", "start");
    obj.put("content-type", "audio/wav");
    client.send(obj.toString());
    logger.info("Done");
  }
} 
