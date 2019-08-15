public void createServerSocket() throws IOException {
    synchronized (this) {
        if (this.useSsl) {
            ListenPort.logger.info("(ListenPort " + this.port + ") creating SSL server Socket , using Ssl : On");
            try {
                final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                ListenPort.logger.info("ListenPort " + this.port + ") Default keymanagerFactory algorithm:  '" + KeyManagerFactory.getDefaultAlgorithm() + "'.");
                //final KeyManagerList keyManagers = new KeyManagerList();
                //final TrustManagerList trustManagers = new TrustManagerList();
                final ArrayList<KeyManager> keyManagers = new ArrayList<KeyManager>();
                final ArrayList<TrustManager> trustManagers = new ArrayList<TrustManager>();

                this.keystores.forEach((k, v) -> {
                    try {
                        ListenPort.logger.info("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "'");
                        final KeyStore ks = KeyStore.getInstance("JKS");
                        final KeyStore ks2 = KeyStore.getInstance("JKS");
                        final FileInputStream fis = new FileInputStream(k);
                        ListenPort.logger.info("(ListenPort " + this.port + ") Retrieving ssl ids from keystore '" + k + "' = passwd '" + v + "'");
                        ks2.load(fis, v.toCharArray());
                        ListenPort.logger.info("(ListenPort " + this.port + ")                         keystore '" + k + "' Loaded.");
                        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                        ListenPort.logger.info("(ListenPort " + this.port + ")          initializing Keymanager '" + k + "'.");
                        kmf.init(ks2, v.toCharArray());
                        final KeyManager[] km = kmf.getKeyManagers();
                        if (km.length > 0) {
                            ListenPort.logger.finest("(ListenPort " + this.port + ") Reading " + km.length + " key(s)");
                            for (int i = 0; i < km.length; ++i) {
                                ListenPort.logger.finest("(ListenPort " + this.port + ") Reading key " + km[i]);
                                keyManagers.add(km[i]);
                            }
                        }
                        ListenPort.logger.info("(ListenPort " + this.port + ")          initializing trustmanager '" + k + "'.");
                        tmf.init(ks2);
                        final TrustManager[] tm = tmf.getTrustManagers();
                        if (tm.length > 0) {
                            ListenPort.logger.finest("(ListenPort " + this.port + ") Reading " + km.length + " trust(s)");
                            for (int j = 0; j < tm.length; ++j) {
                                ListenPort.logger.finest("(ListenPort " + this.port + ")  Reading trust " + tm[j]);
                                trustManagers.add(tm[j]);
                            }
                        }
                        ListenPort.logger.info("(ListenPort " + this.port + ") Retrieving ssl On : from keystore '" + k + "' ok");
                    }
                    catch (UnrecoverableKeyException e7) {
                        ListenPort.logger.info("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "' unrecoverable Key");
                    }
                    catch (KeyStoreException e8) {
                        ListenPort.logger.info("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "' Bad Keystore");
                    }
                    catch (NoSuchAlgorithmException e9) {
                        ListenPort.logger.info("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "' No such Algorithm");
                    }
                    catch (CertificateException e10) {
                        ListenPort.logger.info("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "' Bad Certificate");
                    }
                    catch (IOException e11) {
                        ListenPort.logger.severe("(ListenPort " + this.port + ") ssl On : keystore ='" + k + "' IOExcept");
                    }
                    return;
                });
                try {
                    KeyManager[] km2 = new KeyManager[0];
                    TrustManager[] tp = new TrustManager[0];
                    km2 = keyManagers.toArray(km2);
                    tp = trustManagers.toArray(tp);
                    ListenPort.logger.finest("(ListenPort " + this.port + ") Init SSL Context : " + km2.length + " key , " + tp.length + " trusts");
                    sslContext.init(km2, tp, null);
                    SSLServerSocket sslsrv;
                    if (this.bindAddr == null) {
                        ListenPort.logger.finest("(ListenPort " + this.port + ") No specific Binding Address");
                        sslsrv = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(this.port);
                    }
                    else {
                        ListenPort.logger.finest("(ListenPort " + this.port + ") Binding Address" + this.bindAddr.getHostAddress());
                        sslsrv = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(this.port, 0, this.bindAddr);
                    }
                    ListenPort.logger.finest("(ListenPort " + this.port + ") Enabling protocols");
                    String[] protocols = sslsrv.getSupportedProtocols();
                    for (int l = 0; l < protocols.length; ++l) {
                        ListenPort.logger.finest("(ListenPort " + this.port + ")                 " + protocols[l]);
                    }
                    sslsrv.setEnabledProtocols(protocols);
                    String[] suites = sslsrv.getSupportedCipherSuites();
                    ListenPort.logger.finest("(ListenPort " + this.port + ") Supported cipher suites are:");
                    for (int m = 0; m < suites.length; ++m) {
                        ListenPort.logger.finest("(ListenPort " + this.port + ")            " + suites[m]);
                    }
                    sslsrv.setEnabledCipherSuites(suites);
                    suites = sslsrv.getEnabledCipherSuites();
                    ListenPort.logger.finest("(ListenPort " + this.port + ") Supported cipher suites are:");
                    for (int m = 0; m < suites.length; ++m) {
                        ListenPort.logger.finest("(ListenPort " + this.port + ")            " + suites[m]);
                    }
                    ListenPort.logger.finest("(ListenPort " + this.port + ") Support protocols are:");
                    protocols = sslsrv.getSupportedProtocols();
                    for (int m = 0; m < protocols.length; ++m) {
                        ListenPort.logger.finest("(ListenPort " + this.port + ")            " + protocols[m]);
                    }
                    this.srv = sslsrv;
                }
                catch (NullPointerException e) {
                    ListenPort.logger.warning("ListenPort " + this.port + ") Erreur Creating server socket : " + e.getMessage());
                }
                catch (IOException e2) {
                    ListenPort.logger.warning("ListenPort " + this.port + ") Erreur Creating server socket" + e2.getMessage());
                }
                ListenPort.logger.info("(ListenPort " + this.port + ") ssl On server created");
            }
            catch (NoSuchAlgorithmException e3) {
                if (null != this.srv) {
                    this.srv.close();
                }
                this.srv = null;
                System.err.println(" open ssl listenning port : no such algorithm");
                ListenPort.logger.severe("ListenPort " + this.port + ") open ssl listenning port : no such algorithm disabling ssl : " + e3.getMessage());
            }
            catch (KeyManagementException e4) {
                if (null != this.srv) {
                    this.srv.close();
                }
                this.srv = null;
                ListenPort.logger.severe("(ListenPort " + this.port + ") creating SSL server Socket :key management problem " + e4.getMessage());
            }
            catch (Exception e5) {
                if (null != this.srv) {
                    this.srv.close();
                }
                this.srv = null;
                ListenPort.logger.info("(ListenPort " + this.port + ") creating SSL server Socket :key  management problem " + e5.getMessage());
            }
            ListenPort.logger.info("(ListenPort " + this.port + ") creating server Socket , using Ssl : On");
        }
        else {
            srv = new ServerSocket();
            try {
                if (this.bindAddr == null) {
                    srv.bind(new InetSocketAddress(this.port));
                }
                else {
                    srv.bind(new InetSocketAddress(this.bindAddr, this.port));
                }
            }
            catch (BindException e6) {
            }
        }
    }
}
