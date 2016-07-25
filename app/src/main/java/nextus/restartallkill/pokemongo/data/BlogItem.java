package nextus.restartallkill.pokemongo.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by chosw on 2016-07-16.
 */
public class BlogItem {

    @SerializedName("bestLocation") ArrayList<Blog> blogData = new ArrayList<>();

    public BlogItem() {
        this.blogData = new ArrayList<>();
    }

    public ArrayList<Blog> getBlogData() {
        return blogData;
    }

    public static class Blog
    {
        @SerializedName("bl_title") String bl_title;
        @SerializedName("bl_info") String bl_info;
        @SerializedName("bl_img") String bl_img;
        @SerializedName("bl_url") String bl_url;
        @SerializedName("bl_count") String bl_count;
        @SerializedName("bl_date") String bl_date;

        public String getBl_title() {
            return bl_title;
        }

        public String getBl_info() {
            return bl_info;
        }

        public String getBl_img() {
            return bl_img;
        }

        public String getBl_url() {
            return bl_url;
        }

        public String getBl_count() {
            return bl_count;
        }

        public String getBl_date() {
            return bl_date;
        }
    }
}
