package com.zodiake.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by yagamai on 15-6-4.
 */
public class SocketIOPlugin extends CordovaPlugin {
    public static final String CONNECT= "connect";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        if (CONNECT.equals(action)) {

            Socket socket = null;
            try {
                socket = IO.socket("http://192.168.1.66:3000");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            }).on("newOrder", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject)args[0];
                    try {
                        makeNotification((String)obj.get("title"),(String)obj.get("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            callbackContext.success("ok");
            socket.connect();
            return true;
        }
        return false;
    }

    private void makeNotification(String title,String content) {
        Activity context = cordova.getActivity();
        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(getIconResId());
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification.build());
    }

    private int getIconResId() {
        Context context = cordova.getActivity().getApplicationContext();
        Resources res   = context.getResources();
        String pkgName  = context.getPackageName();
        int resId;
        resId = res.getIdentifier("icon", "drawable", pkgName);
        return resId;
    }
}
