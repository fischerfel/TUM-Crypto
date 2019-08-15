  private final static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDxJtyHARAMe69K0OoqoO+1yfXr\n" +
        "jfWFums8HLOjArJNyVJapUC4Dr0Rcj0e4wAy1QCQJsxCl8OeitiVAugoML39ZdCMPENc9u/LEGVU\n" +
        "lCB8B3tjB9FoDWzKEqI6CXP5Ga80dNRNakC7/aCUFdVdvYA+m4UNtbzP+KB+RrQQkA9TPBSm/25P\n" +
        "anBkCHrmRQIeC2RQz4eTjkpSpA97fmUsTjSOUrAl94OGdZ2EiWUMZuN7lF26/DUsrLlHB15l4f12\n" +
        "HfsvVyRGCkWK9pg0LLoJpCEq8IJExsrTBOT6QHT3Vx2oKFsHVp6sg2Lknk+W8ioRvLPGb6CPHu4I\n" +
        "q70bCcyVLu0lAgMBAAECggEAa3aq7YqesUFo80k4IPkI+ZTffzIKyKYzZV1Q/AKWnK1rgrODrMH8\n" +
        "pUqoTBxSmVRsZNC1U7O32+356CWceVSHReXaUrQEPOhaIb7TuSYtd7gJ1y7Dtb/NzBsTqhNOWCKG\n" +
        "/jRV642+/UdgCL3WEGkDPmMlnt9vHaqpMrylP4BDJA6Tjef9/AcJ0Df+bn+hCA0ICs1i3PXmMWaS\n" +
        "emJ/DK38caumYyuRYF/o26seU/gY0vMqwf2MH9Anb/f9RxfOtx6MlKd1SnotuYahFU0/rNSNmK/G\n" +
        "oFKPFWBX3e0nTdIz4VQtiSKScHsLkaSwSBP2kN3TK2MFRpyLylOD7C7wtM+mvQKBgQD+liJhlF5T\n" +
        "7C0zSQ2t0mGLkoL58U8eA8TZg2VXb/wl6FmTtQ3dQ+QhFIs4xgO+fCiZfjgA5RPLIvbP71vLHsYH\n" +
        "3/R+zwkQtZy4IvT5jgK4beXsIuQVtN69rwBoWvB1XDDOZlozoG/6RvNMstHeXBH11rzQSZAFzCaJ\n" +
        "u9YyIZy1HwKBgQDyfaGcaqPaso2IH/Iy7Xnu1YzsElE82KAv4tvmnzNpWWeQ8k+q8P4vwDCTR206\n" +
        "D6Vt68B6UpZH5BkiyQ1CUBMU4XJR2+JXQjMQg/T1QpK0mBLOTTZhbJa+61hoKwfgmjnYTLra4yUW\n" +
        "prq6jOxxAkQs0dCX5PrsI2L3/wbAJ5HxOwKBgQDAS+rARe9R+IzTthRs/QHNCOeBnzGhfDtOCJ+d\n" +
        "geq1P2GZ3iIQxV0lV30pbvgZA3MvLVVj56QhxdEjkqqFLENsY08sEXnJc0RjGZRsg0WuxOPsjxY1\n" +
        "Bx9Pq0XljPsfynjbDhiH8mFibAEOJ+u5x1WRmZeYxFfS6TcaxhSuXREfBQKBgQCvWmoj0nnrDHOM\n" +
        "nI1ohpJGQ+dET3qvpXcxKbwbacSjyiM6jf9OfjdSEIkP9/bkpavbcUAfNNm34xrKLNmJup4R23Xv\n" +
        "/DIRJ64Fo2bgdOPh9Jak2PEaQoxAQ7AKpBNuOBjccaAt6VRIrQkbHg1dK2Zhgth0/wD1AiHvdFxN\n" +
        "WFARQQKBgAsPKujqLWyQjpFiqk51/mbGP0xnK6QQI4981pG9UkHRugL5EIUwe4mBSa2v9nIsqJ29\n" +
        "Qk2pvaILZWUniVzcK4stbx1ML3HEcpsTi0/w49Abl0k/0ycmErey63bSgf5fszGti/MBOLVagxsa\n" +
        "7srdWq2DntK5AHy7gSk+WcMiHF8v";

private final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8SbchwEQDHuvStDqKqDvtcn16431hbpr\n" +
        "PByzowKyTclSWqVAuA69EXI9HuMAMtUAkCbMQpfDnorYlQLoKDC9/WXQjDxDXPbvyxBlVJQgfAd7\n" +
        "YwfRaA1syhKiOglz+RmvNHTUTWpAu/2glBXVXb2APpuFDbW8z/igfka0EJAPUzwUpv9uT2pwZAh6\n" +
        "5kUCHgtkUM+Hk45KUqQPe35lLE40jlKwJfeDhnWdhIllDGbje5Rduvw1LKy5RwdeZeH9dh37L1ck\n" +
        "RgpFivaYNCy6CaQhKvCCRMbK0wTk+kB091cdqChbB1aerINi5J5PlvIqEbyzxm+gjx7uCKu9GwnM\n" +
        "lS7tJQIDAQAB";

 public String encryptedString(String plain) throws Exception {
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(PRIVATE_KEY, Base64.DEFAULT));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey key = keyFactory.generatePrivate(keySpec);
      byte[] dataToBytes = plain.getBytes("UTF-8");
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, key);

      return Base64.encodeToString(cipher.doFinal(dataToBytes), Base64.DEFAULT);
}
