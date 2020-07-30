package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.util.HttpConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    private static final int REQUEST_OWNER_KEY_QR = 1;
    private static final int REQUEST_MOMCOW_KEY_QR = 2;
    private static final int REQUEST_DADCOW_KEY_QR = 3;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    EditText ownerKeyText;
    EditText momCowKeyText;
    EditText dadCowKeyText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Button addCowButton = findViewById(R.id.a_admin_addcow_button);


        addCowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AdminActivity.this);
                dialog.setContentView(R.layout.dialog_addcow);

                ownerKeyText = dialog.findViewById(R.id.d_addcow_ownerkey_text);
                momCowKeyText = dialog.findViewById(R.id.d_addcow_momcowkey_text);
                dadCowKeyText = dialog.findViewById(R.id.d_addcow_dadcowkey_text);
                EditText descText = dialog.findViewById(R.id.d_addcow_desc_text);

                Button okButton = dialog.findViewById(R.id.d_addcow_ok_button);
                Button exButton = dialog.findViewById(R.id.d_addcow_ex_button);

                Button ownerKeyQrButton = dialog.findViewById(R.id.d_addcow_ownerkey_qr);
                Button momCowKeyQrButton = dialog.findViewById(R.id.d_addcow_momcowkey_qr);
                Button dadCowKeyQrButton = dialog.findViewById(R.id.d_addcow_dadcowkey_qr);


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("jwt", sharedPreferences.getString("jwt", null));
                        contentValues.put("momptr", momCowKeyText.getText().toString());
                        contentValues.put("dadptr", dadCowKeyText.getText().toString());
                        contentValues.put("ownerptr", ownerKeyText.getText().toString());
                        contentValues.put("desc", descText.getText().toString());

                        AddCowNetworkAsyncTask addCowNetworkAsyncTask = new AddCowNetworkAsyncTask();
                        addCowNetworkAsyncTask.execute(contentValues);

                        dialog.dismiss();
                    }
                });

                exButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ownerKeyQrButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanQr(REQUEST_OWNER_KEY_QR);
                    }
                });

                momCowKeyQrButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanQr(REQUEST_MOMCOW_KEY_QR);
                    }
                });

                dadCowKeyQrButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanQr(REQUEST_DADCOW_KEY_QR);
                    }
                });


                dialog.show();
            }
        });

    }




    public class AddCowNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/admin/addcow");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(AdminActivity.this, "추가에 성공했습니다", Toast.LENGTH_SHORT).show();

                    Dialog dialog = new Dialog(AdminActivity.this);
                    dialog.setContentView(R.layout.dialog_qr);

                    ImageView qrImage = dialog.findViewById(R.id.qr_image);
                    TextView title = dialog.findViewById(R.id.title);

                    title.setText("소 QR");
                    qrImage.setImageBitmap(getQrCode(jsonObject.getString("cowPtr")));

                    dialog.show();

                }else{
                    Toast.makeText(AdminActivity.this, "추가에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(AdminActivity.this, "추가에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }




    public void scanQr(int requestCode){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setRequestCode(requestCode);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode > 0 && requestCode <= 3) {
            IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data);
            if (result == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned", Toast.LENGTH_LONG).show();
                try {

                    switch (requestCode){
                        case REQUEST_OWNER_KEY_QR:
                            ownerKeyText.setText(result.getContents().toString());
                            Toast.makeText(this, "Parsing Success", Toast.LENGTH_LONG).show();
                            break;
                        case REQUEST_MOMCOW_KEY_QR:
                            momCowKeyText.setText(result.getContents().toString());
                            Toast.makeText(this, "Parsing Success", Toast.LENGTH_LONG).show();
                            break;
                        case REQUEST_DADCOW_KEY_QR:
                            dadCowKeyText.setText(result.getContents().toString());
                            Toast.makeText(this, "Parsing Success", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(this, "Parsing Failed", Toast.LENGTH_LONG).show();
                    }


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



