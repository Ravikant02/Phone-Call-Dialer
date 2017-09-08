package com.ravikant.calldialerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;


public class CallReceiver extends PhoneCallReceiver {
	@Override
	protected void onIncomingCallStarted(Context ctx, String number, Date start) {
		Log.e("NUMBER==START", number);
	}

	@Override
	protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
	}

	@Override
	protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
		Log.e("NUMBER==END", number);
	}

	@Override
	protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
	}

	@Override
	protected void onMissedCall(Context ctx, String number, Date start) {
	}
}

