public static void main(String[] args) {
        try {
            pruebaConexion pruebaconexion = new pruebaConexion();
            System.out.println(myConnect());
            pruebaconexion.init(ctx, QUEUE);
            pruebaconexion.publish("XD");
            pruebaconexion.close();

        } catch (NamingException ex) {
            Logger.getLogger(pruebaConexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(pruebaConexion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static boolean myConnect() {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }

        boolean result = false;

        Hashtable ht = new Hashtable();
        ht.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY );
        ht.put(Context.PROVIDER_URL, PROVIDER_URL);
        ht.put(Context.SECURITY_PRINCIPAL, USER);
        ht.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        ht.put("Dweblogic.rjvm.enableprotocolswitch",
                "true");


        try {
            ctx = new InitialContext(ht);
            result = true;
        } catch (NamingException ne) {
            System.out.println("JNDI Exception: ");
            ne.printStackTrace();
            result = false;
        }
        return result;
    }

    public void publish(String mensaje) {
        try {
            TextMessage message = qsession.createTextMessage();
            message.setText(mensaje);
            qproducer.send(message);
            //System.out.println("- Delivered: "+temperature+" in "+roomId);
        } catch (JMSException jmse) {
            System.err.println("An exception occurred: " + jmse.getMessage());
        }
    }

    public void init(Context ctx, String queueName)
        throws NamingException, JMSException
    {
        qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
        qcon = qconFactory.createQueueConnection();
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) ctx.lookup(queueName);
        qproducer = qsession.createProducer(queue);
    }

    public void close() throws JMSException {
        qsession.close();
        qcon.close();
    }
