package com.charmingwong.cwimage.searchbyimage;

import android.support.annotation.NonNull;
import com.charmingwong.cwimage.common.ApiManager;
import com.charmingwong.cwimage.common.JsonRequestService;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class PostApi {

    private static final String BASE_URL = "http://image.baidu.com/";

    private static final String FROM = "html5";

    private static final boolean NEED_JSON = true;

    private static JsonRequestService jsonRequestService;

    public static PostApi newInstance() {
        return new PostApi();
    }

    private PostApi() {
        jsonRequestService = ApiManager.getInstance().getJsonRequestService(PostJsonConverterFactory.create());
    }

    private QueryListener mQueryListener;

    public void registerSearchSuggestionsListener(QueryListener queryListener) {
        this.mQueryListener = queryListener;
    }

    public void postImage(File imageFile) {
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), imageFile);
        Call<String> call = jsonRequestService.postImage(image, FROM, NEED_JSON);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                mQueryListener.onSearchCompleted(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                mQueryListener.onSearchFailed(new Exception(t));
            }
        });
    }

    public interface QueryListener {

        void onSearchCompleted(String imageUrl);

        void onSearchFailed(Exception e);
    }

}
