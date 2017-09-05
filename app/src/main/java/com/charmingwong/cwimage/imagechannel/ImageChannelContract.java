package com.charmingwong.cwimage.imagechannel;

import com.charmingwong.cwimage.BasePresenter;
import com.charmingwong.cwimage.BaseView;

import java.util.List;

/**
 * Created by CharmingWong on 2017/5/14.
 */

public interface ImageChannelContract {

    interface View extends BaseView<Presenter> {

        void showChannelImages(int channel, List<ChannelImage> channelImages);

        void showRefreshedChannelImages(int channel, List<ChannelImage> channelImages);

        void showChannelImagesNull(int channel);

        void showChannelImagesFailed(int channel);

        int getCurrentChannelImageLastIndex();

    }

    interface Presenter extends BasePresenter {

        void query(int channel, int tag);

        void refresh(int channel, int tag);

        void downloadWelcomeImage();

    }
}
