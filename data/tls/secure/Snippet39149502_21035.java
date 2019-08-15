SSLSessionContext ssc = SSLContext.getInstance("TLSv1.2").getClientSessionContext();
for (Enumeration<byte[]> e = ssc.getIds(); e.hasMoreElements();)
{
   byte[] id = e.nextElement();
   ssc.getSession(id).invalidate();
   System.out.println("Invalidating!");
}
Thread.sleep(1100);
