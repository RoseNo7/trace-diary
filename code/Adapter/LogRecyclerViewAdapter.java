package com.graduationwork.tracediary.Adapter;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationwork.tracediary.DialogActivity.EditCommentDialog;
import com.graduationwork.tracediary.DialogActivity.InsertMakerDialog;
import com.graduationwork.tracediary.LocationDTO;
import com.graduationwork.tracediary.R;

import java.util.ArrayList;

public class LogRecyclerViewAdapter  extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    public static ArrayList<LocationDTO> logLocationDTOs = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_location_log_item, parent, false);

        return new LogRowcell(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((LogRowcell) holder).time_log_list_text.setText(logLocationDTOs.get(position)._time);
        ((LogRowcell) holder).place_log_list_text.setText(logLocationDTOs.get(position)._place);

        ((LogRowcell) holder).btn_add_maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent addIntent = new Intent(v.getContext(), InsertMakerDialog.class);
                addIntent.putExtra("selectAdd", position);
                v.getContext().startActivity(addIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logLocationDTOs.size();
    }


    private static class LogRowcell extends RecyclerView.ViewHolder {
        LinearLayout btn_add_maker;
        TextView time_log_list_text;
        TextView place_log_list_text;

        public LogRowcell(View view) {
            super(view);

            time_log_list_text = (TextView) view.findViewById(R.id.text_log_list_time);
            place_log_list_text = (TextView) view.findViewById(R.id.text_log_list_place);
            btn_add_maker = (LinearLayout) view.findViewById(R.id.maker_add_button);
        }
    }
}

