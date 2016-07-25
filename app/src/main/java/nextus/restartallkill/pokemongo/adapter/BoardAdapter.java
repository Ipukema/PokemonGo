package nextus.restartallkill.pokemongo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import nextus.restartallkill.pokemongo.data.BoardItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.util.MyApplication;

/**
 * Created by chosw on 2016-07-25.
 */
public class BoardAdapter extends GenericRecylerAdapter<BoardItem.Board> {

    public BoardAdapter() {

    }

    @Override
    protected View createView(ViewGroup viewGroup, int viewType) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_item_recycler_img, viewGroup, false);
        return item;
    }

    @Override
    public void bindView(BoardItem.Board item, ViewHolder viewHolder) {
        if(item != null)
        {
            TextView board_title = (TextView) viewHolder.getView(R.id.board_title);
            TextView user_id = (TextView) viewHolder.getView(R.id.user_id);
            TextView date = (TextView) viewHolder.getView(R.id.date);
            NetworkImageView board_img = (NetworkImageView) viewHolder.getView(R.id.board_img);
            ImageView user_icon = (ImageView) viewHolder.getView(R.id.user_icon);

            board_title.setText(item.getBoard_title());
            user_id.setText(item.getUser_id());
            date.setText(item.getDate());
            board_img.setImageUrl(item.getBoard_img(), MyApplication.getInstance().getImageLoader());
        }
    }
}
