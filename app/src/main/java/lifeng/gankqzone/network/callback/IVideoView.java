package lifeng.gankqzone.network.callback;

import java.util.List;

import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.VideoDetailRes;

/**
 * Created by lifeng on 2017/11/15.
 *
 * @description
 */
public interface IVideoView<T> extends IView<T> {

    void getGankVideo(AndroidListRes videoList);

    void getVideoImage(List<VideoDetailRes> videoDetailRes);
}
