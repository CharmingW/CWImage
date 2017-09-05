package com.charmingwong.cwimage.localview;

import com.charmingwong.cwimage.BasePresenter;
import com.charmingwong.cwimage.BaseView;

/**
 * Created by CharmingWong on 2017/5/12.
 */

public interface LocalViewContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
