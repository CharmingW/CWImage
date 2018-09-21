package com.charmingwong.cwimage.imagechannel;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.charmingwong.cwimage.R;

/**
 * Created by CharmingWong on 2017/7/4.
 */

public class SplashFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
    ImageView welcomeImage = (ImageView) rootView.findViewById(R.id.welcomeImage);
    SharedPreferences sharedPreferences = getActivity()
        .getSharedPreferences("welcome_image_pref", Context.MODE_PRIVATE);
    welcomeImage.setImageURI(Uri.parse(sharedPreferences.getString("url", "")));
    return rootView;
  }
}
