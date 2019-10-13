package com.example.one_handed_braille_keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class OneHandedBrailleKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private BrailleBuilder braillebuilder;
    private int pressedButton;
    private Map<Integer, Integer> gestureMap;
    private Vibrator vibrator;

    @Override
    public View onCreateInputView() {
        this.gestureMap = this.readGestureMap();
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        kv.setPreviewEnabled(false);
        keyboard = new Keyboard(this,R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        braillebuilder = new BrailleBuilder(this);
        return kv;
    }

    private Map<Integer, Integer> readGestureMap() {

        Map<Integer, Integer> result = new HashMap();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(this.getResources().openRawResource(R.raw.gesture_map)));
            String[] line;
            while ((line = reader.readNext()) != null) {
                result.put(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        if (this.braillebuilder != null) this.braillebuilder.reset();
    }

    @Override
    public void onPress(int primaryCode) {
        this.pressedButton = primaryCode;
        this.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    public void onRelease(int primaryCode) {
        InputConnection ic = getCurrentInputConnection();
        if (primaryCode != this.pressedButton) {
            this.makeGesture(primaryCode, ic);
        } else {
            playClick(primaryCode);
            braillebuilder.input(primaryCode);
            if (braillebuilder.isReady()) {
                String key = braillebuilder.getChar();
                if (key == null) {
                    if (this.braillebuilder.getCounter() == 3) {
                        this.vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        this.vibrator.vibrate(VibrationEffect.createOneShot(750, VibrationEffect.DEFAULT_AMPLITUDE));
                        braillebuilder.reset();
                    }
                } else {
                    ic.commitText(key,1);
                    braillebuilder.reset();
                }
            }
        }
    }

    private void makeGesture(int primaryCode, InputConnection ic) {
        Integer gesture = this.gestureMap.get(this.pressedButton * 4 + primaryCode);
        if (gesture != null) {
            switch (gesture.intValue()) {
                case 0:
                    ic.commitText(" ",1);
                    break;
                case 1:
                    ic.deleteSurroundingText(1,0);
                    break;
                case 2:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;
                case 3:
                    break;
            }
        } else {
            this.vibrator.vibrate(VibrationEffect.createOneShot(750, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        this.braillebuilder.reset();
    }

    @Override
    public void onKey(int i, int[] ints) {
    }

    private void playClick(int i) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
    }

    @Override
    public void onText(CharSequence charSequence) {
    }

    @Override
    public void swipeLeft() {
//        InputConnection ic = getCurrentInputConnection();
//        ic.deleteSurroundingText(1,0);
    }

    @Override
    public void swipeRight() {
//        InputConnection ic = getCurrentInputConnection();
//        ic.deleteSurroundingText(1,0);
//        ic.commitText(String.valueOf(' '),1);
    }

    @Override
    public void swipeDown() {
//        InputConnection ic = getCurrentInputConnection();
//        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
    }

    @Override
    public void swipeUp() {
    }
}
