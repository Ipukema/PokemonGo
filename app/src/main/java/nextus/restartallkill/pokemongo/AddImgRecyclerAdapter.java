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

        }
        else
        {
            holder.imageView.setImageBitmap(img_list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return img_list.size()+1;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.addImagebutton);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }
}
