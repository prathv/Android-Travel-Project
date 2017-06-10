package com.example.srkanna.location.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hessro on 4/25/17.
 */

public class YelpUtils {



    private final static String YELP_TOKEN_URL = "https://api.yelp.com/oauth2/token";
    private final static String YELP_Search_URL = "https://api.yelp.com/v3/businesses/search";

    private final static String YELP_SEARCH_QUERY = "usa";
    private final static String CLIENT_KEY = "CsBoHv9RJx1PR_dcRBc3-Q";
    private final static String CLIENT_SECRET = "uHm8HuK65BM9ShuNjv7Y6iPm0Seo8Xs55MRlyOb7QoYViSKXXsu2X5zGWSkrOEx0";
    private final static String YELP_SEARCH_PARAM = "location";
    private final static String YELP_SEARCH_SORT_PARAM = "sort_by";



    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "YelpUtils.SearchResult";
        public String fullName;
        public String description;
        public String htmlURL;
        public int stars;
        public String phone;
        public String id;
        public String status;
        public String name;
        public int rating;
        public String imageUrl;
    }


    public static String buildYelpauthURL() {
        return Uri.parse(YELP_TOKEN_URL).buildUpon()
                .appendQueryParameter("client_id", CLIENT_KEY)
                .appendQueryParameter("client_secret", CLIENT_SECRET)
                .appendQueryParameter("grant_type","client_credentials")
                .build()
                .toString();
    }

    public static String buildYelpSearchURL(String term, String loc, String sort) {
        return Uri.parse(YELP_Search_URL).buildUpon()
                .appendQueryParameter("term", term)
                .appendQueryParameter("location", loc)
                .appendQueryParameter("sort_by",sort)
                .appendQueryParameter("limit","20")
                .build()
                .toString();
    }

    public static ArrayList<SearchResult> parseGitHubSearchResultsJSON(String searchResultsJSON) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("items");

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);
                searchResult.fullName = searchResultItem.getString("full_name");
                searchResult.description = searchResultItem.getString("description");
                searchResult.htmlURL = searchResultItem.getString("html_url");
                searchResult.stars = searchResultItem.getInt("stargazers_count");
                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }
    public static ArrayList<SearchResult> parseYelpSearchResultsJSON(String searchResultsJSON) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("businesses");

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);
                searchResult.fullName = searchResultItem.getString("name");
                searchResult.description = searchResultItem.getString("is_closed");
                searchResult.htmlURL = searchResultItem.getString("review_count");
                searchResult.phone = searchResultItem.getString("phone");
                searchResult.id = searchResultItem.getString("id");
                searchResult.rating = Math.round(Float.valueOf(searchResultItem.getString("rating")));
                searchResult.imageUrl = searchResultItem.getString("image_url");

                Log.d("Utils","Rating is "+ searchResult.rating);
                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }

}
