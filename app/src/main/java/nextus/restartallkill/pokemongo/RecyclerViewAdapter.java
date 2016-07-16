package nextus.restartallkill.pokemongo;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ReStartAllKill on 2016-07-05.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int adapter_type=0;
    ArrayList<Integer> glossary_img = new ArrayList<>();
    ArrayList<String> glossary_title = new ArrayList<>();
    ArrayList<String> glossary_info = new ArrayList<>();
    ArrayList<String> term_title = new ArrayList<>();
    ArrayList<String> term_info = new ArrayList<>();

    public RecyclerViewAdapter(Context context, int type)
    {
        adapter_type = type;
        for(int i=0; i<8; i++)
            glossary_img.add(R.drawable.glossary_01+i);

        glossary_title.add(context.getString(R.string.glossary_title01));glossary_title.add(context.getString(R.string.glossary_title02));glossary_title.add(context.getString(R.string.glossary_title03));glossary_title.add(context.getString(R.string.glossary_title04));
        glossary_title.add(context.getString(R.string.glossary_title05));glossary_title.add(context.getString(R.string.glossary_title06));glossary_title.add(context.getString(R.string.glossary_title07));glossary_title.add(context.getString(R.string.glossary_title08));
        glossary_info.add(context.getString(R.string.glossary_info01));glossary_info.add(context.getString(R.string.glossary_info02));glossary_info.add(context.getString(R.string.glossary_info03));glossary_info.add(context.getString(R.string.glossary_info04));
        glossary_info.add(context.getString(R.string.glossary_info05));glossary_info.add(context.getString(R.string.glossary_info06));glossary_info.add(context.getString(R.string.glossary_info07));glossary_info.add(context.getString(R.string.glossary_info08));

        for(int i=0; i<11; i++)
        {
            term_title.add(context.getString(R.string.term_title01+i));
            term_info.add(context.getString(R.string.term_info01+i));

        }
        term_title.add(context.getString(R.string.term_title11));  term_title.add(context.getString(R.string.term_title12));
        term_info.add(context.getString(R.string.term_info11));term_info.add(context.getString(R.string.term_info12));
        term_title.add(context.getString(R.string.term_title13));term_title.add(context.getString(R.string.term_title14));term_title.add(context.getString(R.string.term_title15));term_title.add(context.getString(R.string.term_title16));term_title.add(context.getString(R.string.term_title17));term_title.add(context.getString(R.string.term_title18));
        term_info.add(context.getString(R.string.term_info13));term_info.add(context.getString(R.string.term_info14));term_info.add(context.getString(R.string.term_info15));term_info.add(context.getString(R.string.term_info16));term_info.add(context.getString(R.string.term_info17));term_info.add(context.getString(R.string.term_info18));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_01, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if( adapter_type == 0 )
        {
            holder.placeImage.setImageResource(glossary_img.get(position));
            holder.placeTitle.setText(glossary_title.get(position));
            holder.placeInfo.setText(glossary_info.get(position));
        }
        else
        {
            holder.placeTitle.setText(term_title.get(position));
            holder.placeInfo.setText(term_info.get(position));
        }

    }

    @Override
    public int getItemCount()
    {
        if(adapter_type==0) return glossary_img.size();
        else return term_title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView placeTitle;
        public TextView placeInfo;
        public ImageView placeImage;


        public ViewHolder(View itemView) {
            super(itemView);
            placeTitle = (TextView) itemView.findViewById(R.id.placeTitle);
            placeInfo = (TextView) itemView.findViewById(R.id.placeInfo);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
        }

    }



}
