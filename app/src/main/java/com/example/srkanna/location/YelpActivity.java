package com.example.srkanna.location;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.preference.PreferenceManager;


import com.example.srkanna.location.utils.YelpUtils;
import com.example.srkanna.location.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class YelpActivity extends AppCompatActivity implements YelpSearchAdapter.OnSearchResultClickListener, LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener  {

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private Toast mSearchResultToast;
    private YelpSearchAdapter mYelpSearchAdapter;
    private EditText mSearchBoxLoc;
    public ArrayList<YelpUtils.SearchResult> searchResultsList;
    private static final int YELP_SEARCH_LOADER_ID = 0;
    private static final String YELP_URL_KEY = "yelpsearchurl";
    private static final String YELP_Auth_KEY = "yelpauthurl";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_main);

        mSearchResultToast = null;

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mSearchBoxLoc = (EditText)findViewById(R.id.et_search_location);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);


        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mYelpSearchAdapter = new YelpSearchAdapter(this);
        mSearchResultsRV.setAdapter(mYelpSearchAdapter);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String yelpURL = YelpUtils.buildYelpSearchURL(
                "food",
                "usa",
                "distance"
        );

        String yelpauth = YelpUtils.buildYelpauthURL();
        Log.d("YelpActivity","yelp url is "+yelpURL);
        Log.d("YelpActivity","yelp auth is "+yelpauth);

        Bundle argsBundle = new Bundle();
        argsBundle.putString(YELP_URL_KEY, yelpURL);
        argsBundle.putString(YELP_Auth_KEY,yelpauth);

        getSupportLoaderManager().initLoader(YELP_SEARCH_LOADER_ID, argsBundle, this);


        //getSupportLoaderManager().restartLoader(YELP_SEARCH_LOADER_ID,argsBundle,this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = mSearchBoxET.getText().toString();
                String searchLoc = mSearchBoxLoc.getText().toString();
                Log.d("Mainactictty","entered search button");
                Yelpsearch(searchTerm,searchLoc);

            }
        });

    }
    private void Yelpsearch(String searchQuery, String searchLoc) {

        String yelpauthUrl = YelpUtils.buildYelpauthURL();
        String yelpSearchUrl = YelpUtils.buildYelpSearchURL(searchQuery, searchLoc, "distance");

        Bundle argsBundle = new Bundle();
        argsBundle.putString(YELP_URL_KEY, yelpSearchUrl);
        argsBundle.putString(YELP_Auth_KEY,yelpauthUrl);
        getSupportLoaderManager().restartLoader(YELP_SEARCH_LOADER_ID, argsBundle, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("Mainactivity","Entered case settings");
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchResultClick(YelpUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(YelpUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mSearchResultsJSON;
            @Override
            protected void onStartLoading() {

                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d("Mainactivity", "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {

                        Log.d("Mainactivity", "AsyncTaskLoader not delivering cached results"+args+" "+mSearchResultsJSON);

                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if (args != null) {
                    Log.d("Mainactivity","Loaded first background call");
                    String yelpurl = args.getString(YELP_URL_KEY);
                    String yelpauth = args.getString(YELP_Auth_KEY);

                    Log.d("Mainactivity", "AsyncTaskLoader making network call: " + yelpurl);
                    String getresult = "";


                    Log.d("Mainactivity","Search Url is "+yelpurl);
                    try {
                        String authresult = "";
                        String getreviewresult="";

                        //    searchResults = NetworkUtils.doHTTPGet(githubSearchUrl);

                        authresult = NetworkUtils.post(yelpauth);
                        JSONObject accesstoken = new JSONObject(authresult);


                        String access = accesstoken.getString("access_token");
                        Log.d("Mainactivity","access token is "+access);


                        if(!access.isEmpty()){
                            getresult = NetworkUtils.doHTTPGetYelp(yelpurl,access);

                            Log.d("Mainactivity","Get review result is "+getreviewresult);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return getresult;
                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                Log.d("Mainactivity","data is "+ mSearchResultsJSON);

                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d("Mainactivty", "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            searchResultsList = YelpUtils.parseYelpSearchResultsJSON(data);

            mYelpSearchAdapter.updateSearchResults(searchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Mainactivity","Key is "+key);

        String search_term;

        search_term = sharedPreferences.getString(key,"distance");
        String yelpauth = YelpUtils.buildYelpauthURL();
        String yelpurl = YelpUtils.buildYelpSearchURL("","Seattle,us",search_term);


        Bundle argsBundle = new Bundle();
        argsBundle.putString(YELP_URL_KEY,yelpurl);
        argsBundle.putString(YELP_Auth_KEY,yelpauth);
        getSupportLoaderManager().restartLoader(YELP_SEARCH_LOADER_ID,argsBundle,this);
    }

}
