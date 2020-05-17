package io.swnomad.btsplayer.datahandlers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import io.swnomad.btsplayer.VideoInfoReader;
import io.swnomad.btsplayer.VideoItem;

public class SQLiteHandler {

    private SQLiteDatabase db;

    private static HashMap<String, VideoItem> dbMuvieItems;
    private static HashMap<String, VideoItem> dbLiveItems;
    private static HashMap<String, VideoItem> dbVlogItems;
    private static HashMap<String, VideoItem> dbFavoriteItems;

    private static SQLiteHandler instance;

    public static SQLiteHandler getInstance(Context context) {
        if(instance == null) {
            instance = new SQLiteHandler(context);
        }

        return instance;
    }

    private SQLiteHandler(Context context) {

        db = context.openOrCreateDatabase("videosInfo",
                Context.MODE_ENABLE_WRITE_AHEAD_LOGGING, null);

        String createMuvieTable = "create table if not exists muvies(title text, videoId text, pubDate text, thumbnailUrl text);";
        String createLiveTable = "create table if not exists lives(title text, videoId text, pubDate text, thumbnailUrl text);";
        String createVlogTable = "create table if not exists vlogs(title text, videoId text, pubDate text, thumbnailUrl text);";
        String createFavoritesTable = "create table if not exists favorites(title text, videoId text, pubDate text, thumbnailUrl text);";

        db.execSQL(createMuvieTable);
        db.execSQL(createLiveTable);
        db.execSQL(createVlogTable);
        db.execSQL(createFavoritesTable);

        dbMuvieItems = new HashMap<>();
        dbLiveItems = new HashMap<>();
        dbVlogItems = new HashMap<>();
        dbFavoriteItems = new HashMap<>();

        updateDB();
        loadVideosFromDB();
    }

    private void loadVideosFromDB() {
        //뮤비
        Cursor cursor = db.rawQuery("SELECT * FROM muvies", null);
        //int count = cursor.getCount(); //데이터의 갯수
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0); // 커서가 가르키는 column0(title) 받기
            String videoId = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String thumbnailUrl = cursor.getString(3);

            dbMuvieItems.put(videoId, new VideoItem(title, pubDate, videoId, thumbnailUrl));

