    HashSet<String> hsDevice =  new HashSet<String>();
hsDevice.add(getDeviceIDforAdMob());
adRequest.setTestDevices(hsDevice);
DfpAdView adView = new DfpAdView(Base.this, AdSize.BANNER, baneerID);                   
    adView.loadAd(adRequest);

    public String getDeviceIDforAdMob()
    {
      String aid = Settings.Secure.getString(Base.this.getContentResolver(), "android_id");

     Object obj = null;
      try 
      {
         ((MessageDigest) (obj = MessageDigest.getInstance("MD5"))).update( aid.getBytes(), 0, aid.length());
         obj = String.format("%032X", new Object[] { new BigInteger(1,((MessageDigest) obj).digest()) });
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException) 
      {
        obj = aid.substring(0, 32);
      }
      return (String) obj;
    }
