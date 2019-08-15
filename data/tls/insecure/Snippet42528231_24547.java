  type PR = ProducerRecord[String, String]

  val props = new Properties();
  props.put("bootstrap.servers", "localhost:9092");
  props.put("key.serializer", classOf[StringSerializer].getCanonicalName);
  props.put("value.serializer", classOf[StringSerializer].getCanonicalName);

  implicit val system = ActorSystem("readings-data-receiver")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val producer = new KafkaProducer[String, String](props);
  val settings = CorsSettings.defaultSettings

  val password: Array[Char] = new Array[Char]('a');// do not store passwords in code, read them from somewhere safe!


  val ks: KeyStore = KeyStore.getInstance("PKCS12")
  val keystore: InputStream = getClass.getClassLoader.getResourceAsStream("server.p12")

  //require(keystore != null, "Keystore required!")
  ks.load(keystore, password)

  val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(ks, password)

  val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  tmf.init(ks)
  val sslContext: SSLContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, SecureRandom.getInstanceStrong)
  val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

  // sets default context to HTTPS – all Http() bound servers for this ActorSystem will use HTTPS from now on
  Http().setDefaultServerHttpContext(https)

  val route: Route= cors(settings) {
    post {
      path("") {
        entity(as[String]) {payload=>
            val rec = new PR("Events", 0, UUID.randomUUID().toString, payload)
            producer.send(rec)
            println(rec)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, ""))

        }
      }
    }
  }

  //val bindingFuture = Http().bindAndHandle(route, "54.85.112.231",9000,connectionContext = https)
  val bindingFuture = Http().bindAndHandle(route, "54.85.112.231:9000",connectionContext = https)
