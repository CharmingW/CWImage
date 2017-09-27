package com.charmingwong.cwimage.localview;

import com.charmingwong.cwimage.base.BasePresenter;
import com.charmingwong.cwimage.base.BaseView;

/**
 * Created by CharmingWong on 2017/5/12.
 */

public interface LocalViewContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
