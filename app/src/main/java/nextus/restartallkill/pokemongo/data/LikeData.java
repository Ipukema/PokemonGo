package nextus.restartallkill.pokemongo.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by chosw on 2016-08-05.
 */
public class LikeData {

    @SerializedName("result_array") ArrayList<Like> likeData = new ArrayList<>();

    public ArrayList<Like> getLikeData() {
        return likeData;
    }

    public static class Like {
        @SerializedName("result") String result;

        public String getResult() {
            return result;
        }

    }
}
