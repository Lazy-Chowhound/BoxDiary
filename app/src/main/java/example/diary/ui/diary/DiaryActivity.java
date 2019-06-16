package example.diary.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import example.diary.R;
import example.diary.base.BaseActivity;
import example.diary.base.IPresenter;
import example.diary.event.DiaryEditDoneEvent;
import example.diary.ui.diary.calendar.DiaryCalendarFragment;
import example.diary.ui.diary.edit.DiaryEditFragment;
import example.diary.ui.diary.list.DiaryListFragment;
import example.diary.util.RxBus;



public class DiaryActivity extends BaseActivity {

    @BindView(R.id.tab_diary)
    SegmentTabLayout mTabLayout;
    @BindView(R.id.vp_diary)
    ViewPager mViewPager;

    public static int index=1;

    private Long diaryId;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"プロジェクト", "カレンダー", "日記"};

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_diary;
    }

    @Override
    protected void initView() {
        //顶部选择框填数据
        diaryId = getIntent().getLongExtra("id", 0);
        mTabLayout.setTabData(mTitles);
        //加 项目、日历、日记    碎片
        mFragments.add(DiaryListFragment.newInstance(diaryId));
        mFragments.add(DiaryCalendarFragment.newInstance(diaryId));
        mFragments.add(DiaryEditFragment.newInstance(diaryId));

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        //选择对应的页面
        RxViewPager.pageSelections(mViewPager)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(integer -> {
                    mTabLayout.setCurrentTab(integer);
                });
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        RxBus.getInstance().onEvent(DiaryEditDoneEvent.class)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(event -> {
                    mViewPager.setCurrentItem(0);

                });
    }

    @Override
    protected void initData() {

    }

    public static void startDiaryActivity(Context context, Long id) {
        Intent intent = new Intent(context, DiaryActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}
