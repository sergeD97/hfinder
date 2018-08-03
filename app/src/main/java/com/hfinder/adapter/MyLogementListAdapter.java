package com.hfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfinder.R;
import com.hfinder.entities.Logement;

import java.util.List;

/**
 * Created by root on 27/06/18.
 */

public class MyLogementListAdapter extends RecyclerView.Adapter<MyLogementListAdapter.MyViewHolder> {
    List<Logement> listLogement;
    ItemClickListener listener;

    public List<Logement> getListLogement() {
        return listLogement;
    }

    public void setListLogement(List<Logement> listLogement) {
        this.listLogement = listLogement;
    }

    public ItemClickListener getListener() {
        return listener;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public MyLogementListAdapter(List<Logement> list, ItemClickListener listener){
        this.listLogement = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.my_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listLogement.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView map;
        private TextView title, subtitle;
        private ConstraintLayout itemV;

        public MyViewHolder(View itemView) {
            super(itemView);
            map = (ImageView)itemView.findViewById(R.id.map);
            title = (TextView)itemView.findViewById(R.id.title);
            subtitle = (TextView)itemView.findViewById(R.id.subtitle);
            itemV = (ConstraintLayout)itemView.findViewById(R.id.item_view);

            itemV.setOnClickListener(this);
            map.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClicked(listLogement.get(getAdapterPosition()).getId(), v.getId());
            }
        }

        public void bind(int position){
            title.setText(listLogement.get(position).getLibelle());
            subtitle.setText( listLogement.get(position).getLieu());
        }
    }

    public interface ItemClickListener{
        public void onItemClicked(Long id, int viewId);
    }
}
