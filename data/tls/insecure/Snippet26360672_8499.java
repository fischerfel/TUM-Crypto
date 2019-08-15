try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, MemorizingTrustManager.getInstanceList(this.getApplicationContext()), new SecureRandom());
            connConfig.setCustomSSLContext(sc);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException(e);
        }
