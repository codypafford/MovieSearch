package com.example.moviesearch;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesearch.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    int count = 1;
    ProgressBar progressBar;
    int NUMBER_OF_FIELDS = 7;


    List mv_id_list = new ArrayList();



    Button btnHit;
    TextView txtJson;
    ProgressDialog pd;
    String url;
    String input;
    EditText input_box;
    String json;
    //-----------------------------------------------------------------
    // THE TEXTVIEWS AND IMAGE VIEWS
    TextView title0;
    TextView rating0;
    TextView rated0;
    TextView genre0;
    ImageView poster0;
    ImageView sm0;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentView(R.layout.start_up_page);
        Button start_searching = findViewById(R.id.button);
        start_searching.setOnClickListener(this);

//        main_startup_routine();
//        context = this;
//
//
//        input_box =(EditText) findViewById(R.id.input_search);
//
//
//        btnHit = (Button) findViewById(R.id.btnHit);
//        txtJson = (TextView) findViewById(R.id.tvJsonItem);
//
//        btnHit.setOnClickListener(this);
//
//        title0 = findViewById(R.id.title0);
//        rating0 = findViewById(R.id.rating0);
//        rated0 = findViewById(R.id.rated0);
//        genre0 = findViewById(R.id.genre0);
//        poster0 = findViewById(R.id.poster0);
//        sm0 = findViewById(R.id.sm0);
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setMax(10);


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button: // the start searching button on the start-up page
                setContentView(R.layout.activity_main);
                main_startup_routine();


                break;

            case R.id.btnHit:
                txtJson.setText("Searching...");
                count =1;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                MyTask myTask = new MyTask();
                myTask.isRunning = true;
                myTask.execute(10);
                searchMoviesandGetIDs(); //put the ids in a list
                populateFields();
                myTask.isRunning = false;

                ScrollView scrollView = findViewById(R.id.sv);
                scrollView.setVisibility(View.VISIBLE);

                // HIDES KEYBOARD AFTER CLICK OF BUTTON
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }


                break;

            case R.id.poster0:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                break;

            case R.id.sm0:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(0));
                break;

            case R.id.sm1:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(1));
                break;

            case R.id.sm2:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(2));
                break;

            case R.id.sm3:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(3));
                break;

            case R.id.sm4:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(4));
                break;

            case R.id.sm5:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(5));
                break;

            case R.id.sm6:
                setContentView(R.layout.movie_description);
                setUpMovieDescPage(mv_id_list.get(6));
                break;


            default:
                break;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        setContentView(R.layout.activity_main);
        main_startup_routine();
        populateFields();
        ScrollView sv = findViewById(R.id.sv);
        sv.setVisibility(View.VISIBLE);
        txtJson.setText("Results found: " + mv_id_list.size());
        return true;
    }

    private void setUpMovieDescPage(Object id) {
        ActionBar actionBar = getActionBar();
        //   actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        url = "https://www.omdbapi.com/?i=" + id + "&apikey=MY_API_KEY";
        json = getJSON(url);

        JSONObject obj;
        try {
            obj = new JSONObject(json);
            String title = obj.getString("Title");
            String poster_url = obj.getString("Poster");
            String str_actors = obj.getString("Actors");
            String str_directors = obj.getString("Director");
            String str_box_office = obj.getString("BoxOffice");
            String str_runtime = obj.getString("Runtime");
            String str_released = obj.getString("Released");
            
            TextView actors = findViewById(R.id.tv_actors);
            TextView runtime = findViewById(R.id.tv_runtime);
            TextView release_date = findViewById(R.id.tv_release_date);
            TextView director = findViewById(R.id.tv_director);
            TextView box_office = findViewById(R.id.tv_box_office);
            TextView big_title = findViewById(R.id.tv_title);
            ImageView big_poster = findViewById(R.id.tv_poster);

            big_title.setText(title);
            actors.setText(str_actors);
            runtime.setText(str_runtime);
            director.setText(str_directors);
            release_date.setText(str_released);
            box_office.setText(str_box_office);

            Picasso.with(this).load(poster_url).into(big_poster);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //****DONT FORGET TO CHANGE THE 0 BACK TO i. IT WAS CHANGED ONLY FOR TESTING THE RESULTS OF THE FIRST MOVIE IN THE LIST***************
    public void populateFields() {
        int number_to_iterate;
        for(int i = 0; i < NUMBER_OF_FIELDS; i++){
            String hz_id = "hz" + i;
            int hz_x = getResources().getIdentifier(hz_id, "id", getPackageName()); //get id number by string id
            LinearLayout hz_view = findViewById(hz_x);
            hz_view.setVisibility(View.VISIBLE);
        }
        for(int i = mv_id_list.size(); i < NUMBER_OF_FIELDS; i++){
            String hz_id = "hz" + i;
            int hz_x = getResources().getIdentifier(hz_id, "id", getPackageName()); //get id number by string id
            LinearLayout hz_view = findViewById(hz_x);
            hz_view.setVisibility(View.INVISIBLE);
        }
        if (mv_id_list.size() < 7){
            number_to_iterate = mv_id_list.size();
        }else{
            number_to_iterate = NUMBER_OF_FIELDS;
        }
        Log.d("size", String.valueOf(mv_id_list.size()));
        for (int i = 0; i < number_to_iterate ; i++) {
            //GET THE TITLEVIEW--------------------------------------------------------------------------------
            String title_id = "title" + i;
            int title_x = getResources().getIdentifier(title_id, "id", getPackageName()); //get id number by string id
            TextView title_view = findViewById(title_x);
            //--------------------------------------------------------------------------------------------------------------
            String poster_id = "poster" + i;
            int poster_x = getResources().getIdentifier(poster_id, "id", getPackageName()); //get id number by string id
            ImageView poster_view = findViewById(poster_x);
            //--------------------------------------------------------------------------------------------------
            String genre_id = "genre" + i;
            int genre_x = getResources().getIdentifier(genre_id, "id", getPackageName()); //get id number by string id
            TextView genre_view = findViewById(genre_x);
            //--------------------------------------------------------------------------------------------------
            String rating_id = "rating" + i;
            int rating_x = getResources().getIdentifier(rating_id, "id", getPackageName()); //get id number by string id
            TextView rating_view = findViewById(rating_x);
            //--------------------------------------------------------------------------------------------------
            String rated_id = "rated" + i;
            int rated_x = getResources().getIdentifier(rated_id, "id", getPackageName()); //get id number by string id
            TextView rated_view = findViewById(rated_x);



            //********************************************************************************************************
            String id = (String) mv_id_list.get(i); //<-------------------------------------NEED TO CHANGE TO i
            url = "https://www.omdbapi.com/?i=" + id + "&apikey=MY_API_KEY";
            json = getJSON(url);

            JSONObject obj;
            try {
                obj = new JSONObject(json);
                String title = obj.getString("Title");
                String movieYear = obj.getString("Year");
                String rating = obj.getString("imdbRating");
                String genre = obj.getString("Genre");
                String rated = obj.getString("Rated");
                String poster_url = obj.getString("Poster");
                title_view.setText(title + " " + "(" + movieYear + ")");
                genre_view.setText(genre);
                rating_view.setText("Rating:" + " " + rating);
                rated_view.setText(rated);
                Picasso.with(this).load(poster_url).into(poster_view);


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }

    public void searchMoviesandGetIDs() {
        mv_id_list.clear();
        input = String.valueOf(input_box.getText()).trim();
        Log.d("url","https://www.omdbapi.com/?s=" + input + "&apikey=MY_API_KEY" );
        url  = "https://www.omdbapi.com/?s=" + input + "&apikey=MY_API_KEY";
        json = getJSON(url);


        try {
            Log.d("url", json);
            JSONObject obj = new JSONObject(json);
            JSONArray moviesArr = obj.getJSONArray("Search");

            String id;

            final int n = moviesArr.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject movie = moviesArr.getJSONObject(i);

                id = movie.getString("imdbID");
                mv_id_list.add(id);
                Log.d("ids", String.valueOf(mv_id_list));
               // title0.setText(movie.getString("Title"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void main_startup_routine(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        input_box =(EditText) findViewById(R.id.input_search);


        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);

        btnHit.setOnClickListener(this);

        title0 = findViewById(R.id.title0);
        rating0 = findViewById(R.id.rating0);
        rated0 = findViewById(R.id.rated0);
        genre0 = findViewById(R.id.genre0);
        poster0 = findViewById(R.id.poster0);
        sm0 = findViewById(R.id.sm0);

        context = this;


        input_box =(EditText) findViewById(R.id.input_search);


        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);

        btnHit.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);

        CreateListenersForImageViews();

    }

    private void CreateListenersForImageViews() {
        ImageView see_more1 = findViewById(R.id.sm0);
        see_more1.setOnClickListener(this);
        for (int i = 0; i < NUMBER_OF_FIELDS; i++){
            String see_more_x = "sm" + i;
            int see_more_id = getResources().getIdentifier(see_more_x, "id", getPackageName()); //get id number by string id
            ImageView see_more = findViewById(see_more_id);
            see_more.setOnClickListener(this);
        }

    }


    class MyTask extends AsyncTask<Integer, Integer, String> {
        Boolean isRunning = false;
        @Override
        protected String doInBackground(Integer... params) {
            while (isRunning) {
                for (; count <= params[0]; count++) {
                    if (!isRunning) break;
                    try {
                        Thread.sleep(1000);
                        publishProgress(count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count == params[0]){
                        return "Search Timed Out";
                    }
                }
                return mv_id_list.size() + " Results Found";
            }
            return mv_id_list.size() + " Results Found";
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            txtJson.setText(result);
            btnHit.setText("Search Again");
        }
        @Override
        protected void onPreExecute() {
           // txtJson.setText("Task Starting...");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
          //  txtJson.setText("Running..."+ values[0]);
            progressBar.setProgress(values[0]);
        }
    }


}

