package example.diary.ui.memo;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import example.diary.base.BaseActivity;
import example.diary.base.BaseApplication;
import example.diary.base.BasePresenter;
import example.diary.db.dao.DaoSession;
import example.diary.db.dao.ItemEntityDao;
import example.diary.db.dao.MemoEntityDao;
import example.diary.db.entity.ItemEntity;
import example.diary.db.entity.MemoEntity;
import example.diary.event.MainRefreshEvent;
import example.diary.util.RxBus;



public class MemoPresenter extends BasePresenter<MemoContract.View> implements MemoContract.Presenter {

    private MemoEntityDao entityDao;
    private ItemEntityDao entityItemDao;

    protected MemoPresenter(BaseActivity activity) {
        super(activity);
        DaoSession daoSession = ((BaseApplication) activity.getApplication()).getDaoSession();
        entityDao = daoSession.getMemoEntityDao();
        entityItemDao = daoSession.getItemEntityDao();
    }

    @Override
    public void addItem(Long memoId, String string) {
        MemoEntity entity = new MemoEntity();
        entity.setMemoId(memoId);
        entity.setMemo(string);
        entityDao.insert(entity);
        //找到ItemEntity 将count++
        editItemEntityCount(memoId, true);
        getItemList(memoId);
    }

    @Override
    public void getItemList(Long memoId) {
        Query query = entityDao.queryBuilder()
                .where(MemoEntityDao.Properties.MemoId.eq(memoId))
                .orderDesc(MemoEntityDao.Properties.Id)
                .build();
        List<MemoEntity> list = query.list();
        mView.showList(list);
    }

    @Override
    public void deleteItem(Long id, Long memoId) {
        entityDao.deleteByKey(id);
        editItemEntityCount(memoId, false);
    }

    @Override
    public void setLine(boolean addLine, MemoEntity entity) {
        entity.setLine(addLine);
        entityDao.update(entity);
    }

    private void editItemEntityCount(Long memoId, boolean plus) {
        ItemEntity entity = entityItemDao.load(memoId);
        int count = entity.getItemCount();
        if (plus) {
            count++;
        } else {
            count--;
        }
        entity.setItemCount(count);
        entityItemDao.update(entity);
        RxBus.getInstance()
                .post(new MainRefreshEvent());
    }
}
