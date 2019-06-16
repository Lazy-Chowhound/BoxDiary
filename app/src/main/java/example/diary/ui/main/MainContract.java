package example.diary.ui.main;

import java.util.List;

import example.diary.base.IPresenter;
import example.diary.base.IView;
import example.diary.db.entity.ItemEntity;



public interface MainContract {

    interface View extends IView {
        void showItemList(List<ItemEntity> list);
    }

    interface Presenter extends IPresenter<View> {
        void getItemList(String account);
        void addItem(int type,String name);
    }
}
