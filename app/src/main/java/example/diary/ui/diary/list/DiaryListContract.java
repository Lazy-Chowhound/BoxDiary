package example.diary.ui.diary.list;

import java.util.List;

import example.diary.base.IPresenter;
import example.diary.base.IView;
import example.diary.db.entity.DiaryEntity;



public interface DiaryListContract {

    interface View extends IView {

        void showDiaryList(List<DiaryEntity> list);

        void showMoreDiaryList(List<DiaryEntity> list);

    }

    interface Presenter extends IPresenter<View> {

        void getDiaryList(Long diaryId);

        void getDiaryList(Long diaryId, int page);

        void deleteDary(Long id, Long diaryId);
    }
}
