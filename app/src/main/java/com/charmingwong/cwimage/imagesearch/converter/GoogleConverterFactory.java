package com.charmingwong.cwimage.imagesearch.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charmingwong.cwimage.imagesearch.ImageFinder;
import com.charmingwong.cwimage.imagesearch.QImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/1.
 */

public class GoogleConverterFactory extends Converter.Factory {

    private static final String TAG = "GoogleConverterFactory";

    private GoogleConverterFactory() {
    }

    public static GoogleConverterFactory create() {
        return new GoogleConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GoogleConverter();
    }

    private class GoogleConverter implements Converter<ResponseBody, List<QImage>> {

        @Override
        public List<QImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                JSONArray jsonArray = new JSONArray(result);
                String data = ((JSONArray) jsonArray.get(1)).getString(1);
                Document document = Jsoup.parse(data);
                Elements elements = document.getElementsByAttributeValue("class", "rg_di rg_bx rg_el ivg-i");

                List<QImage> qImages = new ArrayList<>(elements.size());
                JSONObject image = new JSONObject();
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    JSONObject urlJson = new JSONObject(element.getElementsByAttributeValue("class", "rg_meta notranslate").get(0).text());
                    image.put("url", urlJson.getString("ou"));
                    image.put("width", urlJson.getInt("ow"));
                    image.put("height", urlJson.getInt("oh"));
                    image.put("thumbUrl", element.getElementsByTag("img").get(0).attr("data-src"));
                    qImages.add(new QImage(image, ImageFinder.BING));
                    image.put("imageSize", "");
                    Log.i(TAG, "convert: " + image);
                }
                return qImages;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "图片搜索 json 解析出错");
            }
            return null;
        }
    }
}
