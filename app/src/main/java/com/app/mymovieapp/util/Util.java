package com.app.mymovieapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.app.mymovieapp.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class Util {

    private static ConnectivityManager connectivityManager;

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static DateTime toDate(String date) {
        return DateTimeFormat.forPattern("yyyy-MM-dd")
                .parseDateTime(date);
    }

    public static String toDbDate(DateTime date) {
        return DateTimeFormat.forPattern("yyyy-MM-dd").print(date);
    }

    public static String toPrettyDate(DateTime date) {
        return DateTimeFormat.mediumDate().print(date.getMillis());
    }

    public static boolean isConnected(final Activity activity, boolean showToast) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (info != null && info.isConnected());
        if (!isConnected && showToast) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, R.string.conn_internet, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return isConnected;
    }

    public static void shareText(Activity activity, String text) {
        ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text)
                .startChooser();
    }

    public static void openLinkInExternalApp(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(intent);
    }

    public static Dialog LoadingSpinner(Context mContext) {
        Dialog pd = new Dialog(mContext, android.R.style.Theme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setContentView(view);
        return pd;
    }
}
