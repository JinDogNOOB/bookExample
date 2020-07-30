package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testapplication.qr.HubToUserQr;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;

public class HubActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String sharedQrStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();



        ImageView myidQrImage = findViewById(R.id.a_hub_myid_qr_image);
        Button gotoTraceButton = findViewById(R.id.a_hub_goto_trace_button);
        Button gotoFarmerButton = findViewById(R.id.a_hub_goto_farmer_button);
        Button gotoButcherButton = findViewById(R.id.a_hub_goto_butcher_button);
        Button gotoAdminButton = findViewById(R.id.a_hub_goto_admin_button);

        switch(sharedPreferences.getInt("userType", 0)){
            case 0:{
                gotoAdminButton.setVisibility(View.INVISIBLE);
                gotoButcherButton.setVisibility(View.INVISIBLE);
                gotoFarmerButton.setVisibility(View.INVISIBLE);
                break;
            }case 1:{

                gotoAdminButton.setVisibility(View.INVISIBLE);
                gotoButcherButton.setVisibility(View.INVISIBLE);
                break;
            }case 2:{
                gotoAdminButton.setVisibility(View.INVISIBLE);
                gotoFarmerButton.setVisibility(View.INVISIBLE);
                break;
            }case 99:{
                gotoFarmerButton.setVisibility(View.INVISIBLE);
                gotoButcherButton.setVisibility(View.INVISIBLE);
                break;
            }default:{
                gotoTraceButton.setVisibility(View.INVISIBLE);
                break;
            }
        }

        myidQrImage.setImageBitmap(getQrCode(sharedPreferences.getString("key", null)));

        gotoTraceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, HubToUserQr.class);

                startActivity(intent);
            }
        });

        gotoFarmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, FarmerActivity.class);
                startActivity(intent);
            }
        });

        gotoButcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, ButcherActivity.class);
                startActivity(intent);
            }
        });

        gotoAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HubActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });





    }








    public void scanQr(){

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
                    sharedQrStr = result.getContents().toString();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Parsing Failed", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public Bitmap getQrCode(String contents){
        System.out.println("QR STring : " + contents);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try{
            HashMap<EncodeHintType, String> hint = new HashMap<EncodeHintType, String>();
            hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 600, 600, hint));
            return bitmap;
        }catch (WriterException e){
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for( int x = 0 ; x < width ; x++){
            for(int y = 0; y < height; y++){
                bitmap.setPixel(x, y, matrix.get(x,y)? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

}
