package com.glebdrozdov.vkhackathon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.glebdrozdov.vkhackathon.Consts.ROOMS_COUNT;

public class RoomsActivity extends Activity {

    private String myJSON;
    private int times[] = new int[ROOMS_COUNT + 1];
    private int peopleAtThisMoment[] = new int[ROOMS_COUNT + 1];
    private int ratings[] = new int[ROOMS_COUNT + 1];

    public int[] getPeopleAtThisMoment() {
        return peopleAtThisMoment;
    }

    public int[] getTimes() {
        return times;
    }

    public int[] getRatings() {
        return ratings;
    }

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
                else{
                    Toast.makeText(RoomsActivity.this, "NO DATA", Toast.LENGTH_SHORT).show();
                }
            }
        }
        dataTask DataTask = new dataTask();
        DataTask.execute();
    }

    public void parseList() {
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
            else{
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    final String[] roomNames = new String[ROOMS_COUNT];
    boolean[] toVisit = new boolean[ROOMS_COUNT];
    final HashMap<String, List<Integer>> hm = new HashMap<>();

    static class DistanceAndTime {
        int distance;
        int time;
        int parent;

        DistanceAndTime(int distance, int time) {
            this.distance = distance;
            this.time = time;
            parent = -1;
        }
    }

    static final int CONST = 777;
    static final int PEOPLE_WEIGHT_RATE = CONST;
    static final int WAS_VISITED_RATE = CONST;
    static final int TRANSFER_RATE = CONST;// TODO: change later
    static final int MAX_TIME = 99;//(int) (24 * 60 * 60 * 1000 - Calendar.getInstance().getTimeInMillis());
    // from database
    static final int[][] PROBABILITY = new int[ROOMS_COUNT][ROOMS_COUNT];
    static int[] TIME = new int[ROOMS_COUNT];
    static int[] PEOPLE_NOW = new int[ROOMS_COUNT];
    static int[] RATING = new int[ROOMS_COUNT];

    // from somewhere (probably database)
    static ArrayList<Integer>[] matrix = new ArrayList[ROOMS_COUNT];

    static int[][] people = new int[ROOMS_COUNT][MAX_TIME];
    static int currentTime = 0;
    static boolean[] wasVisited = new boolean[ROOMS_COUNT];
    static ArrayList<Integer> order = new ArrayList<>();
    // from geolocation
    static int currentRoom = 0;

    private static void fillPeopleMatrix() {
        for (int time = 0; time < MAX_TIME - 1; time++) {
            for (int i = 0; i < ROOMS_COUNT; i++) {
                for (int j = 0; j < ROOMS_COUNT; j++) {
                    people[i][time + 1] += people[j][time] * PROBABILITY[j][i];
                }
            }
        }
    }

    private static int countDistance(int currentTime, int verticeToLook) {
        int tempDistance = 0;

        for (int i = 0; i < TIME[verticeToLook]; i++) {
            tempDistance += people[verticeToLook][i + currentTime] * PEOPLE_WEIGHT_RATE;
        }

        if (wasVisited[verticeToLook]) {
            tempDistance += WAS_VISITED_RATE;
        }

        // нужно ли это?
        tempDistance += TRANSFER_RATE;

        // вычитаем из стоимости рейтинг зала, тк чем выше рейтинг тем больше мы туда хотим
        tempDistance -= RATING[verticeToLook];

        return tempDistance;
    }

    static void getDistances(ArrayList<Integer> toVisit, int startVertice) {
        boolean[] needToVisit = new boolean[ROOMS_COUNT];
        DistanceAndTime[] distancesWithTimes = new DistanceAndTime[ROOMS_COUNT];

        for (int i = 0; i < toVisit.size(); i++) {
            needToVisit[toVisit.get(i)] = true;
        }

        for (int i = 0; i < ROOMS_COUNT; i++) {
            distancesWithTimes[i] = new DistanceAndTime(Integer.MAX_VALUE, currentTime);
        }
        distancesWithTimes[startVertice].distance = 0;

        // сам алгоритм
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVertice);
        while (!queue.isEmpty()) {
            int currentVertice = queue.poll();
            for (int i = 0; i < matrix[currentVertice].size(); i++) {
                int verticeToLook = matrix[currentVertice].get(i);
                int newDistance = distancesWithTimes[currentVertice].distance +
                        countDistance(distancesWithTimes[currentVertice].time, verticeToLook);
                if (newDistance < distancesWithTimes[verticeToLook].distance) {
                    distancesWithTimes[verticeToLook].distance = newDistance;
                    distancesWithTimes[verticeToLook].time = distancesWithTimes[currentVertice].time + TIME[verticeToLook];
                    distancesWithTimes[verticeToLook].parent = currentVertice;
                    queue.add(verticeToLook);
                }
            }
        }
        // конец алгоритма

        int closestVertice = -1;
        int minimalDistance = Integer.MAX_VALUE;
        for (int i = 0; i < ROOMS_COUNT; i++) {
            if (needToVisit[i]) {
                if (minimalDistance > distancesWithTimes[i].distance) {
                    minimalDistance = distancesWithTimes[i].distance;
                    closestVertice = i;
                }
            }
        }

        // rebuild of path
        int currentVertice = closestVertice;
        ArrayList<Integer> transferList = new ArrayList<>();
        while (currentVertice != -1) {
            transferList.add(currentVertice);
            currentVertice = distancesWithTimes[currentVertice].parent;
        }
        for (int i = 0; i < transferList.size(); i++) {
            order.add(transferList.get(transferList.size() - i - 1));
            currentTime += TIME[transferList.get(transferList.size() - i - 1)];
        }

        for (int i = 0; i < toVisit.size(); i++) {
            if (toVisit.get(i) == closestVertice) {
                toVisit.remove(i);
            }
        }

        if (toVisit.size() > 0) {
            getDistances(toVisit, closestVertice);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        ListView listView = (ListView) findViewById(R.id.listView);
        Button btn = (Button) findViewById(R.id.btn);
        Log.i("www", "1");
        getData();
        fillMap();
        bar();
        Log.i("www", "1");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, roomNames);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Log.i("www", "1");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView) view;
                check.setChecked(!check.isChecked());
                boolean click = !check.isChecked();
                check.setChecked(click);
                if (click) {
                    int number = Integer.parseInt(roomNames[position].substring(roomNames[position].lastIndexOf(' ') + 1));
                    toVisit[number - 1] = true;
                } else {
                    int number = Integer.parseInt(roomNames[position].substring(roomNames[position].lastIndexOf(' ') + 1));
                    toVisit[number - 1] = false;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TIME = getTimes();
                PEOPLE_NOW = getPeopleAtThisMoment();
                RATING = getRatings();

                Log.i("people-", String.valueOf(PEOPLE_NOW[0]));

                String route = "";
                fillPeopleMatrix();
                for (int i = 0; i < ROOMS_COUNT; i++) {
                    wasVisited[i] = false;
                }
                getDistances(new ArrayList<Integer>(), currentRoom);
                order.add(CONST);
                route += order.get(0);
                for (int i = 1; i < order.size(); i++) {
                    route += String.format(" -> %d", order.get(i));
                }
                Log.i("people-", route);
                // order - лист очередности посещения
                // route - чистая строка очередности посещения
                String s = "";
                for (int i = 0; i < toVisit.length; i++) {
                    s += toVisit[i] + " ";
                }
                //Toast.makeText(RoomsActivity.this, route, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void bar() {
        int j = 0;
        for (HashMap.Entry<String, List<Integer>> entry : hm.entrySet()) {
            String key = entry.getKey();
            List value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                roomNames[j] = key + " " + String.valueOf(value.get(i));
                j++;
            }
        }
        for (int i = j; i < 400; i++) {
            roomNames[i] = "Античный мир 109";
        }
    }

    void fillMap() {
        hm.put("Древности Сибири", Arrays.asList(26, 28, 29, 30, 31, 32));
        hm.put("Средняя Азия", Arrays.asList(38, 39, 40, 46, 47, 48, 49, 50, 51, 52, 53, 54));
        hm.put("Античный мир", Arrays.asList(101, 102, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 121, 127, 128, 129, 130, 131));
        hm.put("Кавказ", Arrays.asList(55, 56, 57, 58, 59, 60, 61, 62, 63));
        hm.put("Древний Египет", Arrays.asList(100));
        hm.put("Древнейшие памятники Евразии", Arrays.asList(11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 27, 33));
        hm.put("Древний Восток", Arrays.asList(89, 90));
        hm.put("Русская культура", Arrays.asList(147, 148, 149, 150, 151, 153, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187));
        hm.put("Дворцовые интерьеры", Arrays.asList(155, 156, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 204, 260, 270, 271, 282, 289, 304, 305, 307, 308));
        hm.put("Франция", Arrays.asList(272, 275, 278, 279, 280, 282, 283, 284, 285, 286, 287, 288, 290, 291, 292, 293, 294, 295, 296, 297));
        hm.put("Испания", Arrays.asList(239, 240));
        hm.put("Германия", Arrays.asList(263, 266, 267, 268));
        hm.put("Англия", Arrays.asList(298, 299, 300));
        hm.put("Фландрия", Arrays.asList(245, 246, 247));
        hm.put("Голландия", Arrays.asList(249, 250, 251, 252, 253, 254));
        hm.put("Нидерланды", Arrays.asList(248, 258, 261, 262));
        hm.put("Италия", Arrays.asList(207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 223, 224, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238));
        hm.put("Западноевропейское Средневековье", Arrays.asList(259));
        hm.put("Западноевропейское Оружие", Arrays.asList(243));
        hm.put("Разное", Arrays.asList(143, 144, 145, 146, 152, 200, 202, 203, 206, 302, 269, 255, 256, 257, 241, 244));
        hm.put("Эрмитажный театр", Arrays.asList(225));
        hm.put("Западная Европа", Arrays.asList(334, 335, 336, 337, 338, 339, 340, 341, 342));
        hm.put("Франция 20 век", Arrays.asList(314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 328, 330, 331, 332, 333, 343, 344, 345, 347, 348, 349, 350));
        hm.put("Византия и страны Востока", Arrays.asList(381, 382, 383, 384, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397));
        hm.put("Дальний Восток и Центральная Азия", Arrays.asList(351, 352, 353, 354, 355, 356, 357, 358, 376, 375, 368, 369, 370, 359, 360, 361, 362, 363, 364, 365, 366, 367));
        hm.put("Другое", Arrays.asList(398, 399, 400));


    }
}
