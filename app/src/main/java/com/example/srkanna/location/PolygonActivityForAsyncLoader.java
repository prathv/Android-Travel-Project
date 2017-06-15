package com.example.srkanna.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.srkanna.location.utils.Connection;
import com.example.srkanna.location.utils.DirectionsUtils;

import java.util.ArrayList;

public class PolygonActivityForAsyncLoader extends AppCompatActivity
        implements  LoaderManager.LoaderCallbacks<String> {

    String latitude,longitude, locationformarker;
    private static final String TAG = PolygonActivityForAsyncLoader.class.getSimpleName();
    private static final String SEARCH_URL_KEY = "directionURL";
    private static final int DIRECETION_LOADER_ID = 1;

    private RecyclerView DirectionResultsRV;
//    private EditText mDirectionBoxET;

    private ProgressBar mLoadingIndicatorDirectionPB;
    private TextView mLoadingErrorMessageDirectionTV;
    private DirectionAdapter mDirectionAdapter;

    private ArrayList<DirectionsUtils.SearchResult> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directionsformapinyelp);

        mSearchResultsList = null;

//        mDirectionBoxET = (EditText)findViewById(R.id.et_search_box_direction);

        mLoadingIndicatorDirectionPB = (ProgressBar)findViewById(R.id.pb_loading_direction_indicator);
        mLoadingErrorMessageDirectionTV = (TextView)findViewById(R.id.tv_loading_error_direction_message);
        DirectionResultsRV = (RecyclerView)findViewById(R.id.rv_directions_results);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DirectionResultsRV.setLayoutManager(layoutManager);
        DirectionResultsRV.setHasFixedSize(true);

        mDirectionAdapter = new DirectionAdapter();
        DirectionResultsRV.setAdapter(mDirectionAdapter);

        Bundle args = getIntent().getExtras();


        locationformarker = args.getString("Location");
      //  String latandLong = latitude+"," +longitude;
        String tempLatlong = DirectionsUtils.buildURLForPolyline("Baltimore" ,locationformarker);
        Log.d("Making the URL", tempLatlong);
        args.putString(SEARCH_URL_KEY,tempLatlong);

        getSupportLoaderManager().initLoader(DIRECETION_LOADER_ID, args, this);

    }



    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d(TAG, "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorDirectionPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if (args != null) {
                    String githubSearchUrl = args.getString(SEARCH_URL_KEY);
                    Log.d(TAG, "AsyncTaskLoader making network call xxcx: " + githubSearchUrl);
                    String searchResults = null;
                    String latAndLong=null;
                    String temp=null;
                    try {
                        searchResults = Connection.doHTTPGet(githubSearchUrl);
                        if(!searchResults.isEmpty()) {

                             latAndLong = DirectionsUtils.returnCoordinatesForPolyline(searchResults);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return latAndLong;
                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorDirectionPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            Intent i = new Intent(this, PolygonAcitivityForRoute.class);
            i.putExtra("latAndLong", data );

            startActivity(i);
        } else {
        Log.d("No data", "Sorry, no data returned");
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }
}
