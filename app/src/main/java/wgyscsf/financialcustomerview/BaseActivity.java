package wgyscsf.financialcustomerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ============================================================
 * 版 权 ：   天厚投资 版权所有 (c)
 * 作 者 :    wgyscsf
 * 创建日期 ：2017/10/16 16:27
 * 描 述 ：
 * ============================================================
 **/
public class BaseActivity extends AppCompatActivity {
    /**
     * Activity 跳转
     *
     * @param clazz  目标activity
     * @param bundle 传递参数
     * @param finish 是否结束当前activity
     */
    public static final int NON_CODE = -1;

    protected Context mContext = null;
    protected String TAG = null;
    protected Activity mActivity;
    /**
     * 对系统系统的toast进行简单封装，方便使用
     */
    private Toast toast = null;

    //retrofit请求集合
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void setContentView(int view) {
        super.setContentView(view);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        TAG = this.getClass().getSimpleName();
        // getBundleExtras
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

    }

    protected void getBundleExtras(Bundle extras) {

    }

    /**
     * 添加Subscription
     *
     * @param subscription
     */
    public void addGcManagerSubscription(Subscription subscription) {
        Log.d(TAG, "add subscription");
        initSingletonCompositeSubscription();
        mCompositeSubscription.add(subscription);
    }

    private void initSingletonCompositeSubscription() {
        if (mCompositeSubscription == null) {
            synchronized (CompositeSubscription.class) {
                if (mCompositeSubscription == null) {
                    mCompositeSubscription = new CompositeSubscription();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            Log.d(TAG, "base activity unscbscribe");
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * startActivity
     *
     * @param clazz target Activity
     */
    public void go(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, false);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz  target Activity
     * @param bundle
     */
    public void go(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, false);
    }

    /**
     * startActivity then finish this
     *
     * @param clazz target Activity
     */
    public void goAndFinish(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, true);
    }

    /**
     * startActivity with bundle and then finish this
     *
     * @param clazz  target Activity
     * @param bundle bundle extra
     */
    public void goAndFinish(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, true);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void goForResult(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, false);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    protected void goForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        _goActivity(clazz, bundle, requestCode, false);
    }

    /**
     * startActivityForResult then finish this
     *
     * @param clazz
     * @param requestCode
     */
    protected void goForResultAndFinish(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, true);
    }

    /**
     * startActivityForResult with bundle and then finish this
     *
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    protected void goForResultAndFinish(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        _goActivity(clazz, bundle, requestCode, true);
    }


    //可以立刻刷新Toast。推荐使用该方式。
    public void showSingletonToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int toastDuration) {
        if (null != msg && (toastDuration == Toast.LENGTH_SHORT || toastDuration == Toast.LENGTH_LONG)) {
            Toast.makeText(getApplicationContext(), msg, toastDuration).show();
        }
    }

    private void _goActivity(Class<? extends Activity> clazz, Bundle bundle, int requestCode, boolean finish) {
        if (null == clazz) {
            throw new IllegalArgumentException("you must pass a target activity where to go.");
        }
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (requestCode > NON_CODE) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            finish();
        }
    }
}
