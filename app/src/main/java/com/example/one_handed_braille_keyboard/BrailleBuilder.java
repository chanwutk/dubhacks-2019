package com.example.one_handed_braille_keyboard;

import android.content.Context;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.opencsv.CSVReader;


public class BrailleBuilder {
    private int value;
    public int counter;
    Map<Integer, String> keyMap;

    public BrailleBuilder (Context context) {
        this.keyMap = new HashMap<Integer, String>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(context.getResources().openRawResource(R.raw.braille_eng)));
            String[] line;
            while ((line = reader.readNext()) != null) {
                keyMap.put(Integer.parseInt(line[1]), line[0]);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        value = 0;
        counter = 0;
    }

    public boolean isReady() {
        return this.counter % 3 == 0 && (this.getChar() == null || !this.getChar().isEmpty());
    }

    public String getChar() {
        return this.keyMap.get(this.value);  // null not found, '\0' need another block
    }

    public void reset() {
        value = 0;
        counter = 0;
    }

    public void input(int in) {
        value = 4 * value + in;
        this.counter++;
    }
}