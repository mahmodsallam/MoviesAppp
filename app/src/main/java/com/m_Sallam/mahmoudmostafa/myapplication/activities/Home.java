package com.m_Sallam.mahmoudmostafa.myapplication.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.m_Sallam.mahmoudmostafa.myapplication.R;
import com.m_Sallam.mahmoudmostafa.myapplication.adapters.MyAdapter;
import com.m_Sallam.mahmoudmostafa.myapplication.constants.Constant;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Movie;
import com.m_Sallam.mahmoudmostafa.myapplication.store.MoviesProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<Movie> moviesList;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesList = new ArrayList<>();

        if (savedInstanceState != null) {

            //there exists some thing in the array
            moviesList = savedInstanceState.getParcelableArrayList("list");
            //working with the recycler and its adapter
            adapter = new MyAdapter(this, moviesList);
            recyclerView = (RecyclerView) findViewById(R.id.recyler_id);
            mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } else {

            //pulling Data from server default data
            show(Constant.POPULAR);
            //working with the recycler and its adapter
            adapter = new MyAdapter(this, moviesList);
            recyclerView = (RecyclerView) findViewById(R.id.recyler_id);
            mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("list", moviesList);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        moviesList = savedInstanceState.getParcelableArrayList("list");
        adapter = new MyAdapter(this, moviesList);
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                show(Constant.POPULAR);

                adapter = new MyAdapter(this, moviesList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;
            case R.id.top_rated:
                show(Constant.TOP_RATED);

                adapter = new MyAdapter(this, moviesList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;

            case R.id.favourite:

                moviesList.clear();

                Cursor cursor = getContentResolver().query(MoviesProvider.CONTENT_URL,
                        null, null, null, null);

                cursor.moveToFirst();
                while (cursor.moveToNext()) {
//                    moviesList.add(new Movie("", cursor.getString(0),
//                            "", cursor.getString(1),
//                            "", cursor.getString(4), "",
//                            "", cursor.getString(3), cursor.getString(2)));

                    //these solution supposed to solve the problem
                    moviesList.add(new Movie("", cursor.getString(0),
                            cursor.getString(5), cursor.getString(1), "",
                            cursor.getString(4), "", "", cursor.getString(3),
                            cursor.getString(2)));
                }
                adapter = new MyAdapter(this, moviesList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void show(String link) {
        moviesList.clear();
        StringRequest stringRequest = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject object = results.getJSONObject(i);
                        String vote_count = object.getString("vote_count");
                        String id = object.getString("id");
                        String vote_average = object.getString("vote_average");
                        String title = object.getString("title");
                        String popularity = object.getString("popularity");
                        String poster_path = object.getString("poster_path");
                        String original_language = object.getString("original_language");
                        String overview = object.getString("overview");
                        String release_date = object.getString("release_date");
                        moviesList.add(new Movie(vote_count, id, vote_average, title, popularity, poster_path,
                                original_language, title, overview, release_date));

                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}








