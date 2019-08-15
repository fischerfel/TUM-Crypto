public class ServerConnection {
    private WebSocketFactory webSocketfactory;
    private WebSocket ws;
    private MediaStream mS;
    private PeerConnectionFactory peerFactory;
    private PeerConnection yourConn;
    private MediaConstraints sdpConstraints;
    private SessionObserver sessionObserver = new SessionObserver();
    private PeerObserver peerObserver = new PeerObserver();
    private String connectedUser;

    public ServerConnection(MediaStream mS, PeerConnectionFactory peerFactory) {
        this.webSocketfactory = new WebSocketFactory();
        this.mS = mS;
        this.peerFactory = peerFactory;
        // Create a custom SSL context.
        SSLContext context = null;
        try {
            context = NaiveSSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // Set the custom SSL context.
        webSocketfactory.setSSLContext(context);
        initWebsockets();

    }

    public void sendMessage(String message) {
        if (connectedUser != null) {
            try {
                JSONObject json = new JSONObject(message);
                json.put("name", connectedUser);
                ws.sendText(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("String s", message);
            ws.sendText(message);

        }
    }

    private void initWebsockets() {
        try {
            ws = webSocketfactory.createSocket("wss://192.168.179.36:9090");
            ws.addListener(new SocketListener());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ws.connect();
                    } catch (WebSocketException e) {
                        e.printStackTrace();
                        initWebsockets();
                    }
                }
            });
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SocketListener extends WebSocketAdapter {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            Log.e("connected", "connected");
            JSONObject json = new JSONObject();
            try {
                // Für den Anfang statisch
                json.put("type", "login");
                json.put("name", "John");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessage(json.toString());
        }


        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            Log.e("onError", exception.getMessage());
        }


        @Override
        public void onDisconnected(WebSocket websocket,
                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) throws Exception {
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            Log.e("Got Message", text);
            JSONObject json = new JSONObject(text);
            if (json != null) {
                switch (json.getString("type")) {
                    case "login":
                        Log.e("Condition", json.getString("success"));
                        handleLogin(json.getBoolean("success"));
                        break;
                    case "offer":
                        handleOffer(json.getString("offer"), json.getString("name"));
                        break;
                    case "answer":
                        handleAnswer(json.getString("answer"));
                        break;
                    case "candidate":
                        String candidate = json.getString("candidate");
                        handleCandidate(candidate);
                        break;
                    case "leave":
                        break;
                    default:
                        break;
                }
            }
        }


        @Override
        public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
            Log.e("sent", frame.getPayloadText());
        }
    }


    private void handleLogin(boolean success) {
        if (!success) {
            Log.e("handleLogin", "Try a different Username");
        } else {
            List<PeerConnection.IceServer> server = new ArrayList<>();
            server.add(new PeerConnection.IceServer("stun:stun2.1.google.com:19302"));
            server.add(new PeerConnection.IceServer("turn:192.158.29.39:3478?transport=udp", "28224511:1379330808", "JZEOEt2V3Qb0y27GRntt2u2PAYA="));
            sdpConstraints = new MediaConstraints();
            sdpConstraints.optional.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
            sdpConstraints.optional.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));

            yourConn = peerFactory.createPeerConnection(server, sdpConstraints, peerObserver);
            Log.e("Media Stream:", mS.toString());
            yourConn.addStream(mS);

            // wird später implementiert um anrufe zu starten
            // yourConn.createOffer(new SessionObserver(), sdpConstraints);
        }
    }


    private void handleOffer(String offer, String name) {
        try {
            JSONObject sdp = new JSONObject(offer);

            connectedUser = name;
            SessionDescription sessionDescription = new SessionDescription(SessionDescription.Type.OFFER, sdp.getString("sdp"));
            yourConn.setRemoteDescription(sessionObserver, sessionDescription);

            yourConn.createAnswer(sessionObserver, sdpConstraints);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void handleAnswer(String answer) {
        try {
            Log.e("answer", answer);
            JSONObject sdp = new JSONObject(answer);
            SessionDescription sessionDescription = new SessionDescription(SessionDescription.Type.ANSWER, sdp.getString("sdp"));
            yourConn.setRemoteDescription(sessionObserver, sessionDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleLeave() {
    }

    private void handleCandidate(String candidate) {
        Log.e("handleCandidate", candidate);

        //yourConn.addIceCandidate(new IceCandidate())
//        try {
//            JSONObject candidateJson = new JSONObject();
//            candidateJson.put("type", "candidate");
//            JSONObject candidateInfosJSON = new JSONObject(candidate);
//            candidateJson.put("candidate", candidateInfosJSON);
//
//            sendMessage(candidateJson.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


    class PeerObserver implements PeerConnection.Observer {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.e("onSignalingChange", "");
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.e("onIceConnectionChange", "");

        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.e("onIceConnectionChange", "");
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.e("onIceGatheringChange", "");
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
//            Log.e("onIceCandidate", iceCandidate.toString());
//            JSONObject candidate = new JSONObject();
//            try {
//
//                candidate.put("type", "candidate");
//                candidate.put("label", iceCandidate.sdpMLineIndex);
//                candidate.put("id", iceCandidate.sdpMid);
//                candidate.put("candidate", iceCandidate);
            Log.e("OnIceCandidate", "here");


//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            Log.e("onAddStream", "Stream added: " + mediaStream);

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {
            Log.e("onRemoveStream", "Removed mediaStream: " + mediaStream);
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.e("onDataChannel", "");
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.e("onRenegotiationNeeded", "");
        }

    }


    class SessionObserver implements SdpObserver {

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.e("Session", "sending");
            JSONObject session = new JSONObject();
            JSONObject answer = new JSONObject();
            try {
                String answerORoffer = sessionDescription.type.toString().toLowerCase();
                session.put("type", answerORoffer);
                answer.put("type", answerORoffer);
                answer.put("sdp", sessionDescription.description);
                session.put(answerORoffer, answer);
                Log.e("SESSION", session.toString());
                //session.put(answerORoffer, sessionDescription.description);

                sendMessage(session.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSetSuccess() {
            Log.e("do call success", ".....");
        }

        @Override
        public void onCreateFailure(String s) {
            Log.e("do call failure", s);

        }

        @Override
        public void onSetFailure(String s) {
            Log.e("onSetFailure", s);

        }
    }

}
