import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class JavaxNetSSLClient {

    public static void main(String[] args) throws Exception {
        try {
            //Create connection
            HttpsURLConnection connection = getClient();

            OutputStreamWriter request = getRequest(connection);
            request.flush();

            //Process response
            parseResponse(connection, request);

        } catch (Exception e) {
            System.out.println("Exception "+e.toString());
        }
    }

    private static void parseResponse(HttpsURLConnection connection, OutputStreamWriter wr) throws IOException {
        InputStream is;BufferedReader bufferedReader;
        is = connection.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuffer lines = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            lines.append(line).append(Util.LINE_BREAKER);
        }
        System.out.println("response from " + Util.TARGET_URL + ":" + Util.LINE_BREAKER + lines);
        wr.close();
        bufferedReader.close();
        System.out.println("response code " + connection.getResponseCode());
        Map<String,List<String>> headerFields = connection.getHeaderFields();
        System.out.println("Header fields:\n"+headerFields.toString());
    }

    private static OutputStreamWriter getRequest(HttpsURLConnection connection) throws IOException {
        Util.setHeaders(connection);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(Util.DATA);
        return wr;
    }

    private static HttpsURLConnection getClient() throws IOException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException {
        URL url;
        HttpsURLConnection connection;
        url = new URL(Util.TARGET_URL);
        connection = (HttpsURLConnection) url.openConnection();
        SSLContext sslContext = Util.getSslContext();
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        connection.setSSLSocketFactory(sslSocketFactory);
        return connection;
    }



}
