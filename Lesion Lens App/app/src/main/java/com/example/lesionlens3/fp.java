package com.example.lesionlens3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class fp extends AppCompatActivity {

    private TextSwitcher mTextSwitcher;
    private String mText = "Lesion Lens";
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp);

        // Initialize TextSwitcher
        mTextSwitcher = findViewById(R.id.textSwitcher);
        mTextSwitcher.setFactory(() -> {
            TextView textView = new TextView(fp.this);
            textView.setTextSize(40);
            textView.setTextColor(Color.rgb(115,94,211));
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            return textView;
        });

        // Start animation
        animateText();

        // Start MainActivity after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(fp.this, Login.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        }, 4000); // Delay in milliseconds
    }

    private void animateText() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIndex < mText.length()) {
                    String currentWord = getNextWord();
                    mTextSwitcher.setText(currentWord);
                    mIndex++;
                    handler.postDelayed(this, 2000); // Delay between words
                }
            }
        }, 1000); // Initial delay before printing the first word
    }

    private String getNextWord() {
        StringBuilder word = new StringBuilder();
        while (mIndex < mText.length()) {
            word.append(mText.charAt(mIndex));
            mIndex++;
        }
        return word.toString();
    }
}
