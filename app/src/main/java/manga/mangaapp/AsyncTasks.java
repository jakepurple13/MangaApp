package manga.mangaapp;

public interface AsyncTasks {

    void onPreExecute();
    boolean doInBackground();
    void onPostExecute(Boolean success);
    //void onProgessUpdate(Integer... progress);
    //void publishYourProgress(Integer... progress);
    //abstract void updatethis();

}
