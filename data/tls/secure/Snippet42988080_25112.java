SSLContext context = null;
try {
    context = SSLContext.getInstance("TLSv1.2");
    context.init(null, null, new java.security.SecureRandom());
    SSLContext.setDefault(context);
    LOGGER.info("Currecnt TLS:" + SSLContext.getDefault().getProtocol());
}catch (Exception e){
    LOGGER.error("Error while updating TLS:",e);
}
