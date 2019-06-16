package example.diary.ui.splash;

import android.content.Intent;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;
import example.diary.R;
import example.diary.base.BaseActivity;
import example.diary.base.BaseApplication;
import example.diary.base.IPresenter;
import example.diary.constant.Constant;
import example.diary.ui.login.LoginActivity;
import example.diary.ui.main.MainActivity;
import example.diary.util.SPUtils;
import example.diary.util.TimeUtils;



public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_splash_date)
    TextView tvData;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        tvData.setText(TimeUtils.getCurTimeString(sdf));
    }

    @Override
    protected void initData() {
        Intent intent = new Intent();
        SPUtils sp = new SPUtils(this, Constant.SP_ACCOUNT);
        if (sp.getString(Constant.SP_ACCOUNT_NUMBER) != null) {
            intent.setClass(this, MainActivity.class);
            BaseApplication.account = sp.getString(Constant.SP_ACCOUNT_NUMBER);
        } else {
            intent.setClass(this, LoginActivity.class);
        }
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aLong -> {
                    this.finish();
                    startActivity(intent);
                });
    }
}
