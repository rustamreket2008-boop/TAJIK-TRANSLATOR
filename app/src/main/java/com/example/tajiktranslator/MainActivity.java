package com.example.tajiktranslator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout main;
    EditText input;
    TextView result;

    int blue = Color.rgb(25, 118, 210);
    int dark = Color.rgb(18, 24, 38);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(24, 30, 24, 24);
        main.setBackgroundColor(Color.rgb(245, 247, 251));

        // TITLE
        TextView title = new TextView(this);
        title.setText("🌐  TAJIK TRANSLATOR");
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(blue);
        title.setGravity(Gravity.CENTER);
        main.addView(title, new LinearLayout.LayoutParams(
                -1, 70
        ));

        // LANGUAGE BAR
        LinearLayout languages = new LinearLayout(this);
        languages.setGravity(Gravity.CENTER);
        languages.setPadding(10, 10, 10, 10);

        TextView tajik = languageButton("Тоҷикӣ");
        TextView arrow = languageButton("  ⇄  ");
        TextView english = languageButton("English");

        languages.addView(tajik);
        languages.addView(arrow);
        languages.addView(english);

        main.addView(languages);

        // INPUT
        input = new EditText(this);
        input.setHint("Матнро нависед...");
        input.setTextSize(18);
        input.setGravity(Gravity.TOP);
        input.setPadding(20, 20, 20, 20);
        input.setBackground(round(Color.WHITE, 25));

        LinearLayout.LayoutParams inputParams =
                new LinearLayout.LayoutParams(-1, 180);
        inputParams.setMargins(0, 20, 0, 15);
        main.addView(input, inputParams);

        // TRANSLATE BUTTON
        Button translate = new Button(this);
        translate.setText("🌐  TRANSLATE");
        translate.setTextSize(17);
        translate.setTextColor(Color.WHITE);
        translate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        translate.setBackground(round(blue, 30));

        main.addView(translate, new LinearLayout.LayoutParams(
                -1, 65
        ));

        // RESULT
        result = new TextView(this);
        result.setText("Тарҷума дар ин ҷо нишон дода мешавад...");
        result.setTextSize(18);
        result.setTextColor(Color.DKGRAY);
        result.setPadding(20, 20, 20, 20);
        result.setBackground(round(Color.WHITE, 25));

        LinearLayout.LayoutParams resultParams =
                new LinearLayout.LayoutParams(-1, 180);
        resultParams.setMargins(0, 20, 0, 15);
        main.addView(result, resultParams);

        // BOTTOM BUTTONS
        LinearLayout buttons = new LinearLayout(this);
        buttons.setGravity(Gravity.CENTER);

        Button copy = new Button(this);
        copy.setText("📋 COPY");

        Button clear = new Button(this);
        clear.setText("🗑 CLEAR");

        buttons.addView(copy, new LinearLayout.LayoutParams(0, 60, 1));
        buttons.addView(clear, new LinearLayout.LayoutParams(0, 60, 1));

        main.addView(buttons);

        // TRANSLATE ACTION
        translate.setOnClickListener(v -> {
            String text = input.getText().toString();

            if (text.trim().isEmpty()) {
                result.setText("Лутфан аввал матнро нависед.");
            } else {
                result.setText("Тарҷумаи матн:\n\n" + text);
            }
        });

        // CLEAR
        clear.setOnClickListener(v -> {
            input.setText("");
            result.setText("Тарҷума дар ин ҷо нишон дода мешавад...");
        });

        // COPY
        copy.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Тарҷума нусха шуд",
                    Toast.LENGTH_SHORT
            ).show();
        });

        setContentView(main);
    }

    TextView languageButton(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(16);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setTextColor(blue);
        t.setGravity(Gravity.CENTER);
        t.setPadding(15, 10, 15, 10);
        return t;
    }

    GradientDrawable round(int color, int radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        return g;
    }
            }
