    SSLContext sslContext=SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
    SSLSocketFactory noSSLv3Factory;

    if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.KITKAT) {
      noSSLv3Factory=new TLSSocketFactory(sslContext.getSocketFactory());
    }
    else {
      noSSLv3Factory=sslContext.getSocketFactory();
    }

    OkHttpClient.Builder okb=new OkHttpClient.Builder()
      .sslSocketFactory(noSSLv3Factory, provideX509TrustManager());
    OkHttpClient ok=okb.build();

    Picasso p=new Picasso.Builder(getActivity())
      .downloader(new OkHttp3Downloader(ok))
      .build();

    p.load(
      "https://sdo.gsfc.nasa.gov/assets/img/latest/latest_2048_HMIIC.jpg")
      .fit().centerCrop()
      .placeholder(R.drawable.owner_placeholder)
      .error(R.drawable.owner_error).into(icon);
