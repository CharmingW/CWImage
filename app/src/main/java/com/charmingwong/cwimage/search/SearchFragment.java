package com.charmingwong.cwimage.search;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.imagelibrary.CollectionTag;
import com.charmingwong.cwimage.imagesearch.ImageFinder;
import com.charmingwong.cwimage.imagesearch.ImageSearchActivity;
import com.charmingwong.cwimage.search.model.HotSearch;
import com.charmingwong.cwimage.search.model.SearchRecord;
import com.charmingwong.cwimage.searchbyimage.SearchByImageActivity;
import com.charmingwong.cwimage.util.ApplicationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public class SearchFragment extends Fragment implements SearchContract.View, SearchActivity.ActivityActionListener {

    private static final String TAG = "SearchFragment";

    private static final int TAKE_PHOTO = 1;

    private static final int PICK_IMAGE = 2;

    private static final int CROP = 3;

    private static final int VOICE = 4;

    private SearchContract.Presenter mPresenter;

    private FloatingSearchView mSearchView;

    private EditText mSearchInput;

    private DrawerLayout mDrawerLayout;

    private HotSearchListAdapter mHotSearchListAdapter;

    private TagsListAdapter mTagsListAdapter;

    private int api;

    private List<HotSearch> mHotSearches;

    private int mSearchFlag = 0;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SearchActivity) getActivity()).setActivityActionListener(this);
        mHotSearches = new ArrayList<>(0);
        mPresenter.loadHotSearches();
        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        api = Integer.parseInt(sharedPreferences.getString("default_engine", "0"));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadTags();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchView = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);

        RecyclerView hotList = (RecyclerView) rootView.findViewById(R.id.hot_list);

        mHotSearchListAdapter = new HotSearchListAdapter();
        hotList.setAdapter(mHotSearchListAdapter);

        RecyclerView.ItemDecoration itemDecorationHot = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int density = ApplicationUtils.getDisplayDensity();
                int border = 10 * density;
                outRect.set(0, border, border, border);
            }
        };

        hotList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        hotList.addItemDecoration(itemDecorationHot);

        RecyclerView.ItemDecoration itemDecorationTag = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int density = ApplicationUtils.getDisplayDensity();
                int border = 5 * density;
                outRect.set(0, border, border, border);
            }
        };

        RecyclerView wallpaperTagList = (RecyclerView) rootView.findViewById(R.id.wallpaper_tag_list);
        TagsListAdapter wallpaperTagsListAdapter = new TagsListAdapter();
        List<String> tags = Arrays.asList(getResources().getStringArray(R.array.wallpaper_channel));
        wallpaperTagsListAdapter.setTags(tags);
        wallpaperTagsListAdapter.setSearchFlag(1);
        wallpaperTagList.setAdapter(wallpaperTagsListAdapter);
        wallpaperTagList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        wallpaperTagList.addItemDecoration(itemDecorationTag);

        RecyclerView tagList = (RecyclerView) rootView.findViewById(R.id.tag_list);
        mTagsListAdapter = new TagsListAdapter();
        tagList.setAdapter(mTagsListAdapter);
        tagList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        tagList.addItemDecoration(itemDecorationTag);

        return rootView;
    }

    private List<String> getCollectionTags(List<CollectionTag> collectionTags) {
        List<String> tags = new ArrayList<>(collectionTags.size());
        for (int i = 0; i < collectionTags.size(); i++) {
            tags.add(collectionTags.get(i).getTag());
        }
        return tags;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        setupSearchView();

        mSearchInput = (EditText) mSearchView.findViewById(R.id.search_bar_text);
        mSearchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        mSearchInput.requestFocus();

    }

    private void setupSearchView() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String s, String s1) {
                if (!"".equals(s1.trim())) {
                    mPresenter.loadSearchSuggestions(s1);
                } else {
                    mSearchView.clearSuggestions();
                    mPresenter.loadSearchRecord();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                startSearchResultActivity(searchSuggestion.getBody());
                mPresenter.saveSearchRecord(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String s) {
                if (!"".equals(s.trim())) {
                    startSearchResultActivity(s);
                    mPresenter.saveSearchRecord(s);
                }
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View view, ImageView imageView, TextView textView, SearchSuggestion searchSuggestion, int i) {
                if (!((ImageSearchSuggestion) searchSuggestion).isSearchRecord()) {
                    imageView.setImageResource(R.drawable.ic_search_black_24dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_search_recent_black_24dp);
                }
                textView.setText(searchSuggestion.getBody());
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                String query = mSearchInput.getText().toString();
                if (!"".equals(query.trim())) {
                    mPresenter.loadSearchSuggestions(query);
                } else {
                    mPresenter.loadSearchRecord();
                }
            }

            @Override
            public void onFocusCleared() {

            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.camera) {
                    selectWay();
                } else {
                    if (item.getItemId() == R.id.wallpaper) {
                        mSearchFlag = 1;
                    } else if (item.getItemId() == R.id.picture) {
                        mSearchFlag = 0;
                    }
                    item.setChecked(true);
                }
            }
        });

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.baidu) {
                    api = ImageFinder.BAIDU;
                } else if (item.getItemId() == R.id.sogou) {
                    api = ImageFinder.SOGOU;
                } else if (item.getItemId() == R.id.qh) {
                    api = ImageFinder.QH360;
                } else if (item.getItemId() == R.id.chinaso) {
                    api = ImageFinder.CHINASO;
                } else if (item.getItemId() == R.id.bing) {
                    api = ImageFinder.BING;
                } else if (item.getItemId() == R.id.google) {
                    api = ImageFinder.GOOGLE;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getMenu().getItem(api).setChecked(true);
    }


    private void selectWay() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.select_title))
                .setItems(R.array.select_image_way_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent take_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                take_photo.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(getContext().getExternalCacheDir() + "/soImage", "image.jpg")));
                                startActivityForResult(take_photo, TAKE_PHOTO);
                                break;
                            case 1:
                                Intent pick_image = new Intent(Intent.ACTION_PICK);
                                pick_image.setType("image/*");
                                startActivityForResult(pick_image, PICK_IMAGE);
                                break;
                            default:
                                break;
                        }
                    }
                }).create().show();
    }

    private void selectSearchWay(final int requestCode, final Intent data) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.select_title))
                .setItems(R.array.select_search_way_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (requestCode == TAKE_PHOTO) {
                                    takeToCrop();
                                } else {
                                    pickToCrop(data);
                                }
                                break;
                            case 1:
                                if (requestCode == TAKE_PHOTO) {
                                    Intent intent = new Intent(getActivity(), SearchByImageActivity.class);
                                    intent.putExtra("image", new File(getContext().getExternalCacheDir() + "/soImage", "image.jpg"));
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), SearchByImageActivity.class);
                                    intent.putExtra("image", new File(ApplicationUtils.getFilePathFromContentUri(data.getData(), getContext().getContentResolver())));
                                    startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }).create().show();
    }

    private void takeToCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(getContext().getExternalCacheDir() + "/soImage", "image.jpg")), "image/*");
        configCropIntent(intent);
    }

    private void pickToCrop(Intent data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data.getData(), "image/*");
        configCropIntent(intent);
    }

    private void configCropIntent(Intent intent) {
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//                    take_photo_crop.putExtra("aspectX", 1);
//                    take_photo_crop.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//                    take_photo_crop.putExtra("outputX", 1000);
//                    take_photo_crop.putExtra("outputY", 1000);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getContext().getExternalCacheDir() + "/soImage", "crop_image.jpg")));
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (requestCode == TAKE_PHOTO) {
                    selectSearchWay(TAKE_PHOTO, null);
                }
            } else {
                switch (requestCode) {
                    case PICK_IMAGE:
                        selectSearchWay(PICK_IMAGE, data);
                        break;
                    case CROP:
                        Intent intent = new Intent(getActivity(), SearchByImageActivity.class);
                        intent.putExtra("image", new File(getContext().getExternalCacheDir() + "/soImage", "crop_image.jpg"));
                        startActivity(intent);
                        break;
                }
            }
        }
    }

    private void startSearchResultActivity(String query) {
        Intent intent = new Intent(getActivity(), ImageSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        bundle.putInt("api", api);
        bundle.putInt("search_flag", mSearchFlag);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSearchInput.clearFocus();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSearchSuggestions(List<ImageSearchSuggestion> suggestions) {
        if (!"".equals(mSearchInput.getText().toString().trim())) {
            mSearchView.swapSuggestions(suggestions);
        }
    }

    @Override
    public void showSuggestionsFailed() {

    }

    @Override
    public void showSearchRecord(List<SearchRecord> records) {
        List<ImageSearchSuggestion> imageSearchSuggestions = new ArrayList<>(records.size());
        for (SearchRecord record : records) {
            ImageSearchSuggestion searchSuggestion = new ImageSearchSuggestion(record.getQuery());
            searchSuggestion.setSearchRecord(true);
            imageSearchSuggestions.add(searchSuggestion);
        }
        mSearchView.swapSuggestions(imageSearchSuggestions);
    }

    @Override
    public void showHotSearches(List<HotSearch> hotSearches) {
        mHotSearches = hotSearches;
        mHotSearchListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showHotSearchesFailed() {

    }

    @Override
    public void showTags(List<CollectionTag> collectionTags) {
        mTagsListAdapter.setTags(getCollectionTags(collectionTags));
        mTagsListAdapter.notifyDataSetChanged();
    }

    private class HotSearchListAdapter extends RecyclerView.Adapter<HotSearchListAdapter.HotSearchViewHolder> {

        @Override
        public HotSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_search, parent, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageSearchActivity.class);
                    String query = (String) v.findViewById(R.id.query).getTag();
                    Bundle bundle = new Bundle();
                    bundle.putString("query", query);
                    bundle.putInt("api", api);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);

                    mPresenter.saveSearchRecord(query);
                }
            });

            itemView.setClipToOutline(true);

            return new HotSearchViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(HotSearchViewHolder holder, int position) {

            HotSearch hotSearch = mHotSearches.get(position);
            String imageUrl = hotSearch.getImageUrl();
            String query = hotSearch.getQuery();

            Glide.with(getActivity())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

            holder.query.setText(query);
            holder.query.setTag(query);
        }

        @Override
        public int getItemCount() {
            return mHotSearches.size();
        }

        class HotSearchViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            TextView query;

            public HotSearchViewHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.image);

                query = (TextView) itemView.findViewById(R.id.query);
            }
        }
    }

    private class TagsListAdapter extends RecyclerView.Adapter<TagsListAdapter.TagsViewHolder> {

        private List<String> mTags;

        private int mSearchFlag;

        public void setTags(List<String> tags) {
            mTags = tags;
        }

        public void setSearchFlag(int flag) {
            mSearchFlag = flag;
        }

        @Override
        public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_seach_tag, parent, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageSearchActivity.class);
                    String query = (String) v.findViewById(R.id.tag).getTag();
                    Bundle bundle = new Bundle();
                    bundle.putInt("api", api);
                    if (mSearchFlag == 1) {
                        bundle.putInt("search_flag", 1);
                        bundle.putString("query", query + "手机壁纸");
                    } else {
                        bundle.putString("query", query);
                        mPresenter.saveSearchRecord(query);
                    }
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

            return new TagsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TagsViewHolder holder, int position) {

            holder.tag.setText(mTags.get(position));
            holder.tag.setTag(mTags.get(position));
        }

        @Override
        public int getItemCount() {
            return mTags != null ? mTags.size() : 0;
        }

        class TagsViewHolder extends RecyclerView.ViewHolder {

            TextView tag;

            public TagsViewHolder(View itemView) {
                super(itemView);
                tag = (TextView) itemView.findViewById(R.id.tag);
            }
        }
    }

    @Override
    public boolean canGoBack() {
        if (mSearchInput.isFocused()) {
            mSearchInput.clearFocus();
            return true;
        }
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

}
