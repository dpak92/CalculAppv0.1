package com.example.calculapp12;

import android.widget.Button;

public class KeysClass {
    String KeyText;
    Button KeyButton;

    public KeysClass(String keyText, Button keyButton) {
        KeyText = keyText;
        KeyButton = keyButton;
    }

    public String getKeyText() {
        return KeyText;
    }

    public void setKeyText(String keyText) {
        KeyText = keyText;
    }

    public Button getKeyButton() {
        return KeyButton;
    }

    public void setKeyButton(Button keyButton) {
        KeyButton = keyButton;
    }

    @Override
    public String toString() {
        return "KeysClass{" +
                "KeyText='" + KeyText + '\'' +
                ", KeyButton=" + KeyButton +
                '}';
    }
}
