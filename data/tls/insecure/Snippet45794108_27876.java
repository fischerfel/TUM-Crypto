final MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(MainActivity.this, "ssl://" + pref.getMqttUrl(), clientId, persistence);

    try {
         String clientId = MqttClient.generateClientId();

         MqttConnectOptions connectionOptions = new MqttConnectOptions();
         connectionOptions.setCleanSession(true);

        Log.e("Test", "ssl://" + pref.getMqttUrl());

        try {
            InputStream trustStoresIs = context.getResources().openRawResource(R.raw.ca_key);


            String trustStoreType = KeyStore.getDefaultType();
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            trustStore.load(trustStoresIs, context.getString(R.string.bks_password).toCharArray());

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trustStore);

            InputStream keyStoreStream = context.getResources().openRawResource(R.raw.user_cer_key);
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreStream, context.getString(R.string.bks_password).toCharArray());

            KeyManagerFactory keyManagerFactory = null;
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, context.getString(R.string.bks_password).toCharArray());

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) context.getSocketFactory();
            connectionOptions.setSocketFactory(sslsocketfactory);

        } catch (KeyManagementException | CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mqttAndroidClient.connect(connectionOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e("Mqtt","Connection Success!");

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e("Mqtt","Connection Failure!");

            }
        });

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("Mqtt","Connection was lost!");

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {



            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("mqtt Delivery Complete!");
            }

        });


    } catch (Exception ex) {
        ex.printStackTrace();

    }
