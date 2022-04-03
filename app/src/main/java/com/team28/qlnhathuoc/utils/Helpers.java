package com.team28.qlnhathuoc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap bytesToBitmap(byte[] icon) {
        return BitmapFactory.decodeByteArray(icon, 0, icon.length);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytes(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public static boolean isDateOfYear(Date date, int year) {
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String strYear = formatYear.format(date);

        return strYear.equals(String.valueOf(year));
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
