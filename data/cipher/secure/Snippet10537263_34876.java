KeyFactory fact = KeyFactory.getInstance("RSA");
BigInteger e=new BigInteger("65537");
BigInteger n=new BigInteger("B7AC0C8F738305F8BDDF93EE25655A70FBDF9F074640C159E36914C227BE1E50A615A25E6706EBA08FB79216F02279420ED4C9DA310778601F6A3233EDADE2FF3775D29E5302C4FCB9E7879D9F3C814AE8F42759148D91CDB23E528241AE2E44F6DE9D4334494C103886B2333D5833EFEABD76205B8F4897BB908E71697A10F6494EBFB0392A831575F64672E0F915A88F46F7FC03E7F94EB56A8A3296840095CB53787EE6E71D4C297108EA5CDD31BD37B1C0A55A8B5FA78F88FC82AEF3C2C80C0FC2CC97A1AEC74ACE44F4AC1B111B727311DA4D1447899A15BA292BCA4E7E864DF55CCB903CD4109874AD475393E7F24FC60E2D7B88E9B4FB57FA54A9B817",16);
RSAPublicKeySpec keySpec=new RSAPublicKeySpec(n,e);
PublicKey pubKey = (PublicKey) fact.generatePublic(keySpec);
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, pubKey );
String msg="dpbqNszFN2cpmtlzPi3myV9M1ctlkSIQD95ue+T+9rz13T+Pe/aLZ8Pd5geI+PhEM/b0UeRS1cAzKybQsKICTBhh3ke5Jjw6BHWGESJWBnUT54lAlTvzkgOxpQ5stBh2cPPSn3KLyKmXifr8ClbV5s1k3Gy5C7HitA5KLw7hRxAmIGSWQG7PaiLNEVRbgicNfJ7Ic7VIdGA/UA51vK8mpywIR2YQUDPv30ThGq4DuclaJ3X4aVWVj8VYChcfM+82sViVU8HO3DF9CCU4EIADNET503olxiDZBp7WMYmJvWq0KhhZXkLSY3QFmcSMX6IThtdKKCcZp6hu3TtC+7aP7Q==";
byte[] bytes=msg.getBytes("UTF-8");
byte[] enc_bytes= Base64.decode(bytes,Base64.DEFAULT);
byte[]  dt = cipher.doFinal(enc_bytes);   
String code=new String(dt,"UTF-8");
System.out.println(code);
