package com.remirobert.remirobert.signalstrentgh;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by remirobert on 23/07/16.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<CellularTower> mCellularTowers;

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView subtitle;

        RecordViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.person_name);
            subtitle = (TextView)itemView.findViewById(R.id.person_age);
        }
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position) {
        final CellularTower cellularTower = mCellularTowers.get(position);
        holder.title.setText("CID Title : " + cellularTower.getCid());
        holder.subtitle.setText("Subtitle LAC : " + cellularTower.getLac());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(holder.itemView.getContext(), DetailRecordActivity.class);
                myIntent.putExtra("record", cellularTower.getId());
                holder.itemView.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCellularTowers.size();
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        RecordViewHolder pvh = new RecordViewHolder(v);
        return pvh;
    }

    public RecordAdapter(List<CellularTower> cellularTowers) {
        mCellularTowers = cellularTowers;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
