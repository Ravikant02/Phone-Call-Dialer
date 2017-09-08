package com.ravikant.calldialerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPhoneNumber;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnOne = (Button) findViewById(R.id.btnOne);
        Button btnTwo = (Button) findViewById(R.id.btnTwo);
        Button btnThree = (Button) findViewById(R.id.btnThree);
        Button btnFour = (Button) findViewById(R.id.btnFour);
        Button btnFive = (Button) findViewById(R.id.btnFive);
        Button btnSix = (Button) findViewById(R.id.btnSix);
        Button btnSeven = (Button) findViewById(R.id.btnSeven);
        Button btnEight = (Button) findViewById(R.id.btnEight);
        Button btnNight = (Button) findViewById(R.id.btnNine);
        Button btnZero = (Button) findViewById(R.id.btnZero);
        Button btnAsterisk = (Button) findViewById(R.id.btnAsterisk);
        Button btnHash = (Button) findViewById(R.id.btnHash);
        ImageView btnDelete = (ImageView) findViewById(R.id.btnDelete);
        Button btnCall = (Button) findViewById(R.id.btnCall);
        Button btnClear = (Button) findViewById(R.id.btnClearAll);

        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNight.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnAsterisk.setOnClickListener(this);
        btnHash.setOnClickListener(this);

    }

    private boolean isCallPermitted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        return true;
    }

    private void call(String phoneNumber){
        String callInfo = "tel:" + phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(callInfo));
        if (isCallPermitted()) startActivity(callIntent);
    }

    @Override
    public void onClick(View v) {
        phoneNumber = edtPhoneNumber.getText().toString().trim();
        switch (v.getId()){
            case R.id.btnOne:
                phoneNumber += "1";
                break;
            case R.id.btnTwo:
                phoneNumber += "2";
                break;
            case R.id.btnThree:
                phoneNumber += "3";
                break;
            case R.id.btnFour:
                phoneNumber += "4";
                break;
            case R.id.btnFive:
                phoneNumber += "5";
                break;
            case R.id.btnSix:
                phoneNumber += "6";
                break;
            case R.id.btnSeven:
                phoneNumber += "7";
                break;
            case R.id.btnEight:
                phoneNumber += "8";
                break;
            case R.id.btnNine:
                phoneNumber += "9";
                break;
            case R.id.btnZero:
                phoneNumber += "0";
                break;
            case R.id.btnAsterisk:
                phoneNumber += "*";
                break;
            case R.id.btnHash:
                phoneNumber += "#";
                break;
            case R.id.btnDelete:
                if (phoneNumber != null && phoneNumber.length() > 0) {
                    phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                }
                break;
            case R.id.btnClearAll:
                phoneNumber = "";
                break;
            case R.id.btnCall:
                if (phoneNumber.isEmpty()) return;
                call(phoneNumber);

                /*Boolean isHash = false;
                if (phoneNumber.subSequence(phoneNumber.length() - 1, phoneNumber.length()).equals("#")) {
                    phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                    String callInfo = "tel:" + phoneNumber + Uri.encode("#");
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(callInfo));
                    startActivity(callIntent);
                } else {
                    String callInfo = "tel:" + phoneNumber;
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(callInfo));
                    startActivity(callIntent);
                }*/
                break;
        }
        edtPhoneNumber.setText(phoneNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(phoneNumber);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }
}
