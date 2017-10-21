package com.glebdrozdov.vkhackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListRooms extends AppCompatActivity {


    HashMap<String, List<Integer>> hm = new HashMap<>();
    Set<String> chosen = new HashSet<>();
    private Button btn;


    private void initMs() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rooms);
        initMs();
        btn = (Button) findViewById(R.id.button5);
        btn.setVisibility(View.GONE);


        ListView rv = (ListView) findViewById(R.id.listViewWings);


        Set<String> names = hm.keySet();
        final String[] ms = new String[names.size()];
        int h = 0;
        for (String s : names) {
            ms[h] = s;
            h++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, ms);

        rv.setAdapter(adapter);
        rv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView check = (CheckedTextView) view;
                check.setChecked(!check.isChecked());
                boolean click = !check.isChecked();
                check.setChecked(click);
                if (click) {
                    if (btn.getVisibility() == View.GONE) {
                        btn.setVisibility(View.VISIBLE);
                    }
                    //Toast.makeText(ListRooms.this, ms[position], Toast.LENGTH_SHORT).show();
                    chosen.add(ms[position]);
                } else {
                    if (btn.getVisibility() == View.VISIBLE && chosen.size() == 1) {
                        btn.setVisibility(View.GONE);
                    }
                    chosen.remove(ms[position]);
                }

            }
        });


    }


    public void makeRoot(View view) {
        List<Integer> list = new ArrayList<>();
        for (String i : chosen) {
            list.addAll(hm.get(i));
        }
    }
}
