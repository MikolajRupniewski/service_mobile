package pl.rupniewski.staticcontent;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogs {
    public static void showDialog(String title, String message, Context context, int icon) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(icon);
        alertDialog.show();
    }}
