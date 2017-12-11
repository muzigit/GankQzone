package lifeng.gankqzone.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseFragment;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public class AllGankFragment extends BaseFragment {
    @Override
    protected void initPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_gank,container,false);
    }
}
