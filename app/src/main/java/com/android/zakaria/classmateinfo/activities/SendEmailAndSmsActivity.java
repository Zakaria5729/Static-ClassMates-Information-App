package com.android.zakaria.classmateinfo.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zakaria.classmateinfo.R;

import java.util.ArrayList;

public class SendEmailAndSmsActivity extends AppCompatActivity {

    private EditText toET, subjectET, messageET;
    private String smsOrEmailKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_and_sms);

        toET = findViewById(R.id.toET);
        subjectET = findViewById(R.id.subjectET);
        messageET = findViewById(R.id.messageET);
        TextView textView = findViewById(R.id.textView);

        smsOrEmailKey = getIntent().getStringExtra("my_key");

        if (smsOrEmailKey.equals("my_phone_number_for_sms")) {
            textView.setText(R.string.send_sms);
            toET.setInputType(InputType.TYPE_CLASS_PHONE);
            toET.setText(getIntent().getStringExtra("phone_key"));

            messageET.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        else if (smsOrEmailKey.equals("my_email")) {
            textView.setText(R.string.send_email);
            subjectET.setVisibility(View.VISIBLE);
            toET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            toET.setText(getIntent().getStringExtra("email_key"));

            subjectET.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    public void sendToEmailOrSmsClient(View view) {
        String recipients = toET.getText().toString();
        String[] emailRecipient = recipients.split(",");
        String message = messageET.getText().toString();

        if (smsOrEmailKey.equals("my_phone_number_for_sms")) {
            if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(recipients)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");

                intent.setData(Uri.parse("sms:" + recipients));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please, fill up input fields.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (smsOrEmailKey.equals("my_email")) {
            String subject = subjectET.getText().toString();
            if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(message) && !TextUtils.isEmpty(recipients)) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, emailRecipient);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);

                intent.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(intent, "Choose an email client: (ex: Gmail app)"));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Please, fill up the input fields.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
