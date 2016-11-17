package david.demos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import david.support.ext.net.IpScanner;
import david.support.ext.net.R;


/**
 *@author  David chen
 *@mail dingwei.chen1988@gmail.com
 * */
public class MainActivity extends Activity implements IpScanner.ScanCallback {


    private ProgressDialog mProgressDialog = null;
    private IpScanner mIpScanner = null;
    private TextView mTitle =null;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private DismissAction mDismissAction = new DismissAction();
    private List<String> mIps = new ArrayList<>(5);
    private String mHostIp;
    private ListView mListView = null;
    private ListAdapter mListAdapter = new ListAdapter();

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mIps.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = new TextView(MainActivity.this);
            tv.setText("Device:"+ mIps.get(i));
            tv.setGravity(Gravity.LEFT);
            return tv;
        }
    }

    private class DismissAction implements Runnable {

        @Override
        public void run() {
            mProgressDialog.dismiss();
            mTitle.setText("host ip:"+mHostIp+" | device :"+mIps.size());
            if (mIps.size() <= 0) {
                toast("Devices not found!");
            } else {
                mListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.result);
        mListView.setAdapter(mListAdapter);
        mTitle = (TextView)this.findViewById(R.id.title);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        mIpScanner = new IpScanner(3000,this)
                .setScannerLogger(new SimpleLogger());
        mIpScanner.startScan();
    }

    @Override
    public void onFound(Set<String> ip, String hostIp, int port) {
        mIps.addAll(ip);
        mHostIp = hostIp;
        mUiHandler.post(mDismissAction);
    }

    @Override
    public void onNotFound(String hostIp, int port) {
        mIps.clear();
        mHostIp = hostIp;
        Log.v("cdw","onNotFound "+hostIp);
        mUiHandler.post(mDismissAction);
    }
}
