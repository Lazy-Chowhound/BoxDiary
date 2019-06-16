package example.diary.ui.login;

import example.diary.base.BaseActivity;
import example.diary.base.BaseApplication;
import example.diary.base.BasePresenter;
import example.diary.constant.Constant;
import example.diary.util.EncryptUtils;
import example.diary.util.SPUtils;



public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private SPUtils sp;

    protected LoginPresenter(BaseActivity activity) {
        super(activity);
        sp = new SPUtils(activity, Constant.SP_ACCOUNT);
    }

    @Override
    public void login(String account, String password) {
        if (sp.getString(Constant.SP_ACCOUNT_NUMBER) != null
                && sp.getString(Constant.SP_ACCOUNT_PASSWORD) != null
                && sp.getString(Constant.SP_ACCOUNT_NUMBER).equals(account)
                && sp.getString(Constant.SP_ACCOUNT_PASSWORD).equals(password)) {
            BaseApplication.account = account;
            mView.gotoMain();
        } else {
            mView.loginFailed();
        }
    }

    @Override
    public void addAccount(String account, String password) {
        String encryptPwd = EncryptUtils.encryptMD5ToString(password);
        sp.putString(Constant.SP_ACCOUNT_NUMBER, account);
        sp.putString(Constant.SP_ACCOUNT_PASSWORD, encryptPwd);
        login(account, encryptPwd);
    }
}
