package com.example.testapplication.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.testapplication.UserTraceActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HubToUserQr extends AppCompatActivity {

    public static HubToUserQr activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned", Toast.LENGTH_LONG).show();
                try {
                    Intent intent = new Intent(HubToUserQr.this, UserTraceActivity.class);
                    intent.putExtra("qrData", result.getContents().toString());
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Parsing Failed", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
