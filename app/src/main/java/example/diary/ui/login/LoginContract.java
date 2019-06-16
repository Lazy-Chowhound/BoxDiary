package example.diary.ui.login;

import example.diary.base.IPresenter;
import example.diary.base.IView;



public interface LoginContract {

    interface View extends IView {

        void gotoMain();

        void loginFailed();

    }

    interface Presenter extends IPresenter<View> {

        void login(String account, String password);

        void addAccount(String account, String password);

    }
}
