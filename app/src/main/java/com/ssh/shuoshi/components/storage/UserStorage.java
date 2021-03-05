package com.ssh.shuoshi.components.storage;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.User;
import com.ssh.shuoshi.util.SPUtil;
import com.tencent.mmkv.MMKV;

/**
 * created by hwt on 2020/12/8
 */
public class UserStorage {

    private Context mContext;
    private SPUtil mSPUtil;
    private int mState;

    public UserStorage(Context context, SPUtil spUtil) {
        this.mContext = context;
        this.mSPUtil = spUtil;
    }

    private String token;
    private User user;
    private HisDoctorBean doctorInfo;

    public String getToken() {
//        if(token!=null && !TextUtils.isEmpty(token)) {
//            return token;
//        }else{
//            token = mSPUtil.getTOKNE();
//            return mSPUtil.getTOKNE();
//        }
        return mSPUtil.getTOKNE();
    }

    public void setToken(String token) {
        this.token = token;
        mSPUtil.setTOKNE(token);
    }

    public void setApprovalState(int state) {
        this.mState = state;
        mSPUtil.setApprovalState(state);
    }

    public int getApprovalState() {
        return mSPUtil.getApprovalState();
    }

    public HisDoctorBean getDoctorInfo() {
        String userString = mSPUtil.getDoctorInfo();
        this.doctorInfo = new Gson().fromJson(userString, HisDoctorBean.class);
        return this.doctorInfo;
    }

    public void setDoctorInfo(HisDoctorBean doctorInfo) {
        String doctorInfoJson = new Gson().toJson(doctorInfo);
        mSPUtil.setDoctorInfo(doctorInfoJson);
        MMKV.defaultMMKV().putString("IM__MY_USER_NAME",doctorInfo.getName());
    }


    public User getUser() {
        if (user != null) {
            return user;
        } else {
            String userString = mSPUtil.getUSER();
            this.user = new Gson().fromJson(userString, User.class);
            return this.user;
        }
    }

    public void setUser(User user) {
        this.user = user;
        String userJson = new Gson().toJson(user);
        mSPUtil.setUSER(userJson);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(token) || (!TextUtils.isEmpty(mSPUtil.getTOKNE()));
    }

    public void logout() {
        user = null;
        token = "";
        mSPUtil.setTOKNE("");
        mSPUtil.setDoctorInfo("");
        mSPUtil.setIS_LOGIN(0);
    }

}
