package com.andrzej.demoapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainToolbar.setElevation(6f);
        }
        setSupportActionBar(mainToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ImageView avatar = (ImageView) findViewById(R.id.detail_avatar);

        TextView detailTitle = (TextView) findViewById(R.id.detail_title);
        TextView detailDescription = (TextView) findViewById(R.id.detail_description);
        TextView detailLanguage = (TextView) findViewById(R.id.detail_language);
        TextView detailWatchers = (TextView) findViewById(R.id.details_watchers);
        TextView detailFork = (TextView) findViewById(R.id.detail_fork);
        TextView detailLastUpdate = (TextView) findViewById(R.id.detail_lastUpdate);

        //Load bundle from cell
        if(getIntent().hasExtra(Element.ELEMENT_TAG)){
            Element element = getIntent().getExtras().getParcelable(Element.ELEMENT_TAG);
            if(element!=null) {
                String sTitle = getResources().getString(R.string.detail_title, element.title);
                String sDesc = getResources().getString(R.string.detail_description, element.description);
                String sLang = getResources().getString(R.string.detail_language, element.language);
                String sWatch = getResources().getString(R.string.detail_watchers, element.watchers);
                String sFork = getResources().getString(R.string.detail_forks, element.forks);
                String sLastUp = getResources().getString(R.string.detail_lastupdate, element.lastUpdate);

                detailTitle.setText(sTitle);
                detailDescription.setText(sDesc);
                detailLanguage.setText(sLang);
                detailWatchers.setText(sWatch);
                detailFork.setText(sFork);
                detailLastUpdate.setText(sLastUp);
            }
            //set avatar
            Picasso.with(this)
                 .load("https://avatars.githubusercontent.com/u/1342004?v=3")
                 .into(avatar);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
