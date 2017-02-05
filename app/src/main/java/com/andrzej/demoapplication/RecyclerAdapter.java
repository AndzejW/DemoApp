package com.andrzej.demoapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrzej on 04.02.2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<Element> items;
    private int layout;

    public RecyclerAdapter(List<Element> items, int layout){
        this.items=items;
        this.layout=layout;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Element item = items.get(position);
        holder.title.setText(item.title);
        holder.desc.setText(item.description);
        holder.lang.setText(item.language);
        holder.watch.setText(String.valueOf(item.watchers));
        holder.fork.setText(String.valueOf(item.forks));
        try {
            holder.lastUp.setText(String.format("Updated at: %s", newDate(item.lastUpdate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setTag(item);
    }
    private String newDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = sdf.parse(date);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        return sdf2.format(parsedDate);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Element> items){
        this.items=items;
        notifyDataSetChanged();
    }
    public void add(Element item, int position){
        items.add(position,item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView desc;
        public TextView lang;
        public TextView watch;
        public TextView fork;
        public TextView lastUp;
        public ViewHolder(View itemView) {
            super(itemView);
            title =(TextView) itemView.findViewById(R.id.element_title);
            desc =(TextView) itemView.findViewById(R.id.element_description);
            lang =(TextView) itemView.findViewById(R.id.element_language);
            watch =(TextView) itemView.findViewById(R.id.element_watchers);
            fork =(TextView) itemView.findViewById(R.id.element_fork);
            lastUp =(TextView) itemView.findViewById(R.id.element_lastUpdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    Element element = items.get(getAdapterPosition());
                    intent.putExtra(Element.ELEMENT_TAG,element);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
