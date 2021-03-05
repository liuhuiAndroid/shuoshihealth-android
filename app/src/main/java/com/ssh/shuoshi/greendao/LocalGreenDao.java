package com.ssh.shuoshi.greendao;

import android.content.Context;

import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.greendao.local.DaoMaster;
import com.ssh.shuoshi.greendao.local.DaoSession;

/**
 * created by hwt on 2020/12/8
 */
public class LocalGreenDao {
    private static LocalGreenDao instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static final String DATABASE_NAME = "userdata.db";
    private Context mContext;

    public LocalGreenDao(Context mContext) {
        getDaoSession();
        this.mContext = mContext;
    }

    public static LocalGreenDao getInstance() {
        if (instance == null) {
            instance = new LocalGreenDao(MyApplication.getContext());
        }
        return instance;
    }


    private DaoMaster obtainMaster(Context context, String dbName) {
        //改
        return new DaoMaster(new MySQLiteOpenHelper(context, dbName, null).getWritableDatabase());
    }

    private DaoMaster getDaoMaster(Context context, String dbName) {
        if (dbName == null)
            return null;
        if (daoMaster == null) {
            daoMaster = obtainMaster(context, dbName);
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    public DaoSession getDaoSession(String dbName) {

        if (daoSession == null) {
            daoSession = getDaoMaster(MyApplication.getContext(), dbName).newSession();
        }
        return daoSession;
    }

    /**
     * 默认操作localdata数据库
     */
    public DaoSession getDaoSession() {

        if (daoSession == null) {
            daoSession = getDaoMaster(MyApplication.getContext(), DATABASE_NAME).newSession();
        }
        return daoSession;
    }


//    public void saveUser(DoctorInfoBean bean) {
//        User user = new User();
//        user.setId(bean.getId());
//        daoSession.getUserDao().insertOrReplace(user);
//    }
}
