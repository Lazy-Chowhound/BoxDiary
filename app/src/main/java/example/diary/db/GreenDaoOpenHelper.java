package example.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import org.greenrobot.greendao.database.Database;

import example.diary.db.dao.DaoMaster;
import example.diary.db.dao.DiaryEntityDao;
import example.diary.db.dao.ItemEntityDao;
import example.diary.db.dao.MemoEntityDao;
import example.diary.db.dao.TagEntityDao;
import example.diary.db.entity.DiaryEntity;



public class GreenDaoOpenHelper extends DaoMaster.OpenHelper {

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.getInstance().migrate(db,
                ItemEntityDao.class,
                MemoEntityDao.class,
                DiaryEntityDao.class);
    }
}
