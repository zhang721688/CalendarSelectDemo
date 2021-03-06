package com.zxn.calendar.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxn.calendar.R;
import com.zxn.calendar.entity.HeadTimeEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by richzjc on 18/3/13.
 */

public class OuterRecycleViewHolder extends RecyclerView.ViewHolder {

    TextView txtMonth;
    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM");

    public OuterRecycleViewHolder(View itemView) {
        super(itemView);
        txtMonth = itemView.findViewById(R.id.plan_time_txt_month);
    }

    public void doBindData(HeadTimeEntity timeEntity) {
        //String title = itemView.getContext().getString(R.string.outer_title, String.valueOf(timeEntity.year), String.valueOf(timeEntity.month + 1));
        txtMonth.setText(formatTitle(timeEntity));
    }

    private String formatTitle(HeadTimeEntity timeEntity) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, timeEntity.year);
        c.set(Calendar.MONTH, timeEntity.month);
        return mSimpleDateFormat.format(c.getTime());
    }

}
