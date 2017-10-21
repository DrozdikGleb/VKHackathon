package com.glebdrozdov.vkhackathon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import static com.glebdrozdov.vkhackathon.Consts.ROOMS_COUNT;

public class RoomsActivity extends Activity {

    final String[] roomNumbers = new String[ROOMS_COUNT];
    boolean[] toVisit = new boolean[ROOMS_COUNT];
    Button btn;

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
    static final int MAX_TIME = (int) (24 * 60 * 60 * 1000 - Calendar.getInstance().getTimeInMillis());
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
        btn = (Button) findViewById(R.id.btn);
// определяем массив типа String

        bar();

// используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, roomNumbers);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView) view;
                check.setChecked(!check.isChecked());
                boolean click = !check.isChecked();
                check.setChecked(click);
                if (click) {
                    toVisit[position] = true;
                } else {
                    toVisit[position] = false;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerAdapter serverAdapter = new ServerAdapter();
                serverAdapter.getData();
                TIME = serverAdapter.getTimes();
                PEOPLE_NOW = serverAdapter.getPeopleAtThisMoment();
                RATING = serverAdapter.getRatings();

                Log.i("people-",String.valueOf(PEOPLE_NOW[0]));

                String route = "";
                fillPeopleMatrix();
                for (int i = 0; i < ROOMS_COUNT; i++) {
                    wasVisited[i] = false;
                }
                getDistances(new ArrayList<Integer>(), currentRoom);
                route += order.get(0);
                for (int i = 1; i < order.size(); i++) {
                    route += String.format(" -> %d", order.get(i));
                }

                // order - лист очередности посещения
                // route - чистая строка очередности посещения
            }
        });
    }

    void bar() {
        for (int i = 0; i < 400; i++) {
            roomNumbers[i] = String.valueOf(i + 1);
        }
    }
}
