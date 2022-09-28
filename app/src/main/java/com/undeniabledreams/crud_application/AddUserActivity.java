package com.undeniabledreams.crud_application;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    EditText txtDate, textStoreName, textProductName, textProductType, doublePrice;
    Button saveButton;
    public static final int DEFAULT_TIMEOUT_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        txtDate = findViewById(R.id.date);
        textStoreName = findViewById(R.id.store_name);
        textProductName = findViewById(R.id.product_name);
        textProductType = findViewById(R.id.product_type);
        doublePrice = findViewById(R.id.price);
        saveButton = findViewById(R.id.btnInsert);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("TAG0927", "onClick: btnClicked");
                    insertData();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void insertData() throws ParseException {

        String sDate1 = txtDate.getText().toString();
        Date date1=new SimpleDateFormat("yyyy/mm/dd").parse(sDate1);
        System.out.println(sDate1+"\t"+date1);

        Date date = date1;
        String storeName = textStoreName.getText().toString();
        String productName = textProductName.getText().toString();
        String productType = textProductType.getText().toString();
        Double price = Double.parseDouble(doublePrice.getText().toString());

        if (date == null) {
            txtDate.setError("Enter date");
        } else if (storeName.isEmpty()) {
            textStoreName.setError("Enter store name");
        } else if (productName.isEmpty()) {
            textProductName.setError("Enter product name");
        } else if (productType.isEmpty()) {
            textProductType.setError("Enter product type");
        } else if (price == null) {
            doublePrice.setError("Enter price");
        } else {
            String insert_url = "http://13.231.226.174/insertData.php";
//            String insert_url = "http://192.168.0.44/insertData.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals(1)) {
                                    Toast.makeText(AddUserActivity.this, "data has been added", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddUserActivity.this, "unable to add the record" + error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onErrorResponse: " + error.toString());

                        }


                    }){
                //get params
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("date", String.valueOf(date));
                    params.put("store_name", storeName);
                    params.put("product_name", productName);
                    params.put("product_type", productType);
                    params.put("price", String.valueOf(price));


                    return super.getParams();
                }



            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }
}