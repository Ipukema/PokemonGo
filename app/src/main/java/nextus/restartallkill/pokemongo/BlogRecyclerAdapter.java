package nextus.restartallkill.pokemongo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import nextus.restartallkill.pokemongo.util.MyApplication;


/**
 * Created by ReStartAllKill on 2016-07-05.
 */
public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>  {

    private int adapter_type=0;
    private Context mContext;
    OnItemClickListener mItemClickListener;

    public BlogRecyclerAdapter(Context context, int type)
    {
        mContext = context;
        adapter_type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.blog_img.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.blog_img.setImageUrl(MyApplication.getInstance().blogItem.getBlogData().get(position).getBl_img(), MyApplication.getInstance().getImageLoader());

        holder.title.setText(MyApplication.getInstance().blogItem.getBlogData().get(position).bl_title);
    //    holder.count.setText("조회수 "+MyApplication.getInstance().blogItem.getBlogData().get(position).bl_count);
        holder.date.setText(MyApplication.getInstance().blogItem.getBlogData().get(position).bl_date);
    }

    @Override
    public int getItemCount()
    {
        return MyApplication.getInstance().blogItem.getBlogData().size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        /*
        public TextView board_title;
        public TextView board_info;
        public NetworkImageView board_img;
        public TextView user_id;
        public TextView date;
        public ImageView user_icon;
*/
        public NetworkImageView blog_img;

        public CardView place_holder;
        public TextView title;
        public TextView count;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            blog_img = (NetworkImageView) itemView.findViewById(R.id.bl_img);

            place_holder = (CardView) itemView.findViewById(R.id.placeCard);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
            place_holder.setOnClickListener(this);
/*
            board_title = (TextView) itemView.findViewById(R.id.board_title);
            board_info = (TextView) itemView.findViewById(R.id.board_info);
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            date = (TextView) itemView.findViewById(R.id.date);

            board_img = (NetworkImageView) itemView.findViewById(R.id.board_img);
            user_icon = (ImageView) itemView.findViewById(R.id.user_icon);
*/
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }






}
