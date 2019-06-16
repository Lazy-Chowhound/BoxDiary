package example.diary.ui.diary.edit;

import java.util.List;

import example.diary.base.IPresenter;
import example.diary.base.IView;
import example.diary.db.entity.DiaryEntity;


public interface DiaryEditContract {

    interface View extends IView {
        void saveDone();
    }

    interface Presenter extends IPresenter<View> {
        void save(Long diaryId, DiaryEntity entity, List<String> tags);
    }
}
