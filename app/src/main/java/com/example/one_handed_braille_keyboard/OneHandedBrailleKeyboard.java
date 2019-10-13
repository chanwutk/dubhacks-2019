package com.example.one_handed_braille_keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class OneHandedBrailleKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        keyboard = new Keyboard(this,R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int i) {
    }

    @Override
    public void onRelease(int i) {
        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        ic.commitText(String.valueOf((i + "").charAt(0)),1);
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
