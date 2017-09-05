package com.charmingwong.cwimage.imagesearch.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charmingwong.cwimage.imagesearch.ImageFinder;
import com.charmingwong.cwimage.imagesearch.QImage;

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
 * Created by CharmingWong on 2017/5/15.
 */

public class QHConverterFactory extends Converter.Factory {


    private static final String TAG = "QHConverterFactory";

    private QHConverterFactory() {
    }

    public static QHConverterFactory create() {
        return new QHConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new QHConverter();
    }

    private class QHConverter implements Converter<ResponseBody, List<QImage>> {

        @Override
        public List<QImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                JSONObject jsonObject = new JSONObject(value.string());
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                Log.i(TAG, "convert: " + jsonArray.length());
                if (jsonArray.length() == 0) {
                    return null;
                }
                List<QImage> QImages = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    QImages.add(new QImage(jsonArray.getJSONObject(i), ImageFinder.QH360));
                }
                return QImages;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "图片搜索 json 解析出错");
            }
            return null;
        }
    }
}
