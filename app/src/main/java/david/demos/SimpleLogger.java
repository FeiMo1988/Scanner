package david.demos;

import android.util.Log;

import david.support.ext.net.IpScanner;

/**
 * Created by chendingwei on 16/11/17.
 */

public class SimpleLogger implements IpScanner.ScannerLogger {
    @Override
    public void onScanLogPrint(String msg) {
        Log.v("cdw",">>>"+msg);
    }
}
