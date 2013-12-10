package com.elevenestates.buzzbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyStartServiceReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
	  Log.d("Buzzbox","Mystartservicereciver recieved");
    Intent service = new Intent(context, Services.class);
    context.startService(service);
  }
}