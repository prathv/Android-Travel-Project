package com.example.srkanna.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.srkanna.location.utils.DirectionsUtils;

import java.util.ArrayList;

/**
 * Created by hessro on 4/21/17.
 */

public class GitHubSearchAdapter extends RecyclerView.Adapter<GitHubSearchAdapter.SearchResultViewHolder> {
    private ArrayList<DirectionsUtils.SearchResult> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public GitHubSearchAdapter() {

    }

    public void updateSearchResults(ArrayList<DirectionsUtils.SearchResult> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(DirectionsUtils.SearchResult searchResult);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            itemView.setOnClickListener(this);
        }

        public void bind(DirectionsUtils.SearchResult searchResult) {
            mSearchResultTV.setText(searchResult.steps);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
