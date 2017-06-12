package com.example.android.githubsearchwithlifecycle;

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

import com.example.android.githubsearchwithlifecycle.utils.Connection;
import com.example.android.githubsearchwithlifecycle.utils.DirectionsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DirectionsAcitivity extends AppCompatActivity
        implements  LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = DirectionsAcitivity.class.getSimpleName();
    private static final String SEARCH_RESULTS_LIST_KEY = "searchResultsList";
    private static final String SEARCH_URL_KEY = "githubSearchURL";
    private static final int GITHUB_SEARCH_LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;

    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private GitHubSearchAdapter mGitHubSearchAdapter;

    private ArrayList<DirectionsUtils.SearchResult> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultsList = null;

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);

        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mGitHubSearchAdapter = new GitHubSearchAdapter();
        mSearchResultsRV.setAdapter(mGitHubSearchAdapter);

        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();

                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });
    }

    private void doGitHubSearch(String searchQuery) {
        String githubSearchUrl = DirectionsUtils.buildGitHubSearchURL(searchQuery);
        String tempURL = "https://maps.googleapis.com/maps/api/directions/json?origin=Corvallis&destination=Portland,OR&mode=transit&key=AIzaSyBkm5Tsa1eKScVBsXo277WBTdFo4i-YvXc";

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, tempURL);
        getSupportLoaderManager().restartLoader(GITHUB_SEARCH_LOADER_ID, argsBundle, this);
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String> (this) {

            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d(TAG, "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
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
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSearchResultsList = DirectionsUtils.parseGitHubSearchResultsJSON(data);
            mGitHubSearchAdapter.updateSearchResults(mSearchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }
}
