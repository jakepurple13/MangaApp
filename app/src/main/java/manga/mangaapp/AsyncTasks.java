package manga.mangaapp;

public interface AsyncTasks {

    void onPreExecute();
    boolean doInBackground();
    void onPostExecute(Boolean success);

}
