package com.hfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.R;
import com.hfinder.entities.Logement;
import com.hfinder.entities.LogementTest;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by root on 05/06/18.
 */

public class ListLogementAdapter extends RecyclerView.Adapter<ListLogementAdapter.LogementviewHolder>{

    private List<Logement> list;
    private LogementListClickListener listListener;

    public ListLogementAdapter(List<Logement> list, LogementListClickListener listener){
        this.list = list;
        this.listListener = listener;
    }

    public List<Logement> getList() {
        return list;
    }

    public void setList(List<Logement> list) {
        this.list = list;
    }

    public LogementListClickListener getListListener() {
        return listListener;
    }

    public void setListListener(LogementListClickListener listListener) {
        this.listListener = listListener;
    }

    @NonNull
    @Override
    public LogementviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.home_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new LogementviewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LogementviewHolder holder, final int position) {
        Logement l = list.get(position);
        holder.bind(l);
        /*
        if(l.getPicture() == null && l.getPictureLoadListener() == null){
            l.setPictureLoadListener(new Logement.PictureLoadListener() {
                @Override
                public void onLoadCompleted() {
                    notifyItemChanged(position);
                }
            });
            l.loadPicture();

        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LogementviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView pinterest;
        private ImageView interest;
        private ImageView firstImage;
        Context c;

        public LogementviewHolder(View view, Context c){
            super(view);
            this.c = c;
            title = (TextView)view.findViewById(R.id.title_item_home);
            pinterest = (TextView)view.findViewById(R.id.item_home_pinterest);
            interest = (ImageView) view.findViewById(R.id.item_home_interest);
            firstImage = (ImageView)view.findViewById(R.id.img_item_home);


            firstImage.setOnClickListener(this);
            interest.setOnClickListener(this);
        }

        public void bind(Logement logement){
            title.setText(logement.getLibelle());
            pinterest.setText(logement.getLieu());
            //firstImage.setImageBitmap(logement.getPicture());

            try{

                if(logement.getImageList().size() != 0){
                    Picasso.with(c)
                            .load(logement.getImageList().get(0).getUrl())
                            .error(R.drawable.hfinderx)
                            .placeholder(R.drawable.hfinderx).fit().centerCrop().into(firstImage);
                }else{
                    firstImage.setImageBitmap(null);
                    Picasso.with(c)
                            .load("serre")
                            .error(R.drawable.hfinderx)
                            .placeholder(R.drawable.loader).fit().centerCrop().into(firstImage);
                }
            }catch(Exception e){
                Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG);
            }




        }

        @Override
        public void onClick(View v) {
            listListener.onLogementItemClick(list.get(getAdapterPosition()), v.getId());
        }
    }

    public interface LogementListClickListener{
        public void onLogementItemClick(Logement logement, int viewId);
    }
}
