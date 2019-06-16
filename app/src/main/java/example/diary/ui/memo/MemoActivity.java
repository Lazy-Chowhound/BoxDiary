package example.diary.ui.memo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import example.diary.R;
import example.diary.base.BaseActivity;
import example.diary.db.entity.MemoEntity;
import example.diary.util.TimeUtils;



public class MemoActivity extends BaseActivity<MemoPresenter> implements MemoContract.View {

    @BindView(R.id.tv_memo_title)
    TextView tvTitle;
    @BindView(R.id.iv_memo_edit)
    ImageView ivEdit;
    @BindView(R.id.tv_memo_save)
    TextView tvSave;
    @BindView(R.id.et_memo_new)
    EditText etNew;
    @BindView(R.id.rv_memo)
    RecyclerView rvMemo;
    @BindView(R.id.alarmView)
    ImageView alarm;

    private MemoAdapter mAdapter;

    private boolean isEdit = false;

    private Long memoId;
    private List<MemoEntity> mList;

    private int chooseYear = 0;
    private int chooseMonth = 0;
    private int chooseDay = 0;
    private int chooseHour = 0;
    private int chooseMinute = 0;
    private Calendar nowCalendar;
    private Calendar chooseCalendar;

    private static int count = 0;

    @Override
    protected MemoPresenter createPresenter() {
        return new MemoPresenter(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_memo;
    }

    @Override
    protected void initView() {
        RxView.clicks(tvSave)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    if (TextUtils.isEmpty(etNew.getText().toString())) {
                        showToast("请输入有效字符");
                        return;
                    }
                    mPresenter.addItem(memoId, etNew.getText().toString());
                    etNew.setText("");
                    if(chooseYear != 0) {
                        startRemind();
                        count++;
                    }
                });
        rvMemo.setLayoutManager(new LinearLayoutManager(this));
        RxView.clicks(ivEdit)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    if (isEdit) {
                        showList();
                    } else {
                        showListEdit();
                    }
                    isEdit = !isEdit;
                });

        RxView.clicks(alarm)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    chooseDate();
                });
    }

    @Override
    protected void initData() {
        tvTitle.setText(getIntent().getStringExtra("title"));
        memoId = getIntent().getLongExtra("id", 0);
        mPresenter.getItemList(memoId);
    }

    public static void startMemoActivity(Context context, Long id, String title) {
        Intent intent = new Intent(context, MemoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void showList(List<MemoEntity> list) {
        mList = list;
        showList();
    }

    private void showList() {
        findViewById(R.id.layout_memo_add).setVisibility(View.VISIBLE);
        if (mList != null && mList.size() > 0) {
            mAdapter = new MemoAdapter(this, mList, false, mPresenter);
            rvMemo.setAdapter(mAdapter);
        }
    }

    private void showListEdit() {
        findViewById(R.id.layout_memo_add).setVisibility(View.GONE);
        if (mList != null && mList.size() > 0) {
            mAdapter = new MemoAdapter(this, mList, true, mPresenter);
            rvMemo.setAdapter(mAdapter);
        }
    }

    private void chooseDate() {
        nowCalendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    chooseYear = year;
                    chooseMonth = monthOfYear;
                    chooseDay = dayOfMonth;
                    chooseTime();
                }
                , nowCalendar.get(Calendar.YEAR), nowCalendar.get(Calendar.MONTH), nowCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void chooseTime() {
        new TimePickerDialog(this,
                (timePicker, hour, minute) -> {
                    chooseHour = hour;
                    chooseMinute = minute;
                    chooseCalendar = new GregorianCalendar(chooseYear, chooseMonth, chooseDay, chooseHour, chooseMinute);

                }, nowCalendar.get(Calendar.HOUR_OF_DAY), nowCalendar.get(Calendar.MINUTE), true).show();
    }

    private void startRemind() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //设置提醒时间
        mCalendar.set(Calendar.YEAR, chooseYear);
        mCalendar.set(Calendar.MONTH, chooseMonth);
        mCalendar.set(Calendar.DAY_OF_MONTH, chooseDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, chooseHour);
        mCalendar.set(Calendar.MINUTE, chooseMinute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        showToast("将在"+TimeUtils.date2String(chooseCalendar.getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm"))+"提醒");
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), count, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
    }

    public void stopRemind() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        Toast.makeText(this, "关闭了提醒", Toast.LENGTH_SHORT).show();
    }

}
