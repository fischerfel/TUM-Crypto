                SSLContext context = SSLContext.getInstance("SSLv3");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        this.connectionFactory.useSslProtocol(context);
