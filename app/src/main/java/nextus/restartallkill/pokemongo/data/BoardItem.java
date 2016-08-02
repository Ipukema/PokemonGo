package nextus.restartallkill.pokemongo.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by chosw on 2016-07-16.
 */
public class BoardItem {

    @SerializedName("BoardItem") ArrayList<Board> boardData = new ArrayList<>();

    public BoardItem() {
        this.boardData = new ArrayList<>();
    }

    public ArrayList<Board> getBoardData() {
        return boardData;
    }

    public static class Board
    {
        @SerializedName("board_id") int board_id;
        @SerializedName("board_title") String board_title;
        @SerializedName("board_info") String board_info;
        @SerializedName("user_id") String user_id;
        @SerializedName("date") String date;
        @SerializedName("board_img") String board_img;
        @SerializedName("image_count") String image_count;

        public String getImage_count() {
            return image_count;
        }

        public String getBoard_img() {
            return board_img;
        }

        public int getBoard_id() {
            return board_id;
        }

        public String getBoard_title() {
            return board_title;
        }

        public String getBoard_info() {
            return board_info;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getDate() {
            return date;
        }
    }
}
