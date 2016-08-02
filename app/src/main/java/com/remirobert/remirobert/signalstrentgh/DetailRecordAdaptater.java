package com.remirobert.remirobert.signalstrentgh;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by remirobert on 02/08/16.
 */
public class DetailRecordAdaptater extends RecyclerView.Adapter<DetailRecordAdaptater.RecordInfoHolder> {

    private List<List<String>> mLists;

    public static class RecordInfoHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView title;
            TextView subtitle;

            RecordInfoHolder(final View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                title = (TextView)itemView.findViewById(R.id.person_name);
                subtitle = (TextView)itemView.findViewById(R.id.person_age);
            }
        }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public void onBindViewHolder(RecordInfoHolder holder, int position) {

    }

    @Override
    public RecordInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public DetailRecordAdaptater(List<List<String>> lists) {
        mLists = lists;
    }
}
