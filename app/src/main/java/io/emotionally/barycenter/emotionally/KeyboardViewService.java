package io.emotionally.barycenter.emotionally;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class KeyboardViewService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    ApplicationController apc;
    private Intent start_analysis = new Intent();
    private Vibrator keyboardVibrator;
    private ClipboardManager clipboardManager;
    Boolean caps = false, shift = false;

    @Override
    public View onCreateInputView() {

        keyboardVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        clipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);

        apc = new ApplicationController();
        start_analysis.setAction("io.emotionally.barycenter.emotionally.START_ANALYSIS");

        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);

        Keyboard keyboard = new Keyboard(this, R.layout.keyboard_layout);
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

            keyboardVibrator.vibrate(50);

            switch(primaryCode) {
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                // The Shift key
                case -1:
                    caps = !caps;
                    break;
                // Analyze from clipboard, was the '123' button
                case -2:
                    instantiateAnalysisOverlaySequence(true);
                    break;
                // Analysis key in bottom left.
                case -3:
                    instantiateAnalysisOverlaySequence(false);
                    break;
                default :
                    //Log.d("EMOTIONALLY", "Unknown Key Code Pressed: " + String.valueOf(primaryCode));
                    char code = (char) primaryCode;
                    if ( Character.isLetter(code) && caps ) {
                        code = Character.toUpperCase(code);
                    }
                    inputConnection.commitText(String.valueOf(code), 1);
            }
        }
    }

    // This function pulls all text from the current input
    // box and sends it over to the analysis overlay.
    public void instantiateAnalysisOverlaySequence(Boolean clipboard)  {

        Log.d("EMOTIONALLY", "Made it to the first function in sequence.");

        String inputText = null;
        if ( !clipboard ) {

            getCurrentInputConnection().performContextMenuAction(android.R.id.selectAll);
            inputText = (String) getCurrentInputConnection().getSelectedText(0);

        } else {

            if ( clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ) {
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                inputText = item.getText().toString();
            }

        }

        final String inputConnectionText = inputText;


        Log.d("EMOTIONALLY", "Input Box: " + inputConnectionText);

        // We must run this on another thread so as to prevent it
        // from locking up our keyboard due to the network call.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("EMOTIONALLY", "Communication with API.");
                String response = apc.getAnalysisApiController().getApiAdaptor().getAPI("IBMToneAPI").analyze( inputConnectionText );

                try {
                    JSONObject temp = new JSONObject(response);
                    Log.d("EMOTIONALLY", "Document Test: " + temp.getJSONObject("document_tone").toString() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startAnalysisOverlay(response);
            }
        };

        if ( inputConnectionText != null && !inputConnectionText.isEmpty() && !inputConnectionText.equals("") ) {
            new Thread(runnable).start();
        }

    }

    public void startAnalysisOverlay(String text) {

        Log.d("EMOTIONALLY", "Made it to analysis overlay invocation in keyboard.");

        Intent svc = new Intent(this, AnalysisOverlayService.class);
        svc.setAction("io.emotionally.barycenter.emotionally.ACTION_ANALYZE");

        svc.putExtra("io.emotionally.barycenter.emotionally.ANALYSIS_TEXT", text);
        svc.putExtra("io.emotionally.barycenter.emotionally.ANALYSIS_API_ID", "IBMToneAPI");

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