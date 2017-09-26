package manga.mangaapp;

import android.app.Dialog;
import android.os.AsyncTask;

import com.yarolegovich.lovelydialog.LovelyProgressDialog;

/**
 * Created by Jacob on 8/23/17.
 */

public class RetrieveInfo extends AsyncTask<String, Integer, Boolean> {

    private AsyncTasks asyncTasks;
    private LovelyProgressDialog dialog;

    public RetrieveInfo(AsyncTasks asyncTasks) {
        this.asyncTasks = asyncTasks;
    }

    public RetrieveInfo(AsyncTasks asyncTasks, LovelyProgressDialog dialog) {
        this.asyncTasks = asyncTasks;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        //do something before starting
        asyncTasks.onPreExecute();
        if(dialog!=null) {
            dialog.show();
        }
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        //do in background
        return asyncTasks.doInBackground();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        // do something with the feed
        asyncTasks.onPostExecute(success);
        if(dialog!=null) {
            dialog.dismiss();
        }
    }

    /*@Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        asyncTasks.onProgessUpdate(values);
    }*/

    public void pleaseWait() {
        /*
        int per = 0;
        final AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle("Please wait").setMessage("Hello").setCancelable(true).create();

        //sourceChanger.execute();
        if(sourceChanges!=null) {
            sourceChanges.cancel(true);
        }
        sourceChanges = new AsyncTask<Integer, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                per = 0;
                ad.show();
            }

            @Override
            protected Boolean doInBackground(Integer... integers) {

                for(int i=0;i<1000;i++) {
                    per++;
                    publishProgress(per);
                    if(isCancelled()) {
                        break;
                    }
                }

                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

                siteLink.setText(values[0]+"%");
                Help.e(values[0] + "%");
                ad.setMessage(values[0]+"%");

            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

            }
        }.execute();*/
    }

}

