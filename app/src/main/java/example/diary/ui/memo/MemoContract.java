package example.diary.ui.memo;

import java.util.List;

import example.diary.base.IPresenter;
import example.diary.base.IView;
import example.diary.db.entity.MemoEntity;



public interface MemoContract {

    interface View extends IView {
        void showList(List<MemoEntity> list);
    }

    interface Presenter extends IPresenter<View> {

        void addItem(Long memoId, String string);

        void getItemList(Long memoId);

        void deleteItem(Long id, Long memoId);

        void setLine(boolean addLine, MemoEntity entity);
    }
}
