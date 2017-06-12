package com.example.srkanna.location;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.srkanna.location.utils.Connection;
import com.example.srkanna.location.utils.DirectionsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DirectionsAcitivity extends AppCompatActivity
        implements  LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = DirectionsAcitivity.class.getSimpleName();
    private static final String SEARCH_URL_KEY = "directionURL";
    private static final int DIRECETION_LOADER_ID = 0;

    private RecyclerView DirectionResultsRV;
    private EditText mDirectionBoxET;

    private ProgressBar mLoadingIndicatorDirectionPB;
    private TextView mLoadingErrorMessageDirectionTV;
    private DirectionAdapter mDirectionAdapter;

    private ArrayList<DirectionsUtils.SearchResult> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);

        mSearchResultsList = null;

        mDirectionBoxET = (EditText)findViewById(R.id.et_search_box_direction);

        mLoadingIndicatorDirectionPB = (ProgressBar)findViewById(R.id.pb_loading_direction_indicator);
        mLoadingErrorMessageDirectionTV = (TextView)findViewById(R.id.tv_loading_error_direction_message);
        DirectionResultsRV = (RecyclerView)findViewById(R.id.rv_directions_results);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DirectionResultsRV.setLayoutManager(layoutManager);
        DirectionResultsRV.setHasFixedSize(true);

        mDirectionAdapter = new DirectionAdapter();
        DirectionResultsRV.setAdapter(mDirectionAdapter);

        getSupportLoaderManager().initLoader(DIRECETION_LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search_direction);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mDirectionBoxET.getText().toString();

                if (!TextUtils.isEmpty(searchQuery)) {
                    doDirectionSearch(searchQuery);
                }
            }
        });
    }

    private void doDirectionSearch(String searchQuery) {
        String githubSearchUrl = DirectionsUtils.buildGitHubSearchURL(searchQuery);
        String tempURL = "https://maps.googleapis.com/maps/api/directions/json?origin=Corvallis&destination=Portland,OR&mode=transit&key=AIzaSyBkm5Tsa1eKScVBsXo277WBTdFo4i-YvXc";

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, tempURL);
        getSupportLoaderManager().restartLoader(DIRECETION_LOADER_ID, argsBundle, this);
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
                    Log.d(TAG, "AsyncTaskLoader making network call: " + githubSearchUrl);
                    String searchResults = null;
                    try {
                        searchResults = Connection.doHTTPGet(githubSearchUrl);
                        if(!searchResults.isEmpty()) {
//                            Log.d(TAG, "I am here"+searchResults);
                            JSONObject searchResultsObj = new JSONObject(searchResults);
                            JSONArray searchResultsRoutes = searchResultsObj.getJSONArray("routes");
                            JSONArray searchResultsLegs = searchResultsRoutes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                            Log.d(TAG, "I am here"+searchResultsLegs);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return searchResults;
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
            mLoadingErrorMessageDirectionTV.setVisibility(View.INVISIBLE);
            DirectionResultsRV.setVisibility(View.VISIBLE);
            mSearchResultsList = DirectionsUtils.parseGitHubSearchResultsJSON(data);
            mDirectionAdapter.updateSearchResults(mSearchResultsList);
        } else {
            DirectionResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageDirectionTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }
}
