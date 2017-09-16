package manga.mangaapp;

import android.os.AsyncTask;

/**
 * Created by Jacob on 8/23/17.
 */

public class RetrieveInfo extends AsyncTask<String, Void, Boolean> {

    private AsyncTasks asyncTasks;

    public RetrieveInfo(AsyncTasks asyncTasks) {
        this.asyncTasks = asyncTasks;
    }

    @Override
    protected void onPreExecute() {
        //do something before starting
        asyncTasks.onPreExecute();
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
    }

}

