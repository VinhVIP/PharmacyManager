package com.team28.qlnhathuoc.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isDateOfQuarterYear(Date date, int quarter, int year) {
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String strMonth = formatMonth.format(date);
        String strYear = formatYear.format(date);

        int mon = Integer.parseInt(strMonth);
        int dateQuarter = (mon - 1) / 3 + 1;

        return strYear.equals(String.valueOf(year)) && dateQuarter == quarter;
    }

    public static String getStringDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        return format.format(date);
    }

    public static String floatToString(float value) {
        String s = String.format(Locale.US, "%.2f", value);
        if (s.contains(".")) {
            while (s.charAt(s.length() - 1) == '0') {
                s = s.substring(0, s.length() - 1);
            }
            if (s.charAt(s.length() - 1) == '.') {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    public static String formatCurrency(float money) {
        if (money == (long) money) {
            return formatCurrency((long) money);
        }

        String s = floatToString(money);
        int dotIndex = s.indexOf('.');
        if (dotIndex != -1) {
            String str = s.substring(0, dotIndex);
            return formatCurrency(Long.parseLong(str)) + s.substring(dotIndex);
        }
        return formatCurrency((long) money);
    }

    public static String formatCurrency(long money) {
        boolean sign = money < 0;
        money = Math.abs(money);

        String s = String.valueOf(money);
        String res = "";
        int i = s.length();
        for (; i >= 0; i -= 3) {
            final String substring = s.substring(Math.max(0, i - 3), i);
            res = substring + "," + res;
        }
        if (res.startsWith(",")) res = res.substring(1);
        if (res.endsWith(",")) res = res.substring(0, res.length() - 1);

        if (sign) res = "-" + res;
        return res;
    }
}
