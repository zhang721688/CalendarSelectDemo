package com.zxn.calendar.holder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;


import com.zxn.calendar.R;
import com.zxn.calendar.entity.DayTimeEntity;
import com.zxn.calendar.utils.Util;

import java.util.Calendar;

/**
 * Updated by zxn on 2020/8/11.
 */
public class InnerViewHolder extends RecyclerView.ViewHolder {

    View dot;
    View leftView;
    View rightView;
    TextView date;

    Calendar startCalendarDate;
    Calendar endCalendarDate;
    Calendar tempCalendar;
    Calendar todayCalendar;
    DayTimeEntity startDayTime;
    DayTimeEntity endDayTime;
    private Drawable mSelectBgDrawable;
    private Drawable mIntervalSelectBgDrawable;
    private int mIntervalSelectBgColor;
    private int mWeekendColor;

    public InnerViewHolder(View itemView, Calendar startCalendarDate, Calendar endCalendarDate, DayTimeEntity startDayTime, DayTimeEntity endDayTime) {
        super(itemView);

        leftView = itemView.findViewById(R.id.left_view);
        rightView = itemView.findViewById(R.id.right_view);

        date = itemView.findViewById(R.id.date);

        dot = itemView.findViewById(R.id.dot);
        todayCalendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();
        setCalendarZero(todayCalendar);
        setCalendarZero(tempCalendar);
        this.startCalendarDate = startCalendarDate;
        this.endCalendarDate = endCalendarDate;
        this.startDayTime = startDayTime;
        this.endDayTime = endDayTime;
    }

