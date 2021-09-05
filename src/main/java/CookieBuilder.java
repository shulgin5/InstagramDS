import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieBuilder {

    private HashMap<String, String> cookies;

    public CookieBuilder(HttpResponse<String> httpResponse) {
        this.cookies = parseCookies(httpResponse);
    }

    private static HashMap<String, String> parseCookies(HttpResponse<String> response) {
        HashMap<String, String> cookies = new HashMap<>();
        List<String> setCookies = response.headers().allValues("set-cookie");
        for(String cookie : setCookies) {
            String[] params = cookie.split(";")[0].split("=");
            String cookieName = params[0];
            String cookieValue = params[1];
            cookies.put(cookieName, cookieValue);
        }
        return cookies;
    }

    public String getCookieByName(String name) {
        return cookies.get(name);
    }

    public boolean containCookie(String name) {
        return cookies.containsKey(name);
    }

    @Override
    public String toString() {
        String cookieString = "";
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            cookieString += cookie.getKey() + "=" + cookie.getValue() + "; ";
        }
        return cookieString;
    }
}
