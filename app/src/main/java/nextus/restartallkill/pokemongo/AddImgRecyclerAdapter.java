package nextus.restartallkill.pokemongo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by chosw on 2016-07-19.
 */
public class AddImgRecyclerAdapter extends RecyclerView.Adapter<AddImgRecyclerAdapter.ViewHolder>
{

    public Context mContext;
    public int count_image = 1;
    public ArrayList<Bitmap> img_list;
    OnItemClickListener mItemClickListener;
    OnClickListener mOnClickListener;

    public AddImgRecyclerAdapter(Context mContext, ArrayList<Bitmap> list)
    {
        this.mContext = mContext;
        img_list = list;
    }

    @Override
    public AddImgRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddImgRecyclerAdapter.ViewHolder holder, int position) {
        Log.e("Position",""+position);
        if(position == getItemCount()-1) //last
        {
            holder.imageView.setImageResource(0);
            holder.cancle.setVisibility(View.INVISIBLE);
            holder.addButton.setImageResource(R.drawable.ic_photo_library_black_24dp);
        }
        else
        {
            holder.imageView.setImageBitmap(img_list.get(position));
            holder.cancle.setVisibility(View.VISIBLE);
            holder.addButton.setImageResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return img_list.size()+1;
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

        ImageView imageView;
        ImageView cancle;
        ImageView addButton;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.addImagebutton);
            cancle = (ImageView)itemView.findViewById(R.id.cancel_button);
            addButton = (ImageView)itemView.findViewById(R.id.addImagebutton_icon);
            cancle.setOnClickListener(this);
            imageView.setOnClickListener(this);
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