            cursor.moveToNext(); // 다음 데이터로 이동
        }
        cursor.close();

        //라이브
        cursor = db.rawQuery("SELECT * FROM lives", null);
        //int count = cursor.getCount(); //데이터의 갯수
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0); // 커서가 가르키는 column0(title) 받기
            String videoId = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String thumbnailUrl = cursor.getString(3);

            dbLiveItems.put(videoId, new VideoItem(title, pubDate, videoId, thumbnailUrl));

            cursor.moveToNext(); // 다음 데이터로 이동
        }
        cursor.close();

        //V로그
        cursor = db.rawQuery("SELECT * FROM vlogs", null);
        //int count = cursor.getCount(); //데이터의 갯수
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0); // 커서가 가르키는 column0(title) 받기
            String videoId = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String thumbnailUrl = cursor.getString(3);

            dbVlogItems.put(videoId, new VideoItem(title, pubDate, videoId, thumbnailUrl));

            cursor.moveToNext(); // 다음 데이터로 이동
        }
        cursor.close();

        //즐겨찾기
        cursor = db.rawQuery("SELECT * FROM favorites", null);
        //int count = cursor.getCount(); //데이터의 갯수
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0); // 커서가 가르키는 column0(title) 받기
            String videoId = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String thumbnailUrl = cursor.getString(3);

            dbFavoriteItems.put(videoId, new VideoItem(title, pubDate, videoId, thumbnailUrl));

            cursor.moveToNext(); // 다음 데이터로 이동
        }
        cursor.close();

    }

    private void updateDB() {
        if(!isMuviesLatest()) {
            db.execSQL("DELETE FROM muvies");
            HashMap<String, VideoItem> updatedMuvieItems = VideoInfoReader.getInstance().getMuvieItems();
            for(String id : updatedMuvieItems.keySet()) {
                VideoItem item = updatedMuvieItems.get(id);
                String title = item.getTitle().replace("\"", "'");
                String addedDate = item.getDate();
                String thumbnailUrl = item.getThumbnailUrl();
                String sql = "insert into muvies(title, videoId, pubDate, thumbnailUrl) " +
                        "values(" + "\"" + title + "\"," + "'" + id + "'," + "'" + addedDate + "'," + "'" + thumbnailUrl + "');";
                db.execSQL(sql);
            }
        }

        if(!isLivesLatest()) {
            db.execSQL("DELETE FROM lives");
            HashMap<String, VideoItem> updatedLiveItems = VideoInfoReader.getInstance().getLiveItems();
            for(String id : updatedLiveItems.keySet()) {
                VideoItem item = updatedLiveItems.get(id);
                String title = item.getTitle().replace("\"", "'");
                String addedDate = item.getDate();
                String thumbnailUrl = item.getThumbnailUrl();
                String sql = "insert into lives(title, videoId, pubDate, thumbnailUrl) " +
                        "values(" + "\"" + title + "\"," + "'" + id + "'," + "'" + addedDate + "'," + "'" + thumbnailUrl + "');";
                db.execSQL(sql);
            }
        }

        if(!isVlogsLatest()) {
            db.execSQL("DELETE FROM vlogs");
            HashMap<String, VideoItem> updatedVlogItems = VideoInfoReader.getInstance().getVlogItems();
            for(String id : updatedVlogItems.keySet()) {
                VideoItem item = updatedVlogItems.get(id);
                String title = item.getTitle().replace("\"", "'");
                String addedDate = item.getDate();
                String thumbnailUrl = item.getThumbnailUrl();
                String sql = "insert into vlogs(title, videoId, pubDate, thumbnailUrl) " +
                        "values(" + "\"" + title + "\"," + "'" + id + "'," + "'" + addedDate + "'," + "'" + thumbnailUrl + "');";
                db.execSQL(sql);
            }
        }
    }

    private boolean isMuviesLatest() {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM muvies", null);
        int muviesCount = cursor.getCount();
        int updatedMuviesCount = VideoInfoReader.getInstance().getMuvieItemsSize();

        cursor.close();

        return muviesCount == updatedMuviesCount;

    }

    private boolean isLivesLatest() {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM lives", null);
        int livesCount = cursor.getCount();
        int updatedLivesCount = VideoInfoReader.getInstance().getLiveItemsSize();

        cursor.close();

        return livesCount == updatedLivesCount;
    }

    private boolean isVlogsLatest() {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM vlogs", null);
        int vlogsCount = cursor.getCount();
        int updatedVlogsCount = VideoInfoReader.getInstance().getVlogItemsSize();

        cursor.close();

        return vlogsCount == updatedVlogsCount;
    }

    public ArrayList<VideoItem> getMuvieItems() {
        return new ArrayList<>(dbMuvieItems.values());
    }

    public ArrayList<VideoItem> getLiveItems() {
        return new ArrayList<>(dbLiveItems.values());
    }

    public ArrayList<VideoItem> getVlogItems() {
        return new ArrayList<>(dbVlogItems.values());
    }

    public ArrayList<VideoItem> getFavoriteItems() {
        return new ArrayList<>(dbFavoriteItems.values());
    }

    public boolean addToFavorites(String id, String title, String pubDate, String thumbnailUrl) {

        String sql1 = "select * from favorites where videoId=" + "'" + id + "'";
        Cursor cursor = db.rawQuery(sql1, null);

        if (cursor.getCount() <= 0) {
            String sql = "insert into favorites(title, videoId, pubDate, thumbnailUrl) " +
                    "values(" + "\"" + title + "\"," + "'" + id + "'," + "'" + pubDate + "'," + "'" + thumbnailUrl + "');";
            db.execSQL(sql);
            dbFavoriteItems.put(id, new VideoItem(title, pubDate, id, thumbnailUrl));
            return true;
        } else {
            return false;
        }
    }

    public void deleteFromFavorites(String id) {
        String[] whereArgs = {id};
        db.delete("favorites", "videoId = ?", whereArgs);
        dbFavoriteItems.remove(id);
    }
}
