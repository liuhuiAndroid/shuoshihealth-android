package com.ssh.shuoshi.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.ssh.shuoshi.greendao.local.DaoMaster;
import com.ssh.shuoshi.greendao.local.ServiceTicketDao;

/**
 * created by hwt on 2020/12/8
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, ServiceTicketDao.class);

    }
}
