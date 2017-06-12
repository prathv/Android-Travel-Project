package com.example.srkanna.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private ArrayList<String> mForecastData;
    private OnForecastClickListener mForecastClickListener;

    public ForecastAdapter(OnForecastClickListener clickListener)
    {
        mForecastClickListener = clickListener;
    }

    public void updateForecastData(ArrayList<String> forecastData) {
        mForecastData = forecastData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastData != null) {
            return mForecastData.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        holder.bind(mForecastData.get(position));
    }

    public interface OnForecastClickListener
    {
        void onForecastClick(String forecast);
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mForecastTextView;

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastTextView = (TextView)itemView.findViewById(R.id.tv_forecast_text);
            itemView.setOnClickListener(this);
        }

        public void bind(String forecast) {
            mForecastTextView.setText(forecast);
        }

        @Override
        public void onClick(View v)
        {
            String forecast = mForecastData.get(getAdapterPosition());
            mForecastClickListener.onForecastClick(forecast);
        }
    }

}
