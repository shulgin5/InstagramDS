
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        InstClient client = new InstClient("denis.developer", "password");
        Profile profile = client.getUser("");
        if(profile.is_private) {
            System.out.println("Account is private!");
            return;
        }
        List<Profile> followings = client.getAllFollowing(profile);
        List<Profile> followers = client.getAllFollowers(profile);
        followings.removeAll(followers);
        System.out.println();
        for(Profile p : followings) {
            System.out.println(p.username);
        }
    }
}