    private void setCalendarZero(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void doBindData(DayTimeEntity entity) {
        tempCalendar.set(Calendar.YEAR, entity.year);
        tempCalendar.set(Calendar.MONTH, entity.month);
        tempCalendar.set(Calendar.DATE, entity.day);
        if (entity.day == 0) {
            responseDayIsZero(entity);
        } else if ((tempCalendar.getTimeInMillis() >= startCalendarDate.getTimeInMillis())
                && (tempCalendar.getTimeInMillis() <= endCalendarDate.getTimeInMillis())) {
            //框定可选时间的范围.
            responseInner(entity);
        } else {
            responseOuter(entity);
        }
    }

    private void responseInner(DayTimeEntity dayTimeEntity) {
        itemView.setEnabled(true);
        if (tempCalendar.getTimeInMillis() == todayCalendar.getTimeInMillis()) {
            setSelectItemBg(dayTimeEntity, true);
        } else {
            setSelectItemBg(dayTimeEntity, false);
        }
    }

    private void responseDayIsZero(DayTimeEntity dayTimeEntity) {
        updateDayIsZeroView();
        boolean flag = (startDayTime.day != 0) && (endDayTime.day != 0);
        boolean value = (startDayTime.year != endDayTime.year)
                || (startDayTime.month != endDayTime.month)
                || (startDayTime.day != endDayTime.day);
        boolean temp = (dayTimeEntity.listPosition > startDayTime.listPosition) && (dayTimeEntity.listPosition < endDayTime.listPosition);
        if (flag && value && temp) {
            rightView.setBackgroundColor(mIntervalSelectBgColor);
            leftView.setBackgroundColor(mIntervalSelectBgColor);

        } else {
            int color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
            rightView.setBackgroundColor(color);
            leftView.setBackgroundColor(color);
        }
    }

    private void responseOuter(DayTimeEntity dayTimeEntity) {
        itemView.setEnabled(false);
        int color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
        leftView.setBackgroundColor(color);
        rightView.setBackgroundColor(color);


        date.setBackgroundColor(Color.TRANSPARENT);
        if (tempCalendar.getTimeInMillis() == todayCalendar.getTimeInMillis()) {
            date.setText(Util.fillZero(dayTimeEntity.day));
            dot.setVisibility(View.VISIBLE);
            dot.setBackgroundResource(R.drawable.global_drawable_circle_gray);
        } else {
            date.setText(Util.fillZero(dayTimeEntity.day));
            dot.setVisibility(View.GONE);
        }
        date.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.day_mode_text_color_b3b3b3));

    }

    private void updateDayIsZeroView() {
        date.setText("");
        dot.setVisibility(View.GONE);
        itemView.setEnabled(false);
        date.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setSelectItemBg(DayTimeEntity entity, boolean isToday) {
        if (startDayTime.day == 0 && endDayTime.day == 0) {
            unselectStartAndEndTIme(entity, isToday);
        } else {
            boolean flag = (startDayTime.year == endDayTime.year)
                    && (startDayTime.month == endDayTime.month)
                    && (startDayTime.day == endDayTime.day);
            if (flag) {
                updateDateBg(entity, startDayTime, isToday);
            } else if (startDayTime.day != 0 && endDayTime.day == 0) {
                updateDateBg(entity, startDayTime, isToday);
            } else if (startDayTime.day == 0 && endDayTime.day != 0) {
                updateDateBg(entity, endDayTime, isToday);
            } else if (startDayTime.day != 0 && endDayTime.day != 0) {
                date.setText(Util.fillZero(entity.day));
                responseToRange(entity, isToday);
            }
        }
    }

    private void responseToRange(DayTimeEntity entity, boolean isToday) {
        if ((startDayTime.listPosition >= 0) && (startDayTime.listPosition == entity.listPosition)) {
            updateDateBg(entity, startDayTime, isToday);
            rightView.setBackgroundColor(mIntervalSelectBgColor);
            int color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
            leftView.setBackgroundColor(color);
        } else if ((startDayTime.listPosition >= 0)
                && (endDayTime.listPosition >= 0)
                && (entity.listPosition > startDayTime.listPosition)
                && (entity.listPosition < endDayTime.listPosition)) {
            updateDateBg(entity, startDayTime, isToday);
            rightView.setBackgroundColor(mIntervalSelectBgColor);
            leftView.setBackgroundColor(mIntervalSelectBgColor);
            date.setBackgroundColor(Color.TRANSPARENT);
        } else if ((endDayTime.listPosition >= 0) && (endDayTime.listPosition == entity.listPosition)) {
            updateDateBg(entity, endDayTime, isToday);
            leftView.setBackgroundColor(mIntervalSelectBgColor);
            int color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
            rightView.setBackgroundColor(color);
        } else {
            updateDateBg(entity, startDayTime, isToday);
        }
    }

    private void unselectStartAndEndTIme(DayTimeEntity entity, boolean isToday) {
        int color;
        if (isToday) {
            color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_text_color_1482f0);
            dot.setVisibility(View.VISIBLE);
            if (null == mSelectBgDrawable) {
                dot.setBackgroundResource(R.drawable.global_drawable_circle_select);
            } else {
                dot.setBackgroundDrawable(mSelectBgDrawable);
            }
        } else {
            dot.setVisibility(View.GONE);
//            dot.setVisibility(View.VISIBLE);
            color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_text_color);
        }
        date.setText(Util.fillZero(entity.day));
        date.setTextColor(color);
        date.setBackgroundColor(Color.TRANSPARENT);
        color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
        leftView.setBackgroundColor(color);
        rightView.setBackgroundColor(color);
    }

    private void updateDateBg(DayTimeEntity entity, DayTimeEntity tempTimeEntity, boolean isToday) {
        int color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_background_color);
        leftView.setBackgroundColor(color);
        rightView.setBackgroundColor(color);
        date.setText(Util.fillZero(entity.day));
        boolean flag;
        flag = (tempTimeEntity.year == entity.year) && (tempTimeEntity.month == entity.month) && (tempTimeEntity.day == entity.day);
        if (flag) {
            if (null == mSelectBgDrawable) {
                date.setBackgroundResource(R.drawable.global_drawable_circle_select);
            } else {
                date.setBackgroundDrawable(mSelectBgDrawable);
            }
            date.setTextColor(Color.WHITE);
            dot.setVisibility(View.GONE);
        } else if (isToday) {
            date.setBackgroundColor(Color.TRANSPARENT);
            color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_text_color_1482f0);
            date.setTextColor(color);
            dot.setVisibility(View.VISIBLE);
            if (null != mSelectBgDrawable) {
                dot.setBackgroundDrawable(mSelectBgDrawable);
            }
            if (tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                date.setTextColor(mWeekendColor);
            }
        } else {
            color = ContextCompat.getColor(itemView.getContext(), R.color.day_mode_text_color);
            date.setTextColor(color);
            date.setBackgroundColor(Color.TRANSPARENT);
            dot.setVisibility(View.GONE);

            if (tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                date.setTextColor(mWeekendColor);
            }
        }
    }

    public void setSelectBgDrawable(Drawable drawable) {
        this.mSelectBgDrawable = drawable;
    }

    public void setIntervalSelectBgDrawable(Drawable drawable) {
        this.mIntervalSelectBgDrawable = drawable;
    }

    public void setIntervalSelectBgColor(int color) {
        this.mIntervalSelectBgColor = color;
    }

    public void setWeekendColor(int color) {
        mWeekendColor = color;
    }
}
