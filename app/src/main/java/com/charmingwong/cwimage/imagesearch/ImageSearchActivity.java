package com.charmingwong.cwimage.imagesearch;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.charmingwong.cwimage.base.BaseActivity;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.dao.DaoManager;
import com.charmingwong.cwimage.imagesearch.api.BaiduApi;
import com.charmingwong.cwimage.imagesearch.api.BaseApi;
import com.charmingwong.cwimage.imagesearch.api.BingApi;
import com.charmingwong.cwimage.imagesearch.api.ChinasoApi;
import com.charmingwong.cwimage.imagesearch.api.GoogleApi;
import com.charmingwong.cwimage.imagesearch.api.QHApi;
import com.charmingwong.cwimage.imagesearch.api.SogouApi;
import com.charmingwong.cwimage.util.ActivityUtils;

public class ImageSearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTheme().applyStyle(R.style.cursor, true);

        setContentView(R.layout.activity_image_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);

        ImageView imageView = (ImageView) findViewById(R.id.bar_image);
        Glide.with(this)
                .load(R.drawable.ic_bar_3)
                .into(imageView);

        Bundle bundle = getIntent().getExtras();

        String query = bundle.getString("query");
        getSupportActionBar().setTitle(query);

        ImageSearchFragment fragment = (ImageSearchFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = ImageSearchFragment.newInstance();
            fragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        BaseApi api;

        int type = bundle.getInt("api", ImageFinder.QH360);
        switch (type) {
            case ImageFinder.BAIDU:
                api = BaiduApi.newInstance();
                break;
            case ImageFinder.SOGOU:
                api = SogouApi.newInstance();
                break;
            case ImageFinder.QH360:
                api = QHApi.newInstance();
                break;
            case ImageFinder.CHINASO:
                api = ChinasoApi.newInstance();
                break;
            case ImageFinder.BING:
                api = BingApi.newInstance();
                break;
            case ImageFinder.GOOGLE:
                api = GoogleApi.newInstance();
                break;
            default:
                api = QHApi.newInstance();
                break;
        }

        int searchFlag = bundle.getInt("search_flag");
        api.setSearchFlag(searchFlag);

        new ImageSearchPresenter(fragment, api, DaoManager.getInstance(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
