
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

public class InstClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String username;
    private final String password;
    private final String authRequestBody;
    private CookieBuilder cookies;

    public InstClient(String username, String password) {
        this.username = username;
        this.password = password;
        Date date = new Date();
        this.authRequestBody = "username=" + username
                + "&enc_password=#PWD_INSTAGRAM_BROWSER:0:" + date.getTime()
                + ":" + password + "&queryParams={}&optIntoOneTap=false";
        authorization();
    }

    private void authorization(){
        HttpResponse<String> response = sendRequestOnMainPage();
        if(response.statusCode() != 200) {
            throw new RuntimeException("Connection error!");
        }
        response = sendRequestOnAuthorization();
        if(response.statusCode() != 200 || !cookies.containCookie("ds_user_id")) {
            throw new RuntimeException("Connection error!");
        }
    }

    private HttpResponse<String> sendRequestOnMainPage(){
        HttpResponse<String> httpResponse = null;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(InstConfig.URL))
                    .header("user-agent", InstConfig.USER_AGENT)
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        cookies = new CookieBuilder(httpResponse);
        return httpResponse;
    }

    private HttpResponse<String> sendRequestOnAuthorization() {
        HttpResponse<String> httpResponse = null;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(InstConfig.LOGIN_URL))
                    .header("accept", InstConfig.ACCEPT)
                    .header("accept-encoding", InstConfig.ACCEPT_ENCODING)
                    .header("accept-language", InstConfig.ACCEPT_LANGUAGE)
                    .header("content-type", InstConfig.CONTENT_TYPE)
                    .header("cookie", cookies.toString())
                    .header("user-agent", InstConfig.USER_AGENT)
                    .header("x-asbd-id", InstConfig.X_ASBD_ID)
                    .header("x-csrftoken", cookies.getCookieByName("csrftoken"))
                    .header("x-ig-app-id", InstConfig.X_IG_APP_ID)
                    .header("x-ig-www-claim", InstConfig.X_IG_WWW_CLAIM)
                    .header("x-instagram-ajax", InstConfig.X_INSTAGRAM_AJAX)
                    .header("x-requested-with", InstConfig.X_REQUESTED_WITH)
                    .POST(HttpRequest.BodyPublishers.ofString(authRequestBody))
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        cookies = new CookieBuilder(httpResponse);
        return httpResponse;
    }

    private long getUserId(String name) {
        HttpResponse<String> httpResponse = null;
        double randomNumber = Math.random();
        String requestBody = String.format("context=blended&query=%s&rank_token=%s&include_reel=true", name, randomNumber);
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(InstConfig.SEARCH_URL + "?" + requestBody))
                    .header("cookie", cookies.toString())
                    .header("user-agent", InstConfig.USER_AGENT)
                    .header("x-csrftoken", cookies.getCookieByName("csrftoken"))
                    .header("x-asbd-id", InstConfig.X_ASBD_ID)
                    .header("x-ig-app-id", InstConfig.X_IG_APP_ID)
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //cookies = new CookieBuilder(httpResponse);
        long pk = 0;
        if (httpResponse.body() != null) {
            pk = InstJson.getPkUser(httpResponse.body());
        }
        return pk;
    }

    public Profile getUser(String name) {
        HttpResponse<String> httpResponse = null;
        long pk = getUserId(name);
        try {
            String url = "https://i.instagram.com/api/v1/users/%s/info/";
            url = String.format(url, pk);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("cookie", cookies.toString())
                    .header("user-agent", InstConfig.USER_AGENT)
                    .header("sec-fetch-dest", "empty")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-site", "same-site")
                    .header("x-ig-www-claim", InstConfig.X_IG_WWW_CLAIM)
                    .header("x-ig-app-id", InstConfig.X_IG_APP_ID)
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return InstJson.getUserInfo(httpResponse.body());
    }

    public List<Profile> getAllFollowing(Profile profile) {
        HttpResponse<String> httpResponse = null;
        try {
            String url = "https://i.instagram.com/api/v1/friendships/%s/following/?count=%s";
            url = String.format(url, profile.pk, profile.following_count);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("cookie", cookies.toString())
                    .header("user-agent", InstConfig.USER_AGENT)
                    .header("x-ig-app-id", InstConfig.X_IG_APP_ID)
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return InstJson.parseAllFollow(httpResponse.body());
    }

    public List<Profile> getAllFollowers(Profile profile) {
        HttpResponse<String> httpResponse = null;
        try {
            String url = "https://i.instagram.com/api/v1/friendships/%s/followers/?count=%s&search_surface=follow_list_page";
            url = String.format(url, profile.pk, profile.follower_count);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("cookie", cookies.toString())
                    .header("user-agent", InstConfig.USER_AGENT)
                    .header("x-ig-app-id", InstConfig.X_IG_APP_ID)
                    .build();
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return InstJson.parseAllFollow(httpResponse.body());
    }
}
