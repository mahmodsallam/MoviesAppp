package com.m_Sallam.mahmoudmostafa.myapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.m_Sallam.mahmoudmostafa.myapplication.R;
import com.m_Sallam.mahmoudmostafa.myapplication.adapters.ReviewsAdapter;
import com.m_Sallam.mahmoudmostafa.myapplication.adapters.TrailerAdapter;
import com.m_Sallam.mahmoudmostafa.myapplication.constants.Constant;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Movie;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Review;
import com.m_Sallam.mahmoudmostafa.myapplication.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    //views
    private TextView detailTitle;
    private TextView detailReleaseDate;
    private TextView detailOverview;
    private ImageView back;
    private RatingBar rating;
    private Button reviewButton;
    private TextView reviewText;
    private Button favourite;
    private Button delete;
    private RecyclerView recyclerView;
    private RecyclerView trailerRecycler;
    private NestedScrollView scrollView, scroll2;

    //arrays
    ArrayList<String> key;
    ArrayList<Review> reviews;
    ArrayList<Trailer> trailers;

    ContentValues contentValues;

    //objects
    Trailer trailer;
    Movie movie;

    //Adapters
    TrailerAdapter trailerAdapter;
    ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentValues = new ContentValues();

        //getting the value from the intent
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");

        //finding the views
        detailTitle = (TextView) findViewById(R.id.detail_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "daisy.ttf");
        detailTitle.setTypeface(typeface);
        detailReleaseDate = (TextView) findViewById(R.id.detail_release_date);
        detailOverview = (TextView) findViewById(R.id.detail_overview);
        rating = (RatingBar) findViewById(R.id.rating);
        rating.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
        back = (ImageView) findViewById(R.id.back);
//        delete = (Button) findViewById(R.id.delete);
        recyclerView = (RecyclerView) findViewById(R.id.review_text_recycler);
        trailerRecycler = (RecyclerView) findViewById(R.id.trailers_text_recycler);
        scrollView = (NestedScrollView) findViewById(R.id.scroll);
        scroll2 = (NestedScrollView) findViewById(R.id.scroll2);

        //filling the views
        Glide.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(back);
        detailTitle.setText(movie.getTitle());
        detailOverview.setText(movie.getOverview());
        detailReleaseDate.setText(movie.getReleaseDate());

        //initializing arrays that holds trailers and reviews
        key = new ArrayList<>();
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();

        if (savedInstanceState != null) {


            //getting the trailers
            trailers = savedInstanceState.getParcelableArrayList("trailersList");
            trailerAdapter = new TrailerAdapter(this, trailers);
            RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(this, 1);
            trailerRecycler.setLayoutManager(mLayoutManager2);
            trailerRecycler.setItemAnimator(new DefaultItemAnimator());
            trailerRecycler.setAdapter(trailerAdapter);


            //getting reviews
            reviews = savedInstanceState.getParcelableArrayList("list");
            adapter = new ReviewsAdapter(this, reviews);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


            //getting back the scrolling position
            final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });


        } else {

            show(Constant.TRAILER_PART1 + movie.getId() + Constant.TRAILER);
            reviewfun(Constant.REVIEW_PART_1 + movie.getId() + Constant.REVIEW);

            adapter = new ReviewsAdapter(this, reviews);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            //the adapter of trailers
            trailerAdapter = new TrailerAdapter(this, trailers);
            RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(this, 1);
            trailerRecycler.setLayoutManager(mLayoutManager2);
            trailerRecycler.setItemAnimator(new DefaultItemAnimator());
            trailerRecycler.setAdapter(trailerAdapter);

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key.get(0)));
                    startActivity(youtube);
                } catch (Exception e) {
                }
            }
        });

        //adding to favourite
//        favourite = (Button) findViewById(R.id.favourite_btn);
//        favourite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    contentValues.put("_id", movie.getId());
//                    contentValues.put("title", movie.getTitle());
//                    contentValues.put("releaseDate", movie.getReleaseDate());
//                    contentValues.put("review", movie.getOverview());
//                    contentValues.put("posterPath", movie.getPosterPath());
//                    contentValues.put("voteAverage", movie.getVoteAverage());
//
//
//                    Uri uri = getContentResolver().insert(MoviesProvider.CONTENT_URL, contentValues);
//                    Toast.makeText(getBaseContext(), "succeeded ", Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Toast.makeText(getBaseContext(), "Already exists  ", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    getContentResolver().delete(MoviesProvider.CONTENT_URL, "_id = " + movie.getId(), null);
//                    Toast.makeText(getBaseContext(), "delted ", Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                }
//            }
//        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("list", reviews);
        outState.putParcelableArrayList("trailersList", trailers);

        //saving the scrolling views positions
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{scrollView.getScrollX(), scrollView.getScrollY()});

        super.onSaveInstanceState(outState);


    }


    private void reviewfun(String link) {
        StringRequest stringRequest = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject object = results.getJSONObject(i);
                        String review = object.getString("content");
                        reviews.add(new Review(review));
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


    private void show(String link) {
        StringRequest stringRequest = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject object = results.getJSONObject(i);
                        key.add(object.getString("key"));
                        trailer = new Trailer(object.getString("key"));
                        trailers.add(trailer);
                        trailerAdapter.notifyDataSetChanged();

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
