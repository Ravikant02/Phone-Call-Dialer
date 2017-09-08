package com.ravikant.calldialerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import java.lang.reflect.Method;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.content.Context.TELEPHONY_SERVICE;


public  class PhoneCallReceiver extends BroadcastReceiver {
	private static int lastState = TelephonyManager.CALL_STATE_IDLE;
	private static Date callStartTime;
	private static boolean isIncoming;
	private static String savedNumber;  //because the passed incoming is only valid in ringing
	@Override
	public void onReceive(Context context, Intent intent) {
        Log.e("NUMBER==RECEIVE","START");
		//We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
			savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
		}
		else{
			String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
			String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			int state = 0;
			if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
				state = TelephonyManager.CALL_STATE_IDLE;
			}
			else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
				state = TelephonyManager.CALL_STATE_OFFHOOK;
			}
			else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
				state = TelephonyManager.CALL_STATE_RINGING;
			}


			onCallStateChanged(context, state, number);
		}
	}

	//Derived classes should override these to respond to specific events of interest
	protected void onIncomingCallStarted(Context ctx, String number, Date start){}
	protected void onOutgoingCallStarted(Context ctx, String number, Date start){}
	protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end){}
	protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end){}
	protected void onMissedCall(Context ctx, String number, Date start){}

	public void onCallStateChanged(Context context, int state, String number) {
        Log.e("NUMBER==RECEIVE","CHANGED");
		if(lastState == state){
			//No change, debounce extras
			return;
		}
		switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				isIncoming = true;
				callStartTime = new Date();
				savedNumber = number;
                answerCall(context);
                /*try{
                    answerPhoneAidl(context);
                }catch (Exception ex){
                    answerPhoneHeadsethook(context);
                }*/
				onIncomingCallStarted(context, number, callStartTime);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
				if(lastState != TelephonyManager.CALL_STATE_RINGING){
					isIncoming = false;
					callStartTime = new Date();
					onOutgoingCallStarted(context, savedNumber, callStartTime);
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				//Went to idle-  this is the end of a call.  What type depends on previous state(s)
				if(lastState == TelephonyManager.CALL_STATE_RINGING){
					//Ring but no pickup-  a miss
					onMissedCall(context, savedNumber, callStartTime);
				}
				else if(isIncoming){
					onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
				}
				else{
					onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
				}
				break;
		}
		lastState = state;
	}

	private void answerCall(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec( "input keyevent " + KeyEvent.KEYCODE_HEADSETHOOK );
                }
                catch (Throwable t) {
                    // do something proper here.
                }
            }
        }).start();
    }

    private void answerCall(Context context) {
        /*Log.d(TAG, "Answering call");
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(telephony);
            //telephonyService.silenceRinger();
            telephonyService.answerRingingCall();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void answerPhoneHeadsethook(Context context) {
        // Simulate a press of the headset button to pick up the call
        Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
        context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");

        // froyo and beyond trigger on buttonUp instead of buttonDown
        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void answerPhoneAidl(Context context) throws Exception {
        // Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
        /*TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class c = Class.forName(tm.getClass().getName());
        Method m = c.getDeclaredMethod("getITelephony");
        m.setAccessible(true);
        ITelephony telephonyService;
        telephonyService = (ITelephony)m.invoke(tm);

        // Silence the ringer and answer the call!
        telephonyService.silenceRinger();
        telephonyService.answerRingingCall();*/
    }
}

