@Stateless
public class GoogleDirectionsIntegration {

private static final Logger LOGGER = Logger.getLogger(GoogleDirectionsIntegration.class.getName());

private GeoApiContext context = null;

/**
 * Initializer
 */
@PostConstruct
public void init() {
    LOGGER.log(Level.INFO, "initiating {0}", this.getClass().getSimpleName());
    this.context = new GeoApiContext().setEnterpriseCredentials("gme-company", "companyGoogleCryptographicSecret");
    this.context.setReadTimeout(1, TimeUnit.SECONDS)
            .setRetryTimeout(1, TimeUnit.SECONDS)
            .setConnectTimeout(1, TimeUnit.SECONDS)
            .setWriteTimeout(1, TimeUnit.SECONDS);
    OkHttpRequestHandler okHttpRequestHandler = null;
    OkHttpClient okHttpClient = null;

    try {
        Field requestField = this.context.getClass().getDeclaredField("requestHandler");
        requestField.setAccessible(true);
        okHttpRequestHandler = (OkHttpRequestHandler) requestField.get(this.context);
        Field f = okHttpRequestHandler.getClass().getDeclaredField("client");
        f.setAccessible(true);
        okHttpClient = (OkHttpClient) f.get(okHttpRequestHandler);
    } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
        throw new IllegalStateException("Failed to create SSL context", e);
    }

    SSLContext sslCtx = this.getSslContext();

    if (sslCtx != null && okHttpClient != null) {
        SSLSocketFactory sslSocketFactory = sslCtx.getSocketFactory();
        okHttpClient.setSslSocketFactory(sslSocketFactory);
    }
}

private SSLContext getSslContext() {
    TrustManager[] tm = new TrustManager[] {
            new CustomTrustManager()
    };

    SSLContext sslContext = null;
    try {
        sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tm, new SecureRandom());
    } catch (NoSuchAlgorithmException | KeyManagementException ex) {
        throw new IllegalStateException("Failed to create SSL context", ex);
    }
    return sslContext;
}

public DirectionsRoute getDirections(final String origin, final String destination, final DistanceUnit distanceUnit,
        @Nullable TransportMode mode, @NotNull Instant arrivalTime) throws NotFoundException {
    TransportMode actualMode = mode == null ? TransportMode.CAR : mode;
    DirectionsRoute[] directionsRoutes;

    DirectionsApiRequest directionsApiRequest = DirectionsApi.getDirections(this.context, origin, destination);
    directionsApiRequest.arrivalTime(new Instant(arrivalTime));
    directionsApiRequest.alternatives(false);
    directionsApiRequest.mode(this.toTravelMode(actualMode));

    try {
        DirectionsResult res = directionsApiRequest.await(); // THIS IS WHERE IT BREAKS!
        directionsRoutes = res.routes;
    } catch (Exception e) {
        LOGGER.log(Level.WARNING, e.getMessage(), e);
        throw new NotFoundException(e.getMessage());
    }

    if (directionsRoutes.length != 1) {
        throw new NotFoundException("Failed to fetch valid directions");
    }

    return directionsRoutes[0];
}

public void getAddress(LatLng startLocation, Location location, boolean cacheOverride) throws Exception {
    com.google.maps.model.LatLng gLatLng = new com.google.maps.model.LatLng(startLocation.getLat(), startLocation.getLng());
    GeocodingApiRequest geocodingApiRequest = GeocodingApi.reverseGeocode(this.context, gLatLng);
    GeocodingResult[] geocodingResults;
    geocodingResults = geocodingApiRequest.await();
    if (0 < geocodingResults.length) {
        //.. Code that does stuff with the result..
    } else {
        LOGGER.log(Level.WARNING, "Received empty results from Google reverse geocode for [{0}].", startLocation);
    }
}

}
