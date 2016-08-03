package com.remirobert.remirobert.signalstrentgh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by remirobert on 02/08/16.
 */
public class DetailRecordAdaptater extends RecyclerView.Adapter<DetailRecordAdaptater.RecordInfoHolder> {

    private List<ListInfo> mLists;

    public static class RecordInfoHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;

        RecordInfoHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_item_title);
            subtitle = (TextView) itemView.findViewById(R.id.list_item_content);
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public void onBindViewHolder(RecordInfoHolder holder, int position) {
        ListInfo listInfo = mLists.get(position);
        holder.title.setText(listInfo.getTitle());
        holder.subtitle.setText(listInfo.getContent());
    }

    @Override
    public RecordInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_info, parent, false);
        RecordInfoHolder pvh = new RecordInfoHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DetailRecordAdaptater(List<ListInfo> lists) {
        mLists = lists;
    }
}
