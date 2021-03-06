// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: LGPL-3.0-only

package org.crossmobile.backend.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import crossmobile.ios.foundation.NSSelector;
import crossmobile.ios.uikit.UIAlertView;
import crossmobile.ios.uikit.UIAlertViewDelegate;
import crossmobile.ios.uikit.UITextField;
import org.crossmobile.bind.system.SystemBridgeExt;
import org.crossmobile.bridge.Native;
import org.crossmobile.bridge.ann.CMLib;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static crossmobile.ios.uikit.$uikit.getTextFieldWrapper;
import static org.crossmobile.bridge.ann.CMLibTarget.ANDROID;


@CMLib(target = ANDROID, name = "cmioslayer")
public class AndroidSystemBridge implements SystemBridgeExt {
    private AndroidPermissions permissions;

    public static void printError(Object message, Throwable th) {
        Log.e(LOGTAG, message == null ? "null" : message.toString(), th);
        if (th != null)
            th.printStackTrace(new PrintWriter(System.err));
    }

    @Override
    public void error(String message, Throwable th) {
        printError(message, th);
    }

    @Override
    public void debug(String message, Throwable th) {
        Log.d(LOGTAG, message == null ? "null" : message, th);
        if (th != null)
            th.printStackTrace(new PrintWriter(System.err));
    }

    @Override
    public void log(String message) {
        Log.i(LOGTAG, message == null ? "null" : message);
    }

    @Override
    public void runOnEventThread(Runnable r) {
        Activity activity = MainActivity.current;
        if (activity != null) // Run only if still active
            activity.runOnUiThread(r);
    }

    @Override
    public void postOnEventThread(Runnable r) {
        View view = MainView.current;
        if (view != null)
            view.post(r);
    }

    @Override
    public boolean isEventThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    @Override
    public String version() {
        return Build.VERSION.RELEASE;
    }

    @Override
    public String model() {
        return Build.MODEL;
    }

    @Override
    public void showAlert(final UIAlertView view, final String title, final String message, final List<String> buttons, final UIAlertViewDelegate delegate) {
        Native.system().runAndWaitOnEventThread(new Runnable() {
            final Map<EditText, EditText> joints = new HashMap<>();

            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.current).create();
                dialog.setTitle(title);
                dialog.setMessage(message);
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, buttons.get(0), (DialogInterface di, int i) -> {
                    actionClick(0);
                });
                if (buttons.size() > 1) {
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttons.get(1), (DialogInterface di, int i) -> {
                        actionClick(1);
                    });
                    if (buttons.size() > 2)
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttons.get(2), (DialogInterface di, int i) -> {
                            actionClick(2);
                        });
                }
                LinearLayout linear = new LinearLayout(MainActivity.current);
                linear.setOrientation(LinearLayout.VERTICAL);
                attachTextField(linear, 0);
                attachTextField(linear, 1);
                dialog.setView(linear);
                dialog.show();
            }

            private void attachTextField(LinearLayout linear, int index) {
                if (view == null)
                    return;
                UITextField item = view.textFieldAtIndex(index);
                if (item == null)
                    return;
                final EditText attached = (EditText) getTextFieldWrapper(item).getNativeWidget();
                final EditText mirror = new EditText(MainActivity.current);
                mirror.setTransformationMethod(attached.getTransformationMethod());
                mirror.setText(attached.getText());
                joints.put(attached, mirror);
                linear.addView(mirror);
            }

            private void actionClick(int index) {
                for (EditText key : joints.keySet())
                    key.setText(joints.get(key).getText());
                if (delegate != null)
                    delegate.clickedButtonAtIndex(view, index);
            }
        });
    }

    @Override
    public void showActionSheet(String title, String cancelTitle, String destructiveTitle, List<String> otherTitles, NSSelector<Integer> callback) {
        Native.system().runAndWaitOnEventThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.current);
            builder.setTitle(title);
            if (destructiveTitle != null)
                builder.setNegativeButton(destructiveTitle, (di, i) -> callback.exec(DESTROY_ID));
            builder.setCancelable(cancelTitle != null);
            builder.setOnCancelListener(di -> callback.exec(CANCEL_ID));
            if (cancelTitle != null)
                builder.setNeutralButton(cancelTitle, (di, i) -> callback.exec(CANCEL_ID));
            if (otherTitles != null) {
                CharSequence[] items = new CharSequence[otherTitles.size()];
                for (int i = 0; i < otherTitles.size(); i++)
                    items[i] = otherTitles.get(i);
                builder.setItems(items, (di, i) -> callback.exec(i));
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public void setKeyboardVisibility(boolean status) {
        InputMethodManager imm = (InputMethodManager) MainActivity.current.getSystemService(Context.INPUT_METHOD_SERVICE);
        View mainView = MainView.current;
        if (status) {
            mainView.requestFocusFromTouch();
            imm.showSoftInput(mainView, InputMethodManager.SHOW_FORCED);
        } else
            imm.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
    }

    @Override
    public boolean launchPhoneCall(String phone) {
        return Native.network().openURL("tel:" + phone);
    }

    @Override
    public boolean isRTL() {
        if (Build.VERSION.SDK_INT >= 17)
            return MainActivity.current().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        return false;
    }

    public AndroidPermissions getPermissions() {
        if (permissions == null)
            permissions = new AndroidPermissions();
        return permissions;
    }
}
