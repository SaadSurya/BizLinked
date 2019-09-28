package com.application.lumaque.bizlinked.helpers.ui.dialogs;

import com.application.lumaque.bizlinked.helpers.common.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatHelper {
    public static final String DEFAULT_FORMAT_DATE_PICKER = "dd/MM/yyyy";

    public static String formatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMM yyyy");
        return format.format(newDate);
    }

    public static String ConfirmPaymentformatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat("MM-yy");
        return format.format(newDate);
    }
/*
    public static String formatDate(String date)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		format = new SimpleDateFormat("dd MMM, YYYY");
		return format.format(newDate);
	}
*/


    public static String formatTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm");
        return format.format(newDate);
    }


    public static String changeServerFormatDate(String date) {
        if (!Utils.isEmptyOrNull(date)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date newDate = null;
                newDate = format.parse(date);
                format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return format.format(newDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return date;
    }


    public static String changeServerToOurFormatDate(String date) {

        if (!Utils.isEmptyOrNull(date)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date newDate = null;
                newDate = format.parse(date);
                format = new SimpleDateFormat("dd MMM yyyy");
                return format.format(newDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return date;

    }

    public static String getDateForAudioFileName() {
        String dateString = null;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy-HHmmss", Locale.getDefault());
        dateString = format.format(Calendar.getInstance().getTime());
        return dateString;
    }

    public static String getFormattedDate(Calendar calendar, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat == null ? DEFAULT_FORMAT_DATE_PICKER
                : dateFormat, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }


}
