package com.charmingwong.cwimage.imagesearch.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charmingwong.cwimage.imagesearch.ImageFinder;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.imagesearch.api.BingApi;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/31.
 */

public class BingConverterFactory extends Converter.Factory {


    private static final String TAG = "BingConverterFactory";

    private BingConverterFactory() {
    }

    public static BingConverterFactory create() {
        return new BingConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new BingConverter();
    }

    private class BingConverter implements Converter<ResponseBody, List<QImage>> {

        @Override
        public List<QImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                Document document = Jsoup.parse(result);

                BingApi.NEXT_URL = document.getElementsByAttribute("data-nextUrl").get(0).attr("data-nextUrl");
                Log.i(TAG, "convert: " + BingApi.NEXT_URL);

                Elements elements = document.getElementsByAttributeValue("class", "iuscp varh");
                List<QImage> qImages = new ArrayList<>(elements.size());
                JSONObject image = new JSONObject();
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    JSONObject urlJson = new JSONObject(element.getElementsByAttribute("m").get(0).attr("m"));
                    image.put("url", urlJson.getString("murl"));
                    image.put("thumbUrl", element.getElementsByTag("img").get(0).attr("src"));
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(element.getElementsByAttribute("tabindex").get(0).text());
                    if (matcher.find()) {
                        image.put("width", matcher.group());
                    }
                    if (matcher.find()) {
                        image.put("height", matcher.group());
                    }
//                    Log.i(TAG, "convert: " + image.toString());
                    qImages.add(new QImage(image, ImageFinder.BING));
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
