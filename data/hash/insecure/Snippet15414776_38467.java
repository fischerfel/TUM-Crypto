final byte[] officalkey = {-58, -42, -44, -106, 90, -88, -87, -88, -52, -124, 84, 117, 66, 79, -112, -111, -46, 86, -37, 109};
final byte[] officaldebugkey = {-99, -69, 45, 71, 114, -116, 82, 66, -99, -122, 50, -70, -56, -111, 98, -35, -65, 105, 82, 43};

Signature raw = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];
CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(raw.toByteArray()));
MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] der = cert.getEncoded();
md.update(der);
byte[] digest = md.digest();


if (Arrays.equals(digest, officalkey))
    apksign = c.getString(R.string.official_build);
else if (Arrays.equals(digest, officaldebugkey))
    apksign = c.getString(R.string.debug_build);
else
    apksign = c.getString(R.string.built_by,cert.getSubjectX500Principal().getName());
