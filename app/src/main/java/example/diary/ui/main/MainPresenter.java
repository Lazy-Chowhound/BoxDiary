package example.diary.ui.main;

import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

import example.diary.base.BaseActivity;
import example.diary.base.BaseApplication;
import example.diary.base.BasePresenter;
import example.diary.db.dao.DaoSession;
import example.diary.db.dao.ItemEntityDao;
import example.diary.db.entity.ItemEntity;



public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private ItemEntityDao entityDao;

    public MainPresenter(BaseActivity activity) {
        super(activity);
        DaoSession daoSession = ((BaseApplication) activity.getApplication()).getDaoSession();
        entityDao = daoSession.getItemEntityDao();
    }

    //显示listview的项
    @Override
    public void getItemList(String account) {
        Query query = entityDao.queryBuilder()
                .where(ItemEntityDao.Properties.Account.eq(account))
                .build();
        List<ItemEntity> list = query.list();
        mView.showItemList(list);
    }

    @Override
    public void addItem(int type, String name) {
        ItemEntity entityContact = new ItemEntity();
        entityContact.setAccount(BaseApplication.account);
        entityContact.setDate(new Date());
        entityContact.setItemTitle(name);
        entityContact.setItemType(type);
        entityDao.save(entityContact);
        getItemList(BaseApplication.account);
    }
}
