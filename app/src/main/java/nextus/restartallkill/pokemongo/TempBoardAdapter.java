package nextus.restartallkill.pokemongo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nextus.restartallkill.pokemongo.util.MyApplication;

/**
 * Created by chosw on 2016-07-19.
 */
public class TempBoardAdapter extends RecyclerView.Adapter<TempBoardAdapter.ViewHolder>
{

    public Context mContext;
    public int count_image = 1;
    public ArrayList<Bitmap> img_list;
    OnItemClickListener mItemClickListener;
    OnClickListener mOnClickListener;

    public TempBoardAdapter(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public TempBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_recycler_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TempBoardAdapter.ViewHolder holder, int position) {
        holder.board_title.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_title());
        holder.user_id.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).getUser_id());
        holder.date.setText(MyApplication.getInstance().boardItem.getBoardData().get(position).getDate());

        Glide.with(mContext).load(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_img()).into(holder.board_img);
        /*
        if ( Integer.parseInt(MyApplication.getInstance().boardItem.getBoardData().get(position).getImage_count()) == 0 )
        {
            holder.board_img.setVisibility(View.GONE);
        }
        else
        {
            //holder.board_img.setImageURI(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_img());
            //Picasso.with(mContext).load(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_img()).into(holder.board_img);

           //holder.board_img.setImageUrl(MyApplication.getInstance().boardItem.getBoardData().get(position).getBoard_img(), MyApplication.getInstance().getImageLoader());
        }*/
    }

    @Override
    public int getItemCount() {
        if(MyApplication.getInstance().boardItem == null )
            return 0;
        else
            return MyApplication.getInstance().boardItem.getBoardData().size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public void setOnClickListener(final OnClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView board_title;
        TextView user_id;
        TextView date;
        ImageView board_img;
        ImageView user_icon;

        public ViewHolder(View itemView)
        {
            super(itemView);

            board_title = (TextView) itemView.findViewById(R.id.board_title);
            user_id = (TextView) itemView.findViewById(R.id.user_id);
            date = (TextView) itemView.findViewById(R.id.date);
            board_img = (ImageView) itemView.findViewById(R.id.board_img);
            user_icon = (ImageView) itemView.findViewById(R.id.user_icon);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }

            if(mOnClickListener != null){
                mOnClickListener.onClick(view, getPosition());
            }
        }
    }
}
