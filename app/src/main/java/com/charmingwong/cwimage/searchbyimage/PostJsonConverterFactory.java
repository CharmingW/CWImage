package com.charmingwong.cwimage.searchbyimage;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/22.
 */
public class PostJsonConverterFactory extends Converter.Factory {

    private static final String TAG = "PostFactory";

    private PostJsonConverterFactory() {
    }

    public static PostJsonConverterFactory create() {
        return new PostJsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new PostJsonConverter();
    }

    private class PostJsonConverter implements Converter<ResponseBody, String> {

        @Override
        public String convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                JSONObject jsonObject = new JSONObject(result);
                String url = jsonObject.getJSONObject("data").getString("imageUrl");
                Log.i(TAG, "convert: " + url);
                Log.i(TAG, "convert: ========================================================");
                return url;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
