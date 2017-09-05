package com.charmingwong.cwimage;

import com.charmingwong.cwimage.imagechannel.ChannelImage;
import com.charmingwong.cwimage.imagesearch.QImage;
import com.charmingwong.cwimage.search.ImageSearchSuggestion;
import com.charmingwong.cwimage.search.model.HotSearch;
import com.charmingwong.cwimage.searchbyimage.SoImage;
import com.charmingwong.cwimage.wallpaper.WallpaperCover;
import com.charmingwong.cwimage.wallpaperdetails.Wallpaper;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public interface JsonRequestService {

    @GET("search/acjson")
    Call<List<QImage>> getBaiduImages(
            @Query("word") String word,
            @Query("tn") String tn,
            @Query("ipn") String ipn,
            @Query("rn") int rn,
            @Query("pn") int pn,
            @Query("width") String width,
            @Query("height") String height
    );

    @GET("j")
    Call<List<QImage>> getQH360Images(
            @Query("q") String query,
            @Query("sn") int sn,
            @Query("pn") int pn,
            @Query("width") String width,
            @Query("height") String height
    );

    @GET("pics")
    Call<List<QImage>> getSogouImages(
            @Query("query") String query,
            @Query("reqType") String reqType,
            @Query("start") int start,
            @Query("cwidth") String width,
            @Query("cheight") String height,
            @Query("dm") int dm
    );

    @GET("getpic")
    Call<List<QImage>> getChinasoImages(
            @Query("q") String query,
            @Query("st") int start,
            @Query("rn") int rn);

    @GET
    Call<List<QImage>> getBingImages(
            @Url String url
    );

    @Headers("user-agent:Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19")
    @GET("search")
    Call<List<QImage>> getGoogleImage(
            @Query("hl") String hl,
            @Query("asearch") String asearch,
            @Query("tbm") String tbn,
            @Query("q") String query,
            @Query("start") int start,
            @Query("ijn") int ijn,
            @Query("tbs") String tbs
    );

    @GET("zj")
    Call<List<ChannelImage>> getChannelImages(
            @Query("ch") String channel,
            @Query("sn") int sn,
            @Query("pn") int pn,
            @Query("t1") int t1,
            @Query("t2") int t2,
            @Query("listtype") String listType,
            @Query("temp") int temp
    );

    @GET("suggest/word")
    Call<List<ImageSearchSuggestion>> getSearchSuggestions(
            @Query("callback") String callback,
            @Query("encodein") String encodeIn,
            @Query("encodeout") String encodeOut,
            @Query("word") String word
    );

    @Headers("Cache-Control:max-age=86400")
    @GET("/")
    Call<List<HotSearch>> getHotSearches();

    @POST("n/image")
    Call<String> postImage(
            @Body RequestBody image,
            @Query("fr") String from,
            @Query("needJson") boolean needJson
    );

    @GET("n/similar")
    Call<List<SoImage>> getSoImages(
            @Query("queryImageUrl") String url,
            @Query("pn") int pn,
            @Query("rn") int rn);

    @GET()
    Call<List<WallpaperCover>> getWallPaperCover(
            @Url String url
    );

    @GET
    Call<Wallpaper> getWallPaper(
            @Url String url
    );

    @GET
    Call<List<String>> getWallpaperStart(
            @Url String url
    );
}
