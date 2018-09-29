package com.charmingwong.cwimage.searchbyimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SearchByImageContract.View) {
            new SearchByImagePresenter((SearchByImageContract.View) fragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_image);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar.setTitle("");
        }

        ImageView imageView = findViewById(R.id.bar_image);
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
