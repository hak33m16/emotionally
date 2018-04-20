package io.emotionally.barycenter.emotionally;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.provider.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.lang.reflect.Method;

public class KeyboardViewService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private Intent start_analysis = new Intent();

    @Override
    public View onCreateInputView() {

        start_analysis.setAction("io.emotionally.barycenter.emotionally.START_ANALYSIS");

        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.layout.number_pad);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch(primaryCode) {
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                // Analysis key in bottom left.
                case -3:
                    instantiateAnalysisOverlay();
                    break;
                default :
                    Log.d("EMOTIONALLY", "Unknown Key Code Pressed: " + String.valueOf(primaryCode));
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
            }
        }
    }

    public void instantiateAnalysisOverlay()  {

        getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
        String boxOfText = (String) getCurrentInputConnection().getSelectedText(0);

        Log.d("EMOTIONALLY", "Analysis instantiation in keyboard activated.");
        Log.d("EMOTIONALLY", boxOfText);

        Intent svc = new Intent(this, AnalysisOverlayService.class);
        svc.putExtra("ANALYSIS", boxOfText);

        // Close activity if it is already open
        stopService(svc);
        startService(svc);

    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}