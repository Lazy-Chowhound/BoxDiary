package example.diary.ui.diary.calendar;

import java.util.Calendar;
import java.util.List;

import example.diary.base.BaseActivity;
import example.diary.base.BasePresenter;
import example.diary.db.DbManager;
import example.diary.db.entity.DiaryEntity;

//Calendar Presenter
public class DiaryCalendarPresenter extends BasePresenter<DiaryCalendarContract.View> implements DiaryCalendarContract.Presenter {

    private Calendar mCalendar;

    protected DiaryCalendarPresenter(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void getDiaryList(Long diaryId, Calendar calendar) {
        mCalendar = calendar;
        List<DiaryEntity> list = DbManager.queryDiaryListByDate(diaryId, calendar);
        mView.showDiaryList(list);
    }

    @Override
    public void deleteDary(Long id, Long diaryId) {
        DbManager.deleteDiaryById(id);
        editItemEntityCount(diaryId);
        getDiaryList(diaryId, mCalendar);
    }

    private void editItemEntityCount(Long diaryId) {
        DbManager.updateItemCountByItemId(diaryId);
    }
}
