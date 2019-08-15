import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class GetExample {
    OkHttpClient client = new OkHttpClient();
    String run(String url) throws IOException {

        client.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify1(String hostname, SSLSession session) {
                //TODO: Make this more restrictive
                return true;
            }
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        Request request = new Request.Builder()
        .url(url)
        .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.protocol());
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        GetOrginal example = new GetOrginal();
        String response = example.run("https://localhost/");
    //  String response = example.run("https://google.com");
        System.out.println(response.length());
    }
}
