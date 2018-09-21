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
 * Created by CharmingWong on 2017/5/31.
 */

public class ChinasoConverterFactory extends Converter.Factory {


    private static final String TAG = "ChinasoConverterFactory";

    private ChinasoConverterFactory() {
    }

    public static ChinasoConverterFactory create() {
        return new ChinasoConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ChinasoConverter();
    }

    private class ChinasoConverter implements Converter<ResponseBody, List<QImage>> {

        @Override
        public List<QImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                Log.i(TAG, "convert: " + result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("arrResults");
                Log.i(TAG, "convert: " + jsonArray.length());
                if (jsonArray.length() == 0) {
                    return null;
                }
                List<QImage> QImages = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    QImages.add(new QImage(jsonArray.getJSONObject(i), ImageFinder.CHINASO));
                }
                return QImages;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "图片搜索 json 解析出错");
            }
            return new ArrayList<>(0);
        }
    }
}
