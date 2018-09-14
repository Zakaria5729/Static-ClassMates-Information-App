package com.android.zakaria.classmateinfo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zakaria.classmateinfo.R;
import com.android.zakaria.classmateinfo.permissions.PermissionUtils;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private TextView nameTV, idTV, emailTV, phoneTV;
    private ImageView img;

    private PermissionUtils permissionUtils;
    private TextToSpeech textToSpeech;

    private static final int MY_REQUEST_PHONE_CALL_CODE = 111;
    private static final int TXT_MY_REQUEST_PHONE_CALL = 1;

    private String name, id, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameTV = findViewById(R.id.yourName);
        idTV = findViewById(R.id.yourId);
        emailTV = findViewById(R.id.yourEmail);
        phoneTV = findViewById(R.id.yourPhone);
        img = findViewById(R.id.img);

        permissionUtils = new PermissionUtils(this);
        getAndSetStudentInfo();
    }

    private void getAndSetStudentInfo() {
        name = getIntent().getStringExtra("name_key");
        id = getIntent().getStringExtra("id_key");
        email = getIntent().getStringExtra("email_key");
        phone = getIntent().getStringExtra("phone_key");

        nameTV.setText(name);
        idTV.setText(id);
        emailTV.setText(email);
        phoneTV.setText(phone);
        img.setImageResource(getIntent().getIntExtra("image_key", 0));
    }

    //check Permission Already Granted Or Not
    private int checkPermissionAlreadyGrantedOrNot(int permission) {
        int status = PackageManager.PERMISSION_DENIED;

        switch (permission) {
            case TXT_MY_REQUEST_PHONE_CALL:
                status = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                break;
        }
        return status;
    }

    //request for permission (new)
    private void requestPermission(int permission) {
        switch (permission) {
            case TXT_MY_REQUEST_PHONE_CALL:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_REQUEST_PHONE_CALL_CODE);
                break;
        }
    }

    //show permission explanation dialog
    private void showPermissionExplanationDialog(final int permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (permission == TXT_MY_REQUEST_PHONE_CALL) {
            builder.setTitle("Phone Call Permission needed.");
            builder.setIcon(R.drawable.phone_call_icon);
            builder.setMessage("This app needed Phone call permission because without phone call permission this app can not generate call to any number.");
        }

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (permission == TXT_MY_REQUEST_PHONE_CALL) {
                    requestPermission(TXT_MY_REQUEST_PHONE_CALL); //request for (new) permission
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    public void phoneCallAction(View view) {
        if (checkPermissionAlreadyGrantedOrNot(TXT_MY_REQUEST_PHONE_CALL) == PackageManager.PERMISSION_DENIED) {

             if (!permissionUtils.checkPermissionPreference("phone_call")) {  // for first time permission call (!false means true)
                requestPermission(TXT_MY_REQUEST_PHONE_CALL);
                //permissionUtils.updatePermissionPreference("phone_call");
            }
             else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                showPermissionExplanationDialog(TXT_MY_REQUEST_PHONE_CALL);
            }  else {
                Toast.makeText(this, "Please, allow phone call permission in your app setting", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            makePhoneCall();
        }
    }

    private void makePhoneCall() {
        String number = getIntent().getStringExtra("phone_key");
        if (number.trim().length() > 0) {
            String dial = "tel:" + number;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //stop text to speech after clicking send call button
            stopTextToSpeaking();
            try {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Phone number is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSmsAction(View view) {
        //stop text to speech after clicking send sms button
        stopTextToSpeaking();

        String phoneNumber = getIntent().getStringExtra("phone_key");
        Intent intent = new Intent(this, SendEmailAndSmsActivity.class);
        intent.putExtra("phone_key", phoneNumber);
        intent.putExtra("my_key", "my_phone_number_for_sms");
        startActivity(intent);
    }

    public void emailSendAction(View view) {
        //stop text to speech after clicking send email button
        stopTextToSpeaking();

        String email = getIntent().getStringExtra("email_key");
        Intent intent = new Intent(this, SendEmailAndSmsActivity.class);
        intent.putExtra("email_key", email);
        intent.putExtra("my_key", "my_email");
        startActivity(intent);
    }

    public void listenUserDetails(View view) {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(DetailsActivity.this, "This language is not supported.", Toast.LENGTH_SHORT).show();
                    } else {
                        textToSpeech.setPitch(1.1f);
                        textToSpeech.setSpeechRate(0.85f);
                        speakNow();
                    }
                }
            }
        });
    }

    private void speakNow() {
        String[] userDetailList;
        id = id.replace("-", "");
        phone = phone.replace("+88", "");

        String userDetails = "His name is " + name + "\n\n His ID is " + id + "\n\n He has an EMAIL which is " + email + "\n\n And he has a phone number also that is " + phone;
        userDetailList = userDetails.split("\\n\\n");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String anUserDetailList : userDetailList) {
                textToSpeech.speak(anUserDetailList, TextToSpeech.QUEUE_ADD, null, null);
               // textToSpeech.setOnUtteranceProgressListener(new )
                textToSpeech.playSilentUtterance(250, TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            for (String anUserDetailList : userDetailList) {
                textToSpeech.speak(anUserDetailList, TextToSpeech.QUEUE_ADD, null);
                textToSpeech.playSilence(250, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    private void stopTextToSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        stopTextToSpeaking();
        super.onDestroy();
    }
}
