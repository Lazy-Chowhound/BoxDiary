package example.diary.ui.diary.calendar;

import java.util.Calendar;
import java.util.List;

import example.diary.base.IPresenter;
import example.diary.base.IView;
import example.diary.db.entity.DiaryEntity;



public interface DiaryCalendarContract {

    interface View extends IView {
        void showDiaryList(List<DiaryEntity> list);
    }

    interface Presenter extends IPresenter<View> {

        void getDiaryList(Long diaryId, Calendar calendar);

        void deleteDary(Long id, Long diaryId);
    }
}
