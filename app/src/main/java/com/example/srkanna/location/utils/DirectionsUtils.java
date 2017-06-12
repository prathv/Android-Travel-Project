package com.example.android.githubsearchwithlifecycle.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hessro on 4/25/17.
 */

public class DirectionsUtils {

    private final static String GITHUB_SEARCH_BASE_URL = "https://api.github.com/search/repositories";
    private final static String GITHUB_SEARCH_QUERY_PARAM = "q";
    private final static String GITHUB_SEARCH_SORT_PARAM = "sort";
    private final static String GITHUB_SEARCH_DEFAULT_SORT = "stars";

    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "DirectionsUtils.SearchResult";
        public String steps;
//        public String fullName;
//        public String description;
//        public String htmlURL;
//        public int stars;
    }

//    public static String buildGitHubSearchURL(String searchQuery) {
//        return Uri.parse(GITHUB_SEARCH_BASE_URL).buildUpon()
//                .appendQueryParameter(GITHUB_SEARCH_QUERY_PARAM, searchQuery)
//                .appendQueryParameter(GITHUB_SEARCH_SORT_PARAM, GITHUB_SEARCH_DEFAULT_SORT)
//                .build()
//                .toString();
//    }

    public static String buildGitHubSearchURL(String searchQuery) {
    return "https://maps.googleapis.com/maps/api/directions/json?origin=Corvallis&destination=Portland,OR&mode=transit&key=AIzaSyBkm5Tsa1eKScVBsXo277WBTdFo4i-YvXc";
    }

    public static ArrayList<SearchResult> parseGitHubSearchResultsJSON(String searchResultsJSON) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsRoutes = searchResultsObj.getJSONArray("routes");
            JSONArray searchResultsLegs = searchResultsRoutes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            String abc=searchResultsLegs.toString();

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();

            for (int i=0;i<searchResultsLegs.length();i++) {
                SearchResult searchResult = new SearchResult();
                JSONObject directionItem = searchResultsLegs.getJSONObject(i);
                searchResult.steps = directionItem.getString("html_instructions");
                searchResultsList.add(searchResult);
                Log.d("sa", "I am here with" + directionItem.getString("html_instructions"));

            }
            return  searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }
}
