package com.ssh.shuoshi.ui;

import android.content.Context;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * created by hwt on 2020/12/8
 */
public class BaseMainFragment extends SupportFragment {

    protected OnBackToFirstListener _mBackToFirstListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            _mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mBackToFirstListener = null;
    }

    /**
     * 处理回退事件
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            _mActivity.finish();
//            if (this instanceof FirstFragment) {
//                _mActivity.finish();
//            } else {
//                _mBackToFirstListener.onBackToFirstFragment();
//            }
        }
        return true;
    }

    public interface OnBackToFirstListener {
        void onBackToFirstFragment();
    }
}
