package com.example.padelwear;

import android.content.Context;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

/**
 * Created by daniel on 17/05/2017.
 */

public class MyChildLayoutManager extends CurvedChildLayoutManager {
    private static final float MAX_ICON_PROGRESS=0.65f;

    private float progressToCenter;

    public MyChildLayoutManager(Context context){
        super(context);
    }

    @Override
    public void updateChild(View child, WearableRecyclerView parent){
        super.updateChild(child,parent);

        // https://developer.android.com/training/wearables/ui/lists.html
        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        // Normalize for center
        progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        // Adjust to the maximum scale
        progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS);

        child.setScaleX(1 - progressToCenter);
        child.setScaleY(1 - progressToCenter);

    }
}
