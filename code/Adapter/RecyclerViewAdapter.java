package com.graduationwork.tracediary.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.graduationwork.tracediary.DialogActivity.DeleteMakerDialog;
import com.graduationwork.tracediary.DialogActivity.EditCommentDialog;
import com.graduationwork.tracediary.LocationDTO;
import com.graduationwork.tracediary.R;

import java.util.ArrayList;

import static com.graduationwork.tracediary.Activity.TraceListActivity.googleMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    public static ArrayList<LocationDTO> locationDTOs = new ArrayList<>();


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_location_item, parent, false);

        return new Rowcell(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((Rowcell) holder).time_list_text.setText(locationDTOs.get(position)._time);
        ((Rowcell) holder).place_list_text.setText(locationDTOs.get(position)._place);

        /* Maker에 넣을 코멘트 넣을 다이얼로그 생성 */
        ((Rowcell) holder).btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent commentIntent = new Intent(v.getContext(), EditCommentDialog.class);
                commentIntent.putExtra("selectMaker", position);
                v.getContext().startActivity(commentIntent);
            }
        });


        ((Rowcell) holder).btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent(v.getContext(), DeleteMakerDialog.class);
                deleteIntent.putExtra("selectDelete", position);
                v.getContext().startActivity(deleteIntent);
            }
        });

        ((Rowcell) holder).btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng standLatLng = new LatLng(locationDTOs.get(position)._latitude, locationDTOs.get(position)._longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(standLatLng, googleMap.getCameraPosition().zoom));
            }
        });
    }

    /* 아이템 갯수 */
    @Override
    public int getItemCount() {
        return locationDTOs.size();
    }


    private static class Rowcell extends RecyclerView.ViewHolder {
        LinearLayout btn_item;
        LinearLayout btn_comment;
        LinearLayout btn_delete;
        TextView time_list_text;
        TextView place_list_text;

        public Rowcell(View view) {
            super(view);

            time_list_text = (TextView) view.findViewById(R.id.text_list_time);
            place_list_text = (TextView) view.findViewById(R.id.text_list_place);
            btn_comment = (LinearLayout) view.findViewById(R.id.comment_button);
            btn_delete = (LinearLayout) view.findViewById(R.id.delete_button);
            btn_item = (LinearLayout) view.findViewById(R.id.location_item);
        }
    }
}
