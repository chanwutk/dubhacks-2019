package com.example.one_handed_braille_keyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.HashMap;
import java.util.Map;

public class OneHandedBrailleKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private BrailleBuilder braillebuilder;
    private int pressedButton;
    private boolean isShifted;
    private Map<Integer, Integer> gestureMap;

    @Override
    public View onCreateInputView() {
        this.gestureMap = new HashMap<>();
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        kv.setPreviewEnabled(false);
        keyboard = new Keyboard(this,R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        braillebuilder = new BrailleBuilder(this);
        return kv;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        if (this.braillebuilder != null) this.braillebuilder.reset();
    }

    @Override
    public void onPress(int i) {
        this.pressedButton = i;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    public void onRelease(int i) {
        if (i != this.pressedButton) {

        }
        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        braillebuilder.input(i);
        if (braillebuilder.isReady()) {
            String key = braillebuilder.getChar();
            if (key == null) {
                // vibrate
            } else {
                ic.commitText(key,1);
            }
            braillebuilder.reset();
        }
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
