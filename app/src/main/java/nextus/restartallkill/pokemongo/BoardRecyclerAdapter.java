package nextus.restartallkill.pokemongo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import nextus.restartallkill.pokemongo.util.ImgController;
import nextus.restartallkill.pokemongo.util.MyApplication;
import nextus.restartallkill.pokemongo.util.MySingleton;


/**
 * Created by ReStartAllKill on 2016-07-05.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder> {

    private int adapter_type=0;
    private Context mContext;

    public BoardRecyclerAdapter(Context context, int type)
    {
        mContext = context;
        adapter_type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_recycler_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.board_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        holder.board_img.setImageUrl(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_img(), MyApplication.getInstance().getImageLoader());
        holder.board_title.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).board_title);
        holder.board_info.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).board_info);
        holder.user_id.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).user_id);
        holder.date.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).date);
    }

    @Override
    public int getItemCount()
    {
        return MyApplication.getInstance().boardItem.getBoardData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView board_title;
        public TextView board_info;
        public NetworkImageView board_img;
        public TextView user_id;
        public TextView date;
        public ImageView user_icon;


        public ViewHolder(View itemView) {
            super(itemView);
            board_title = (TextView) itemView.findViewById(R.id.board_title);
            board_info = (TextView) itemView.findViewById(R.id.board_info);
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            date = (TextView) itemView.findViewById(R.id.date);

            board_img = (NetworkImageView) itemView.findViewById(R.id.board_img);
            user_icon = (ImageView) itemView.findViewById(R.id.user_icon);
        }
    }





}
