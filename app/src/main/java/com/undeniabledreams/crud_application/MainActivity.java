package com.undeniabledreams.crud_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private RecyclerView mRecyclerView;
    private UserAdapter adapter;
    private List<UserModel> usersModelList;
    private RequestQueue mRequestQueue;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date date;

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRequestQueue = Volley.newRequestQueue(this);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresher);

        usersModelList = new ArrayList<>();

        adapter = new UserAdapter(this, usersModelList);
        mRecyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.add_btn);

        retrieve_all_user();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieve_all_user();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddUserActivity.class));
            }
        });
    }

    private void retrieve_all_user() {
        String get_all_users_url = "http://192.168.0.44/displayAll.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_all_users_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("details");

                            if (success.equals("1")){

                                usersModelList.clear();

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int id = Integer.parseInt(object.getString("id"));


                                    String sDate1 = object.getString("date");
                                    Date date1=new SimpleDateFormat("yyyy/mm/dd").parse(sDate1);

                                    String storeName = object.getString("store_name");
                                    String productName = object.getString("product_name");
                                    String productType = object.getString("product_type");
                                    Double price = Double.parseDouble(object.getString("price"));
                                    String created_date = object.getString("created_date");

                                    UserModel userModel = new UserModel(id, date1, storeName, productName, productType, price);

                                    usersModelList.add(userModel);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Server error "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(stringRequest);
    }
}