TrustManager[] tms =
        { new com.willeke.security.FakeX509TrustManager() };
        SSLContext context = null;
        try
        {
            context = SSLContext.getInstance("TLS", "SunJSSE");
            /**
             * The first parameter - null means use the default key manager. The desired TrustManager The third parameter - null means use the default value secure random".
             */
            context.init(null, tms, null);
        }
        catch (... ex)
}
