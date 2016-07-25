package nextus.restartallkill.pokemongo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import nextus.restartallkill.pokemongo.data.BlogItem;
import nextus.restartallkill.pokemongo.R;
import nextus.restartallkill.pokemongo.util.MyApplication;

/**
 * Created by chosw on 2016-07-25.
 */
public class BlogAdapter extends GenericRecylerAdapter<BlogItem.Blog> {

    private Context mContext;

    public BlogAdapter(Context mContext, OnViewHolderClick mListener) {
        super(mListener);
        this.mContext = mContext;
    }

    @Override
    protected View createView(ViewGroup viewGroup, int viewType) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_recycler_layout, viewGroup, false);
        return item;
    }

    @Override
    public void bindView(BlogItem.Blog item, ViewHolder viewHolder) {
        if(item != null)
        {
            TextView title = (TextView) viewHolder.getView(R.id.title);
            TextView count = (TextView) viewHolder.getView(R.id.count);
            TextView date = (TextView) viewHolder.getView(R.id.date);
            NetworkImageView blog_img = (NetworkImageView) viewHolder.getView(R.id.bl_img);

            title.setText(item.getBl_title());
            count.setText(item.getBl_count());
            date.setText(item.getBl_date());
            blog_img.setImageUrl(item.getBl_img(), MyApplication.getInstance().getImageLoader());
            blog_img.setScaleType(ImageView.ScaleType.FIT_XY);


        }
    }

}
