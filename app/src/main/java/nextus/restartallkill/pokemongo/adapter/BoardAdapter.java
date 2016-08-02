package nextus.restartallkill.pokemongo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nextus.restartallkill.pokemongo.data.BoardItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.util.MyApplication;

/**
 * Created by chosw on 2016-07-25.
 */
public class BoardAdapter extends GenericRecylerAdapter<BoardItem.Board> {

    public Context mContext;

    public BoardAdapter(Context context)
    {
        mContext = context;
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
            ImageView board_img = (ImageView) viewHolder.getView(R.id.board_img);
            TextView view_count = (TextView) viewHolder.getView(R.id.view_count);
            TextView like_count = (TextView) viewHolder.getView(R.id.like_count);
            TextView comment_count = (TextView) viewHolder.getView(R.id.comment_count);


            view_count.setText("조회수 "+item.getView_count());
            like_count.setText("좋아요 "+item.getLike_count());
            comment_count.setText("댓글 "+item.getComment_count());
            //board_img.setOnClickListener();

            ImageView user_icon = (ImageView) viewHolder.getView(R.id.user_icon);

            board_title.setText(item.getBoard_title());
            user_id.setText(item.getUser_id());
            date.setText(item.getDate());

            //ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
            //board_img.setImageUrl(item.getBoard_img(), MyApplication.getInstance().getImageLoader());

            Glide.with(mContext).load(item.getBoard_img()).crossFade().thumbnail(0.1f).fitCenter().into(board_img);

            String date_string = item.getDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA);

            Date current = new Date(System.currentTimeMillis());


            try{
                Date d = format.parse(date_string) ;
                long time_long = current.getTime() - d.getTime();
                int time = (int)time_long/1000;

                Log.e("Time", ""+time);
                if( time < 60 )
                {
                    date.setText("방금");
                }
                else if( time >=60 && time < 3600 )
                {
                    time = time/60;
                    date.setText(""+time+"분 전");
                }
                else if( time >=3600 && time < 86400 )
                {
                    time = time/3600;
                    date.setText(""+time+"시간 전");
                }
                else
                {
                    date.setText(date_string);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }
}
