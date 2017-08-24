package manga.mangaapp.manymanga.helpers;

import manga.mangaapp.manymanga.data.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class JsoupHelper {

    private final static int NUMBER_OF_TRIES = 5;
    private final static int MAX_BODY_SIZE = 0; // Unlimited (limited only by RAM)

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0";
    private final static String USER_AGENT_MOBILE = "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19";

    private JsoupHelper() {
    }

    public static Document getHTMLPage(String url) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE)
                        .timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).get();
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageWithPost(String url,
            Map<String, String> post) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE)
                        .timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).data(post).post();
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static Document getHTMLPageMobile(String url) throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(url).maxBodySize(MAX_BODY_SIZE)
                        .timeout((i + 1) * 3000).userAgent(USER_AGENT_MOBILE)
                        .ignoreHttpErrors(true).get();
            } catch (Exception e) {
                System.err.println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                        + ". Link: " + url + ". Error: " + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImage(String imageLink, String referrer)
            throws Exception {
        Exception ex = null;

        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            try {
                return Jsoup.connect(imageLink).maxBodySize(MAX_BODY_SIZE)
                        .timeout((i + 1) * 3000).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).referrer(referrer)
                        .ignoreContentType(true).execute().bodyAsBytes();
            } catch (Exception e) {
                System.err
                        .println("Try " + (i + 1) + " of " + NUMBER_OF_TRIES
                                + ". Link: " + imageLink + ". Error: "
                                + e.getMessage());
                ex = e;
            }
        }

        throw ex;
    }

    public static byte[] getImageWithFragment(Image images) throws Exception {
        byte[] imgByte1 = getImage(images.getLink(), images.getReferrer());
        byte[] imgByte2 = getImage(images.getLinkFragment(),
                images.getReferrer());

        byte[] imageInByte = new byte[0];

        return imageInByte;
    }

    // http://nanashi07.blogspot.de/2014/06/enable-ssl-connection-for-jsoup.html
    public static void deactivateCertificateCheck() {
        try {
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname,
                                SSLSession session) {
                            return true;
                        }
                    });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context
                    .getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            System.err
                    .println("JsoupHelper.deactivateCertificateCheck() error: "
                            + ex.getMessage());
        }
    }

}
