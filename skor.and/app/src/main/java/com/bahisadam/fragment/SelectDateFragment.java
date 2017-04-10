package com.bahisadam.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import com.bahisadam.utility.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by atata on 21/11/2016.
 */

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    FootballFragment frag;

    public static SelectDateFragment newInstance(FootballFragment frag) {
        SelectDateFragment selectDateFragment = new SelectDateFragment();
        selectDateFragment.frag = frag;
        return selectDateFragment;
    }

    public SelectDateFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm, dd);
    }
    public void populateSetDate(int year, int month, int day) {

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        newDate.set(Calendar.HOUR_OF_DAY,3);
        newDate.set(Calendar.SECOND,0);
        newDate.set(Calendar.MINUTE,0);
        newDate.set(Calendar.MILLISECOND,0);
        Locale trlocale = Locale.forLanguageTag("tr-TR");
        String dateSelected = Utilities.toJSONDateString(newDate.getTime());
        frag.currentDate = newDate.getTime();
        frag.setSelectedDate(dateSelected,true);
        getDialog().dismiss();
    }

}