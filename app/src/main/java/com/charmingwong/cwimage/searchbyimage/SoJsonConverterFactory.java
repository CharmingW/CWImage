package com.charmingwong.cwimage.searchbyimage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/23.
 */

public class SoJsonConverterFactory extends Converter.Factory {

    private static final String TAG = "SoJsonConverterFactory";

    private SoJsonConverterFactory() {
    }

    public static SoJsonConverterFactory create() {
        return new SoJsonConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new SearchByImageConverter();
    }

    private class SearchByImageConverter implements Converter<ResponseBody, List<SoImage>> {

        @Override
        public List<SoImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                JSONObject jsonObject = new JSONObject(result);
                Log.i(TAG, "convert: --------------------------------------------------------------------");
                Log.i(TAG, "convert: " + jsonObject.getJSONObject("extra").getInt("totalNum"));
                Log.i(TAG, "convert: " + jsonObject.getInt("errno"));
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<SoImage> soImages = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    soImages.add(new SoImage(jsonArray.getJSONObject(i)));
                }
                Log.i(TAG, "convert: " + soImages.size());
                Log.i(TAG, "convert: --------------------------------------------------------------------");
                return soImages;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "图片频道 json 解析出错");
            }
            return null;
        }
    }
}
