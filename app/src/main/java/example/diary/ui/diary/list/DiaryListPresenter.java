package example.diary.ui.diary.list;

import java.util.List;

import example.diary.base.BaseActivity;
import example.diary.base.BasePresenter;
import example.diary.db.DbManager;
import example.diary.db.entity.DiaryEntity;



public class DiaryListPresenter extends BasePresenter<DiaryListContract.View> implements DiaryListContract.Presenter {

    protected DiaryListPresenter(BaseActivity activity) {
        super(activity);
    }

    //获取diary列表
    @Override
    public void getDiaryList(Long diaryId) {
        List<DiaryEntity> list = DbManager.queryDiaryListByItemId(diaryId);
        mView.showDiaryList(list);
    }

    @Override
    public void getDiaryList(Long diaryId, int page) {
        List<DiaryEntity> list = DbManager.queryDiaryListByItemIdPage(diaryId, page);
        mView.showMoreDiaryList(list);
    }
    //删除日记
    @Override
    public void deleteDary(Long id, Long diaryId) {
        DbManager.deleteDiaryById(id);
        editItemEntityCount(diaryId);
        getDiaryList(diaryId);
    }
    private void editItemEntityCount(Long diaryId) {
        DbManager.updateItemCountByItemId(diaryId);
    }
}
