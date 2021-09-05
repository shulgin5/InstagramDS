import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class InstJson {

    public static List<Profile> parseAllFollow(String jsonString) {
        List<Profile> profiles = new ArrayList<>(100);
        try {
            Object objectResponse = new JSONParser().parse(jsonString);
            JSONObject jsonObjectAll = (JSONObject) objectResponse;
            JSONArray jsonObjectUsers = (JSONArray) jsonObjectAll.get("users");
            for(Object user : jsonObjectUsers) {
                Profile profile = new Profile();
                JSONObject jsonUser = (JSONObject) user;
                profile.pk = (long) jsonUser.get("pk");
                profile.username = (String) jsonUser.get("username");
                profile.full_name = (String) jsonUser.get("full_name");
                profile.is_private = (boolean) jsonUser.get("is_private");
                profile.is_verified = (boolean) jsonUser.get("is_verified");
                profile.profile_pic_url = (String) jsonUser.get("profile_pic_url");
                profile.profile_pic_id = (String) jsonUser.get("profile_pic_id");
                profiles.add(profile);
            }
        } catch(ParseException exception) {
            exception.printStackTrace();
        }
        return profiles;
    }

    public static Profile getUserInfo(String jsonString) {
        Profile profile = new Profile();
        try {
            Object objectResponse = new JSONParser().parse(jsonString);
            JSONObject jsonObjectAll = (JSONObject) objectResponse;
            JSONObject jsonObjectUserProperties = (JSONObject) jsonObjectAll.get("user");
            profile.pk = (long) jsonObjectUserProperties.get("pk");
            profile.username = (String) jsonObjectUserProperties.get("username");
            profile.full_name = (String) jsonObjectUserProperties.get("full_name");
            profile.biography = (String) jsonObjectUserProperties.get("biography");
            profile.is_private = (boolean) jsonObjectUserProperties.get("is_private");
            profile.is_verified = (boolean) jsonObjectUserProperties.get("is_verified");
            profile.following_count = (long) jsonObjectUserProperties.get("following_count");
            profile.follower_count = (long) jsonObjectUserProperties.get("follower_count");
            profile.profile_pic_url = (String) jsonObjectUserProperties.get("profile_pic_url");
            profile.profile_pic_id = (String) jsonObjectUserProperties.get("profile_pic_id");
            profile.media_count = (long) jsonObjectUserProperties.get("media_count");
        } catch(ParseException exception) {
            exception.printStackTrace();
        }
        return profile;
    }

    public static long getPkUser(String jsonString) {
        long pk = 0;
        try {
            Object objectResponse = new JSONParser().parse(jsonString);
            JSONObject jsonObjectAll = (JSONObject) objectResponse;
            JSONArray jsonObjectUsers = (JSONArray) jsonObjectAll.get("users");
            JSONObject jsonObjectFirstUser = null;
            JSONObject userProperty = null;
            if (jsonObjectUsers.size() != 0) {
                jsonObjectFirstUser = (JSONObject) jsonObjectUsers.get(0);
                userProperty = (JSONObject) jsonObjectFirstUser.get("user");
            }
            pk = Long.parseLong((String) userProperty.get("pk"));
        } catch(ParseException exception) {
            exception.printStackTrace();
        }
        return pk;
    }
}
