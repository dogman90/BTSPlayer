package io.swnomad.btsplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VideoInfoReader {

    private static final String muvieUrl = "https://nobbaggu.github.io/assets/rss_feed/feed_bts_muvie.json";
    private static final String liveUrl = "https://nobbaggu.github.io/assets/rss_feed/feed_bts_live.json";
    private static final String vlogUrl = "https://nobbaggu.github.io/assets/rss_feed/feed_bts_log.json";

    private static String muvieJsonText;
    private static String liveJsonText;
    private static String vlogJsonText;

    public static void load() {
        muvieJsonText = loadJsonText(muvieUrl);
        liveJsonText = loadJsonText(liveUrl);
        vlogJsonText = loadJsonText(vlogUrl);
    }

    private static String loadJsonText(final String urlString) {
        final String[] jsonText = new String[1];

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoInput(true);

                    int responseCode = conn.getResponseCode();//데이터 요청

                    InputStream is = conn.getInputStream(); // 연결된 입력 스트림 객체 참조
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null) {
                        jsonBuilder.append(line);
                    }


                    br.close();
                    isr.close();
                    is.close();
                    conn.disconnect();

                    jsonText[0] = jsonBuilder.toString();


                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
        service.shutdown();
        try {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonText[0];
    }

    public static ArrayList<VideoItem> getMuvieItems() {
        ArrayList<VideoItem> items = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(muvieJsonText);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String title = item.getString("title");
                String id = item.getString("id");
                String addedDate = item.getString("addedDate");
                String thumbnailUrl = item.getString("picture");

                items.add(new VideoItem(title, addedDate, id, thumbnailUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static ArrayList<VideoItem> getLiveItems() {
        ArrayList<VideoItem> items = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(liveJsonText);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String title = item.getString("title");
                String id = item.getString("id");
                String addedDate = item.getString("addedDate");
                String thumbnailUrl = item.getString("picture");

                items.add(new VideoItem(title, addedDate, id, thumbnailUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static ArrayList<VideoItem> getVlogItems() {
        ArrayList<VideoItem> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(vlogJsonText);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String title = item.getString("title");
                String id = item.getString("id");
                String addedDate = item.getString("addedDate");
                String thumbnailUrl = item.getString("picture");

                items.add(new VideoItem(title, addedDate, id, thumbnailUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }
}
