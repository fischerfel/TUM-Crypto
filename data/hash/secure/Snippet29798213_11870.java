String username = "username";
String server = "10.99.00.00";
String password = "password";
String realm = null ;
private String nonce = null;

// Objects used to communicate to the JAIN SIP API.
SipFactory sipFactory;          // Used to access the SIP API.
SipStack sipStack;              // The SIP stack.
SipProvider sipProvider;        // Used to send SIP messages.
MessageFactory messageFactory;  // Used to create SIP message factory.
HeaderFactory headerFactory;    // Used to create SIP headers.
AddressFactory addressFactory;  // Used to create SIP URIs.
ListeningPoint listeningPoint;  // SIP listening IP address/port.
Properties properties;          // Other properties.
ClientTransaction inviteTid;
Request request;
Response response;

// Objects keeping local configuration.
String proxy = null;
String sipIP="10.99.00.00"; 
String localIP= null;
// The local IP address.
int sipport = 5060;                // The local port.
int rport = 52216;
String protocol = "UDP";        // The local protocol (UDP).
int tag = (new Random()).nextInt(); // The local tag.
Address contactAddress;         // The contact address.
ContactHeader contactHeader;    // The contact header.
private Dialog dialog;
private Logger logger;
private String current_process;

public test() throws NoSuchAlgorithmException, ParseException{
    init();
    Response response = null;
    register(response);
}

public void init() {
    try {
    // Get the local IP address.
    localIP = InetAddress.getLocalHost().getHostAddress();

    // Create the SIP factory and set the path name.
    sipFactory = SipFactory.getInstance();
    sipFactory.setPathName("gov.nist");
    // Create and set the SIP stack properties.
    properties = new Properties();
    properties.setProperty("javax.sip.STACK_NAME", "stack");
    properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");

    if(proxy != null) {
    properties.setProperty("javax.sip.OUTBOUND_PROXY", sipIP + ':' + sipport + '/' + protocol);
    }

    properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "true");
    properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",  "mss-jsip-debuglog.txt");
    properties.setProperty("gov.nist.javax.sip.SERVER_LOG","mss-jsip-messages.xml");
    // Create the SIP stack.
    sipStack = sipFactory.createSipStack(this.properties);
    // Create the SIP message factory.
    messageFactory = sipFactory.createMessageFactory();
    // Create the SIP header factory.
    headerFactory = sipFactory.createHeaderFactory();
    // Create the SIP address factory.
    addressFactory = sipFactory.createAddressFactory();
    // Create the SIP listening point and bind it to the local IP
    // address, port and protocol.
    listeningPoint = sipStack.createListeningPoint(localIP, rport, protocol);
    // Create the SIP provider.
    sipProvider = sipStack.createSipProvider(listeningPoint);
    // Add our application as a SIP listener.
    sipProvider.addSipListener(this);


    // Display the local IP address and port in the text area.
    } catch (Exception e) {
    e.printStackTrace();
    // If an error occurs, display an error message box and exit.
    System.exit(-1);
    }
    }
    int cseq;
public void register(Response response) {
    try {

    cseq++;
    current_process = cseq + "REGISTER";
    ArrayList viaHeaders = new ArrayList();
    ViaHeader viaHeader = headerFactory.createViaHeader(localIP,
    rport, "udp", null);
    viaHeader.setRPort();
    viaHeaders.add(viaHeader);
    // The "Max-Forwards" header.
    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
    // The "Call-Id" header.
    CallIdHeader callIdHeader = sipProvider.getNewCallId();
    // The "CSeq" header.
    @SuppressWarnings("deprecation")
    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L,Request.REGISTER);

    Address fromAddress = addressFactory.createAddress("sip:"
    + "username" + '@' + server);

    FromHeader fromHeader = headerFactory.createFromHeader(
    fromAddress, String.valueOf(this.tag));
    // The "To" header.

    ToHeader toHeader = headerFactory.createToHeader(fromAddress , null);

    // Create the contact address used for all SIP messages.
    contactAddress = addressFactory.createAddress("sip:" + username + "@"+ localIP +":"+rport+ ";"+ "transport=UDP");
    // Create the contact header used for all SIP messages.
    contactHeader = headerFactory.createContactHeader(contactAddress);

    URI requestURI = addressFactory.createURI("sip:" + server);

    request = messageFactory.createRequest(requestURI, Request.REGISTER,callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwardsHeader);

    request.addHeader(contactHeader);


//      System.out.println(request.toString());
    if (response != null) {

    AuthorizationHeader authHeader = makeAuthHeader(headerFactory, response, request, username, password);
    request.addHeader(authHeader);
    }
    inviteTid = sipProvider.getNewClientTransaction(request);
    // send the request out.
    inviteTid.sendRequest();

//      dialog = inviteTid.getDialog();

    System.out.println(request.toString());
    // Send the request statelessly through the SIP provider.
//          this.sipProvider.sendRequest(request);

    // Display the message in the text area.
//      logger.debug("Request sent:\n" + request.toString() + "\n\n");
    } catch (Exception e) {
    // If an error occurred, display the error.
    e.printStackTrace();
//      logger.debug("Request sent failed: " + e.getMessage() + "\n");
    }

    }

private AuthorizationHeader makeAuthHeader(HeaderFactory headerFactory2,  Response response, Request request, String username2,
        String password2) throws ParseException {
    // TODO Auto-generated method stub
    // Authenticate header with challenge we need to reply to
    WWWAuthenticateHeader ah_c =  (WWWAuthenticateHeader)response.getHeader(WWWAuthenticateHeader.NAME);

    // Authorization header we will build with response to challenge
    AuthorizationHeader ah_r =    headerFactory.createAuthorizationHeader(ah_c.getScheme());

    // assemble data we need to create response string
    URI request_uri = request.getRequestURI();
    String request_method = request.getMethod();
    String nonce  = ah_c.getNonce();
    String algrm  = ah_c.getAlgorithm();
    String realm  = ah_c.getRealm();

    MessageDigest mdigest;
    try {
        mdigest = MessageDigest.getInstance(algrm);

         // A1
        String A1 = username + ":" + realm + ":" + password;
        String HA1 = toHexString(mdigest.digest(A1.getBytes()));

        // A2
        String A2 = request_method.toUpperCase() + ":" + request_uri ;
        String HA2 = toHexString(mdigest.digest(A2.getBytes()));

        // KD
        String KD = HA1 + ":" + nonce + ":" + HA2;
        String responsenew = toHexString(mdigest.digest(KD.getBytes()));

        ah_r.setUsername(username);
        ah_r.setRealm(realm);
        ah_r.setNonce(nonce);
        ah_r.setURI(request_uri);
        ah_r.setAlgorithm(algrm);
        ah_r.setResponse(responsenew);


    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return ah_r;

}
