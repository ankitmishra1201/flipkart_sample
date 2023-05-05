package com.floatingwidgetchathead_demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scan = findViewById(R.id.scan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startFloatingWidgetService();
    }




    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
                saveData(intentResult.getContents().toString());
//                ArrayList list=convertJsonStringToList(intentResult.getContents() );



            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void onClick(View v) {
        IntentIntegrator intentIntegrator=new IntentIntegrator(
                MainActivity.this
        );
        intentIntegrator.setPrompt("For Flash use volume up button");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();

    }

    public void saveData(String name){
        String url="https://script.google.com/macros/s/AKfycby9C3H6-ecuu798RGfN2dzrZXJZwRsqfN7qoyh_UusEpko2DAfT4sACigg0m1Thhp43/exec?";
        url=url+"action=create&name="+name;
        Log.d("URL", url.toString());

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error",error.getMessage().toString());
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

//    public ArrayList<String> convertJsonStringToList(String jsonString) {
//        ArrayList<String> list = new ArrayList<>();
//        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            list.add(jsonArray.get(i).getAsString());
//        }
//        for(int i=0;i<list.size();i++){
//            Log.d("Element", list.get(i).toString());
//        }
//        return list;
//    }


    //String jsonInput = "{ \"name\": \"Pari\", \"barcode\": PARI112}"



}










