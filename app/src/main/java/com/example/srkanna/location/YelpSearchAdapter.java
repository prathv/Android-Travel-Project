package com.example.srkanna.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srkanna.location.utils.YelpUtils;

import java.util.ArrayList;

/**
 * Created by hessro on 4/21/17.
 */

public class YelpSearchAdapter extends RecyclerView.Adapter<YelpSearchAdapter.SearchResultViewHolder> {
    private ArrayList<YelpUtils.SearchResult> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;


    public YelpSearchAdapter(OnSearchResultClickListener clickListener) {
        mSearchResultClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<YelpUtils.SearchResult> searchResultsList) {
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
        void onSearchResultClick(YelpUtils.SearchResult searchResult);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;
        private ImageView mSearchResultStar1;
        private ImageView mSearchResultStar2;
        private ImageView mSearchResultStar3;
        private ImageView mSearchResultStar4;
        private ImageView mSearchResultStar5;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            mSearchResultStar1 = (ImageView)itemView.findViewById(R.id.star1);
            mSearchResultStar2 = (ImageView)itemView.findViewById(R.id.star2);
            mSearchResultStar3 = (ImageView)itemView.findViewById(R.id.star3);
            mSearchResultStar4 = (ImageView)itemView.findViewById(R.id.star4);
            mSearchResultStar5 = (ImageView)itemView.findViewById(R.id.star5);
            itemView.setOnClickListener(this);
        }

        public void bind(YelpUtils.SearchResult searchResult) {
            mSearchResultTV.setText(searchResult.fullName);

            switch (searchResult.rating){
                case 1: mSearchResultStar1.setVisibility(View.VISIBLE);break;
                case 2: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);break;
                case 3: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);break;
                case 4: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);mSearchResultStar4.setVisibility(View.VISIBLE);break;
                case 5: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);mSearchResultStar4.setVisibility(View.VISIBLE);mSearchResultStar5.setVisibility(View.VISIBLE);
                default: break;
            }

            // show The Image in a ImageView
            new SearchResultDetailActivity.DownloadImageTask((ImageView) itemView.findViewById(R.id.YelpImage))
                    .execute(searchResult.imageUrl);
        }

        @Override
        public void onClick(View v) {
            YelpUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }
}
