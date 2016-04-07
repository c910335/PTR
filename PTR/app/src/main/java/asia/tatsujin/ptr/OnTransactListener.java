package asia.tatsujin.ptr;

/**
 * Created by tatsujin on 2016/4/8.
 */
public interface OnTransactListener {
    void onStartTransaction();
    void onFinishTransaction(String title);
}
