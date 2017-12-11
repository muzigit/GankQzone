package lifeng.gankqzone.network.callback;

import java.util.List;

import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public interface IFengHVideoView<T> extends IView<T> {

    void getVideoChannelList(List<VideoChannelRes> videoChannelRes);

    void getVidelDetailList(List<VideoDetailRes> videoDetailRes);
}
