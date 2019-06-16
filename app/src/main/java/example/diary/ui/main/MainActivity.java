package example.diary.ui.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import example.diary.R;
import example.diary.base.BaseActivity;
import example.diary.base.BaseApplication;
import example.diary.constant.Constant;
import example.diary.db.entity.ItemEntity;
import example.diary.event.MainRefreshEvent;
import example.diary.util.RxBus;
import example.diary.util.SPUtils;
import example.diary.widget.MyAlertDialog;
import example.diary.widget.MyEditDialog;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.iv_main_plus)
    ImageView ivPlus;

    @BindView(R.id.rv_main)
    RecyclerView rvItem;
    @BindView(R.id.tv_main_name)
    TextView tvName;

    private MainItemAdapter itemAdapter;
    private MyEditDialog dialogEdit;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //显示账号名，默认为“三叶”
        SPUtils sp = new SPUtils(this, Constant.SP_ACCOUNT);
        String name = sp.getString(Constant.SP_ACCOUNT_NAME, "用户名");
        tvName.setText(name);
        //弹出下拉菜单，新建联系人，新建日记，新建备忘录
        RxView.clicks(ivPlus)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    showPlusDrop();
                });

        rvItem.setLayoutManager(new LinearLayoutManager(this));
        RxBus.getInstance().onEvent(MainRefreshEvent.class)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(mainActivity -> {
                    itemAdapter.notifyDataSetChanged();
                });
        //改账号名
        RxView.clicks(tvName)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aVoid -> {
                    dialogEdit = new MyEditDialog.Builder()
                            .setTitle("你的名字？")
                            .setListener(view -> {
                                sp.putString(Constant.SP_ACCOUNT_NAME, dialogEdit.getEditTextString());
                                dialogEdit.dismiss();
                                tvName.setText(dialogEdit.getEditTextString());
                            })
                            .build();
                    //？？？
                    dialogEdit.show(getSupportFragmentManager(), "");
                });
    }
    //下拉菜单的实现（加item）
    private void showPlusDrop() {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, ivPlus);
        droppyBuilder.addMenuItem(new DroppyMenuItem("新建日记", R.drawable.ic_plus_diary));
        droppyBuilder.addMenuItem(new DroppyMenuItem("新增备忘录", R.drawable.ic_plus_alert));
        droppyBuilder.setOnClick((v, id) -> showAddDialog(id));
        DroppyMenuPopup droppyMenu = droppyBuilder.build();
        droppyMenu.show();
    }

    @Override
    protected void initData() {
        mPresenter.getItemList(BaseApplication.account);
    }

    //显示listview的项
    @Override
    public void showItemList(List<ItemEntity> list) {
        if (list != null && list.size() > 0) {
            itemAdapter = new MainItemAdapter(this, list);
            rvItem.setAdapter(itemAdapter);
        } else {
            showEmptyDialog();
        }
    }

    //输入条目名的dialog
    private void showAddDialog(int type) {
        dialogEdit = new MyEditDialog.Builder()
                .setTitle("输入条目名称")
                .setListener(view -> {
                    mPresenter.addItem(type, dialogEdit.getEditTextString());
                    dialogEdit.dismiss();
                })
                .build();
        dialogEdit.show(getSupportFragmentManager(), "");
    }

    //没有数据时，显示提示信息
    private void showEmptyDialog() {
        MyAlertDialog dialog = new MyAlertDialog.Builder()
                .setTitle("没有内容")
                .setContent("日记本是空的╮(╯▽╰)╭")
                .setAffirm("新建一篇日记")
                .setListener(view -> showAddDialog(0))
                .build();
        dialog.show(getSupportFragmentManager(), "");
    }

    private boolean isBack = false;


    //两次点击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isBack) {
                showToast("再次点击退出");
                isBack = true;
                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribe(aLong -> {
                            isBack = false;
                        });
                return true;
            } else {
                MainActivity.this.finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
