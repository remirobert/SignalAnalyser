package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by remirobert on 23/07/16.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> mRecordList;
    private Context mContext;

    public RecordAdapter(List<Record> recordList, Context context) {
        mRecordList = Lists.reverse(recordList);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position) {
        final Record record = mRecordList.get(position);
        holder.title.setText("#" + (mRecordList.size() - position));

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        String date = format.format(java.sql.Date.parse(record.getDate().toString()));
        holder.subtitle.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(holder.itemView.getContext(), DetailRecordActivity.class);
                myIntent.putExtra("record", record.getId());
                holder.itemView.getContext().startActivity(myIntent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_record_list, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Realm realm = Realm.getDefaultInstance();
                        final RealmResults<Record> results = realm.where(Record.class).equalTo("mId", record.getId()).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                results.deleteFirstFromRealm();
                            }
                        });
                        SignalActivity signalActivity = (SignalActivity) mContext;
                        signalActivity.initListRecords();
                        return true;
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        RecordViewHolder pvh = new RecordViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView subtitle;

        RecordViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.person_name);
            subtitle = (TextView) itemView.findViewById(R.id.person_age);
        }
    }
}
