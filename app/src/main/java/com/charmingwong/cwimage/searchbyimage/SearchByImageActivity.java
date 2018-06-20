package com.charmingwong.cwimage.searchbyimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.charmingwong.cwimage.base.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ActivityUtils;

public class SearchByImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_by_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");

        ImageView imageView = (ImageView) findViewById(R.id.bar_image);
        Glide.with(this)
                .load(R.drawable.ic_bar_1)
                .into(imageView);

        SearchByImageFragment fragment = (SearchByImageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = SearchByImageFragment.newInstance();
            Bundle args = new Bundle();
            Intent data = getIntent();
            args.putSerializable("image", data.getSerializableExtra("image"));
            args.putString("imageUrl", data.getStringExtra("imageUrl"));
            fragment.setArguments(args);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        new SearchByImagePresenter(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case  android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
