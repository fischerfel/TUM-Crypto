Cipher        c;
Provider      p;
StringBuilder bldr;

c    = Cipher.getInstance("RSA");
p    = cipher.getProvider();
bldr = new StringBuilder();

bldr.append(_p.getName())
  .append(" ").append(_p.getVersion())
  .append(" (").append(_p.getInfo()).append(")");
Log.i("test", bldr.toString());
