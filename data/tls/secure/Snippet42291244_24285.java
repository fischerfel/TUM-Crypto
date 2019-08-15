try {
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(null, null, null);
        SSLContext.setDefault(ctx);
} catch (Exception e) {
        System.out.println(e.getMessage());
}
