package Security;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.security.*;

import javax.net.ssl.*;

import com.sun.net.httpserver.*;


public class HTTPSServer 
{
    public static void main(String[] args) throws IOException
    {
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpsServer server = HttpsServer.create(addr, 0);

        try
        {
            System.out.println("\nInitializing context ...\n");
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] password = "vwpolo".toCharArray();
            ks.load(new FileInputStream("myKeys"), password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            //  a HTTPS server must have a configurator for the SSL connections.
            server.setHttpsConfigurator (new HttpsConfigurator(sslContext)
            {
                //  override configure to change default configuration.
                public void configure (HttpsParameters params)
                {
                    try
                    {
                        //  get SSL context for this configurator
                        SSLContext c = getSSLContext();

                        //  get the default settings for this SSL context
                        SSLParameters sslparams = c.getDefaultSSLParameters();

                        //  set parameters for the HTTPS connection.
                        params.setNeedClientAuth(true);
                        params.setSSLParameters(sslparams);
                        System.out.println("SSL context created ...\n");
                    }
                    catch(Exception e2)
                    {
                        System.out.println("Invalid parameter ...\n");
                        e2.printStackTrace();
                    }
                }
            });
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }

        server.createContext("/", new MyHandler1());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8080 ...\n");
    }
}

class MyHandler implements HttpHandler 
{
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET"))
        {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            String response = "HTTP headers included in your request:\n\n";
            responseBody.write(response.getBytes());

            Headers requestHeaders = exchange.getRequestHeaders();
            Set<String> keySet = requestHeaders.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext())
            {
                String key = iter.next();
                List values = requestHeaders.get(key);
                response = key + " = " + values.toString() + "\n";
                responseBody.write(response.getBytes());
                System.out.print(response);
            }

            response = "\nHTTP request body: ";
            responseBody.write(response.getBytes());
            InputStream requestBody = exchange.getRequestBody();
            byte[] buffer = new byte[256];
            if(requestBody.read(buffer) > 0) 
            {
                responseBody.write(buffer);
            }
            else
            {
                responseBody.write("empty.".getBytes());
            }

            URI requestURI = exchange.getRequestURI();
            String file = requestURI.getPath().substring(1);
            response = "\n\nFile requested = " + file + "\n\n";
            responseBody.write(response.getBytes());
            responseBody.flush();
            System.out.print(response);

            Scanner source = new Scanner(new File(file));
            String text;
            while (source.hasNext())
            {
                text = source.nextLine() + "\n";
                responseBody.write(text.getBytes());
            }
            source.close();

            responseBody.close();

            exchange.close();
        }
    }
}
