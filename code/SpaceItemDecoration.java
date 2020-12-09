package com.graduationwork.tracediary;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public SpaceItemDecoration(int verticalSpaceHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        /* 마지막 전까지 divide 생성  */
        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1 ){
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
