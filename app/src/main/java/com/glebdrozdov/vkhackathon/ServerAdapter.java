package com.glebdrozdov.vkhackathon;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.glebdrozdov.vkhackathon.Consts.ROOMS_COUNT;

public class ServerAdapter {

    private static String myJSON;
    private int times[] = new int[ROOMS_COUNT + 1];
    private int peopleAtThisMoment[] = new int[ROOMS_COUNT + 1];
    private int ratings[] = new int[ROOMS_COUNT + 1];

    public void getData() {
        class dataTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                Response response = null;

                try {
                    OkHttpClient client = new OkHttpClient();

                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("null", "null");
                    RequestBody formBody = formBuilder.build();
                    Request request = new Request.Builder()
                            .url("https://telegrambotdrozd.000webhostapp.com/data.php")
                            .post(formBody)
                            .build();

                    response = client.newCall(request).execute();
                    return response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (!s.trim().equals("{\"rooms\":[]}") && !s.trim().equals(null)) {
                    myJSON = s.trim();
                    parseList();
                }
            }
        }
        dataTask DataTask = new dataTask();
        DataTask.execute();
    }

    private void parseList() {
        try {
            if (myJSON.contains("{")) {
                JSONObject jsonObj = new JSONObject(myJSON.substring(myJSON.indexOf("{"), myJSON.lastIndexOf("}") + 1));
                JSONArray p = jsonObj.getJSONArray("rooms");
                for (int i = 0; i < p.length(); i++) {
                    JSONObject jsonObject = p.getJSONObject(i);
                    String time = jsonObject.getString("time");
                    String people = jsonObject.getString("people");
                    String rating = jsonObject.getString("rating");
                    times[i] = Integer.parseInt(time);
                    peopleAtThisMoment[i] = Integer.parseInt(people);
                    ratings[i] = Integer.parseInt(rating);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int[] getPeopleAtThisMoment() {
        return peopleAtThisMoment;
    }

    public int[] getTimes() {
        return times;
    }

    public int[] getRatings() {
        return ratings;
    }
}
