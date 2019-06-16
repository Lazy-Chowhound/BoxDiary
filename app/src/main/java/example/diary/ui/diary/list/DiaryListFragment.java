package example.diary.ui.diary.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.List;

import butterknife.BindView;
import example.diary.R;
import example.diary.base.BaseActivity;
import example.diary.base.BaseFragment;
import example.diary.db.entity.DiaryEntity;
import example.diary.event.DiaryEditDoneEvent;
import example.diary.util.RxBus;



public class DiaryListFragment extends BaseFragment<DiaryListPresenter> implements DiaryListContract.View {

    @BindView(R.id.rv_diary_list)
    XRecyclerView rvList;
    @BindView(R.id.tv_diary_list_count)
    TextView tvCount;

    private DiaryListAdapter mAdapter;
    //日记ID
    private Long diaryId;
    //当前日记数量
    private int page = 0;

    public static DiaryListFragment newInstance(Long id) {
        DiaryListFragment fragment = new DiaryListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected DiaryListPresenter createPresenter() {
        return new DiaryListPresenter((BaseActivity) getActivity());
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_diary_list;
    }
    //重写初始化视图方法
    @Override
    protected void initView() {
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RxBus.getInstance().onEvent(DiaryEditDoneEvent.class)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(event -> {
                    initData();
                });
        rvList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getDiaryList(diaryId, page);
            }
        });
    }

    @Override
    protected void initData() {
        diaryId = getArguments().getLong("id", 0);
        mPresenter.getDiaryList(diaryId);
    }
    //显示共有几篇日记
    @Override
    public void showDiaryList(List<DiaryEntity> list) {
        rvList.refreshComplete();
        if (list != null) {
            tvCount.setText("共  "+list.size() + "  篇日记");
            mAdapter = new DiaryListAdapter(getActivity(), list, mPresenter);
            //在rv_diary_list下显示日记
            rvList.setAdapter(mAdapter);
        }
    }

    @Override
    public void showMoreDiaryList(List<DiaryEntity> list) {
        rvList.loadMoreComplete();
        mAdapter.addData(list);
    }

}
