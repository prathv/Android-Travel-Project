package com.example.srkanna.location;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srkanna.location.utils.YelpUtils;

import java.io.InputStream;

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mSearchResultStarsTV;
    private TextView mSearchResultPhone;
    private YelpUtils.SearchResult mSearchResult;
    private ImageView mSearchResultStar1;
    private ImageView mSearchResultStar2;
    private ImageView mSearchResultStar3;
    private ImageView mSearchResultStar4;
    private ImageView mSearchResultStar5;
    private ImageView mSearchResultYelpPic;
    private String latu;
    private String longu;

    private Button mviewOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yelp_activity_search_result_detail);

        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
        mSearchResultPhone = (TextView)findViewById(R.id.tv_search_result_phone);
        mSearchResultStar1 = (ImageView)findViewById(R.id.star1);
        mSearchResultStar2 = (ImageView)findViewById(R.id.star2);
        mSearchResultStar3 = (ImageView)findViewById(R.id.star3);
        mSearchResultStar4 = (ImageView)findViewById(R.id.star4);
        mSearchResultStar5 = (ImageView)findViewById(R.id.star5);
        mSearchResultYelpPic = (ImageView) findViewById(R.id.YelpImage);
        mviewOnMap =(Button) findViewById(R.id.viewonmap);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(YelpUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (YelpUtils.SearchResult)intent.getSerializableExtra(YelpUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(mSearchResult.fullName);
            latu = mSearchResult.lati;
            longu = mSearchResult.longi;

            if(mSearchResult.description.compareTo("false")==1){
                mSearchResultDescriptionTV.setText("Not Closed");
            }
            else{
                mSearchResultDescriptionTV.setText("Closed");

            }

            Log.d("SearchResultActivity","Image url is"+mSearchResult.imageUrl);
            switch (mSearchResult.rating){
                case 1: mSearchResultStar1.setVisibility(View.VISIBLE);break;
                case 2: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);break;
                case 3: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);break;
                case 4: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);mSearchResultStar4.setVisibility(View.VISIBLE);break;
                case 5: mSearchResultStar1.setVisibility(View.VISIBLE);mSearchResultStar2.setVisibility(View.VISIBLE);mSearchResultStar3.setVisibility(View.VISIBLE);mSearchResultStar4.setVisibility(View.VISIBLE);mSearchResultStar5.setVisibility(View.VISIBLE);
                default: break;
            }


            // show The Image in a ImageView
            new DownloadImageTask((ImageView) findViewById(R.id.YelpImage))
                    .execute(mSearchResult.imageUrl);

            mSearchResultPhone.setText(mSearchResult.phone);
        }

        mviewOnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg) {
                Intent weatherIntent = new Intent(SearchResultDetailActivity.this, DirectionsAcitivityForYelp.class);
                weatherIntent.putExtra("latu", latu);
                weatherIntent.putExtra("longu", longu);
                // pass it the city, state info
                startActivity(weatherIntent); // launch the activity
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_repo:
                viewRepoOnWeb();
                return true;
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result_detail, menu);
        return true;
    }

    public void viewRepoOnWeb() {
        if (mSearchResult != null) {
            Uri repoUri = Uri.parse(mSearchResult.htmlURL);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, repoUri);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }

    }

    public void shareRepo() {
        if (mSearchResult != null) {
            String shareText = mSearchResult.fullName + ": " + mSearchResult.htmlURL;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }
    }

}
