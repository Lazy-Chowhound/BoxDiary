package example.diary.ui.diary.edit;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import example.diary.base.BaseActivity;
import example.diary.base.BasePresenter;
import example.diary.db.DbManager;
import example.diary.db.entity.DiaryEntity;
import example.diary.db.entity.TagEntity;


public class DiaryEditPresenter extends BasePresenter<DiaryEditContract.View> implements DiaryEditContract.Presenter {

    protected DiaryEditPresenter(BaseActivity activity) {
        super(activity);
    }


    @Override
    public void save(Long diaryId, DiaryEntity entity, List<String> tags) {
        DbManager.addDiary(entity);
        DbManager.updateItemCountByItemId(diaryId);
        mView.saveDone();
        Observable.from(tags)
                .subscribe(s -> {
                    TagEntity tag = new TagEntity();
                    tag.setIdItem(diaryId);
                    tag.setIdDiary(entity.getId());
                    tag.setTag(s);
                    DbManager.addTag(tag);
                });
    }

}
