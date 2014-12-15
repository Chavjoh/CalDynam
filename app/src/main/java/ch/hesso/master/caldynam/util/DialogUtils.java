package ch.hesso.master.caldynam.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class DialogUtils {

    private static boolean dialogShown = false;

    public static boolean isDialogShown() {
        return dialogShown;
    }

    public static AlertDialog show(final Activity context, final String message) {
        final SpannableString s = new SpannableString(message);
        Linkify.addLinks(s, Linkify.ALL);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(s);
        builder.setPositiveButton(android.R.string.ok, closeDialog());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogShown = true;

        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        return dialog;
    }

    public static DialogInterface.OnClickListener closeDialog() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                dialogShown = false;
            }
        };
    }

    public static AlertDialog showAndExit(final Activity context, final String message) {
        final SpannableString s = new SpannableString(message);
        Linkify.addLinks(s, Linkify.ALL);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(s);
        builder.setPositiveButton(android.R.string.ok, closeDialogAndQuit(context));
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogShown = true;

        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

        return dialog;
    }

    public static DialogInterface.OnClickListener closeDialogAndQuit(final Activity context) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                dialogShown = false;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
    }
}
