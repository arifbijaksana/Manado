package com.haerul.manado.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.databinding.BindingAdapter;

import com.haerul.manado.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BindingAdapterUtil {

    public final static Calendar myCalendar = Calendar.getInstance();

    @BindingAdapter(value = {"inputDate", "errorMessage"}, requireAll = false)
    public static void setInputDate(TextView editText, String dated, boolean isErorMessage) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (isErorMessage) {
                    setDate2(editText);
                } else {
                    setDate(editText);
                }
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private static void setDate(TextView et) {
        String myFormat = Constants.DATE_ONLY_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String date = sdf.format(myCalendar.getTime());
        et.setText(date.toUpperCase());
    }

    private static void setDate2(TextView et) {
        String myFormat = Constants.DATE_ONLY_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String date = sdf.format(myCalendar.getTime());
        et.setText(date.toUpperCase());
        et.setError(null);
    }

    @BindingAdapter(value = {"inputTime", "errorMessage"}, requireAll = false)
    public static void setInputTime(TextView editText, String dated, boolean isErorMessage) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(editText.getContext(), R.style.DateTimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);

        editText.setOnClickListener(v -> mTimePicker.show());
    }

    @BindingAdapter({"statusView"})
    public static void setStatusView(TextView textView, int stype) {
        switch (stype) {
            case 1:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_2);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWaringLightDark));
                return;
            case 2:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_1);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWarningExtraDark));
                return;
            case 3:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_3);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorDangerExtraDark));
                return;
            case 4:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_4);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorSuccessExtraDark));
                return;
            default:
                textView.setBackgroundResource(R.drawable.bg_label_status_null);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorTextGray0));
        }
    }

    @BindingAdapter({"statusViewGangguan"})
    public static void statusViewGangguan(TextView textView, int stype) {
        switch (stype) {
            case 1:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_01);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWaringLightDark));
                return;
            case 2:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_1);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWarningExtraDark));
                return;
            case 3:
                textView.setBackgroundResource(R.drawable.bg_label_status_null);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorPrimary));
                return;
            case 4:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_4);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorSuccessExtraDark));
                return;
            case 5:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_3);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorDangerExtraDark));
                return;
            default:
                textView.setBackgroundResource(R.drawable.bg_label_status_null);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorTextGray0));
        }
    }

    @BindingAdapter({"textColorGangguan"})
    public static void textColorGangguan(TextView textView, int stype) {
        switch (stype) {
            case 0:
            case 1:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorDanger));
                return;
            case 2:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWarningExtraDark));
                return;
            case 3:
                return;
            case 4:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorSuccessExtraDark));
                return;
            case 5:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorDangerExtraDark));
                return;
            default:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorTextGray0));
        }
    }

    @BindingAdapter({"statusViewTL"})
    public static void setStatusViewTL(TextView textView, int stype) {
        switch (stype) {
            case 1:
                textView.setBackgroundResource(R.drawable.bg_label_status_bordered_4);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorSuccessExtraDark));
                return;
            default:
                textView.setBackgroundResource(R.drawable.bg_label_status_null);
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorTextGray0));
        }
    }

    @BindingAdapter({"statusViewTL2"})
    public static void setStatusViewTL2(TextView textView, int stype) {
        switch (stype) {
            case 0:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorWaring));
                return;
            case 1:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorAccent));
                return;
            case 2:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorSuccessExtraDark));
                return;
            case 3:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorDanger));
                return;
            default:
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.colorTextGray0));
        }
    }

    @BindingAdapter({"setSendStatus"})
    public static void setSendStatus(ImageView view, boolean isSend) {
        if (isSend) {
            setImageDrawable(view, R.drawable.cloud_check);
        } else {
            setImageDrawable(view, R.drawable.cloud_alert_red);
        }
    }

    static void setImageDrawable(ImageView view, int background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImageDrawable(view.getContext().getResources().getDrawable(background));
        } else {
            view.setImageDrawable(view.getContext().getResources().getDrawable(background));
        }
    }

    @BindingAdapter({"setSendIcon"})
    public static void setSendIcon(ImageView view, boolean isSend) {
        if (isSend) {
            setImageDrawable(view, R.drawable.ic_send_gray);
        } else {
            setImageDrawable(view, R.drawable.ic_send);
        }
    }
}
