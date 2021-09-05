import java.util.Objects;

public class Profile {
    public long pk;
    public String username;
    public String full_name;
    public boolean is_private;
    public String profile_pic_url;
    public String profile_pic_id;
    public boolean is_verified;
    public long media_count;
    public long follower_count;
    public long following_count;
    public String biography;

    @Override
    public String toString() {
        return "Profile:\n"
                + "pk: " + pk + "\n"
                + "username: " + username + "\n"
                + "full_name: " + full_name + "\n"
                + "is_private: " + is_private + "\n"
                + "profile_pic_url: " + profile_pic_url + "\n"
                + "profile_pic_id: " + profile_pic_id + "\n"
                + "is_verified: " + is_verified + "\n"
                + "media_count: " + media_count + "\n"
                + "follower_count: " + follower_count + "\n"
                + "following_count: " + following_count + "\n"
                + "biography: " + biography + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return pk == profile.pk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk);
    }
}
