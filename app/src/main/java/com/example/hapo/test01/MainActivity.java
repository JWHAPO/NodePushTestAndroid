package com.example.hapo.test01;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private EditText editText;
    private Button button;
    private EditText editText2;
    private Button button2;
    private EditText editText3;
    private Button button3;



    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        editText2 = (EditText)findViewById(R.id.editText2);
        button2 = (Button)findViewById(R.id.button2);
        editText3 = (EditText)findViewById(R.id.editText3);
        button3 = (Button)findViewById(R.id.button3);
        textView = (TextView)findViewById(R.id.textView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            println("onResponse() 호출됨. " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();;
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        return params;
                    }
                };

                request.setShouldCache(false);
                Volley.newRequestQueue(MainActivity.this).add(request);
                println("웹 서버에 요청함 : "+url);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText2.getText().toString();

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            println("onResponse() 호출됨. " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        DeviceInfo device = getDeviceInfo();
                        params.put("mobile",device.getMobile());
                        params.put("osVersion",device.getOsVersion());
                        params.put("model",device.getModel());
                        params.put("display",device.getDisplay());
                        params.put("manufacturer",device.getManufacturer());
                        params.put("macAddress",device.getMacAddress());
                        return params;
                    }
                };

                request.setShouldCache(false);
                Volley.newRequestQueue(MainActivity.this).add(request);
                println("웹 서버에 요청함 : "+url);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        println("등록 ID : "+instanceIdResult.getToken());
                        sendToMobileServer(instanceIdResult.getToken());
                    }
                });



            }
        });

    }

    public void println(final String data){
        textView.append(data + '\n');
    }

    public DeviceInfo getDeviceInfo(){
        DeviceInfo device = null;

        //1. mobile
        String mobile = "010-1234-5678";

        //2. osVersion
        String osVersion = Build.VERSION.RELEASE;

        //3. model
        String model = Build.MODEL;

        //4. display
        String display = "";

        //5. manufacturer
        String manufacturer = Build.MANUFACTURER;

        //6. macAddress
        String macAddress = getMacAddress(MainActivity.this);

        device = new DeviceInfo(mobile,osVersion,model,display,manufacturer,macAddress);

        return device;
    }

    private static String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    public void sendToMobileServer(final String regId){

        String url = editText3.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    println("onResponse() 호출됨. " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                DeviceInfo device = getDeviceInfo();
                params.put("mobile",device.getMobile());
                params.put("registrationId",regId);
                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(MainActivity.this).add(request);
        println("웹 서버에 요청함 : "+url);
    }
}
