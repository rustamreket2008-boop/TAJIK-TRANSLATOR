package com.example.tajiktranslator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    int BG = Color.rgb(10, 18, 35);
    int CARD = Color.rgb(22, 32, 54);
    int BLUE = Color.rgb(45, 125, 255);
    int WHITE = Color.WHITE;
    int GRAY = Color.rgb(170, 180, 200);

    LinearLayout main;
    EditText input;
    TextView result;
    Spinner fromLanguage;
    Spinner toLanguage;

    String[] languages = {
            "🇹🇯  Тоҷикӣ",
            "🇬🇧  English",
            "🇷🇺  Русский"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(20, 25, 20, 20);
        main.setBackgroundColor(BG);

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        // LOGO
        TextView logo = new TextView(this);
        logo.setText("🌐");
        logo.setTextSize(45);
        logo.setGravity(Gravity.CENTER);

        content.addView(
                logo,
                new LinearLayout.LayoutParams(-1, 65)
        );

        // TITLE
        TextView title = new TextView(this);
        title.setText("TAJIK TRANSLATOR");
        title.setTextSize(27);
        title.setTextColor(WHITE);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        content.addView(title);

        // SUBTITLE
        TextView subtitle = new TextView(this);
        subtitle.setText("Тоҷикӣ  •  English  •  Русский");
        subtitle.setTextSize(14);
        subtitle.setTextColor(GRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 5, 0, 20);

        content.addView(subtitle);

        // LANGUAGE CARD
        LinearLayout languageCard = new LinearLayout(this);
        languageCard.setOrientation(LinearLayout.HORIZONTAL);
        languageCard.setGravity(Gravity.CENTER_VERTICAL);
        languageCard.setPadding(12, 8, 12, 8);
        languageCard.setBackground(round(CARD, 25));

        fromLanguage = new Spinner(this);
        toLanguage = new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        languages
                );

        fromLanguage.setAdapter(adapter);
        toLanguage.setAdapter(adapter);

        fromLanguage.setSelection(0);
        toLanguage.setSelection(1);

        languageCard.addView(
                fromLanguage,
                new LinearLayout.LayoutParams(0, 60, 1)
        );

        TextView swap = new TextView(this);
        swap.setText("⇄");
        swap.setTextSize(28);
        swap.setTextColor(BLUE);
        swap.setGravity(Gravity.CENTER);

        languageCard.addView(
                swap,
                new LinearLayout.LayoutParams(55, 60)
        );

        languageCard.addView(
                toLanguage,
                new LinearLayout.LayoutParams(0, 60, 1)
        );

        content.addView(
                languageCard,
                new LinearLayout.LayoutParams(-1, 75)
        );

        // INPUT
        TextView inputTitle = new TextView(this);
        inputTitle.setText("  Матн");
        inputTitle.setTextSize(16);
        inputTitle.setTextColor(WHITE);
        inputTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        inputTitle.setPadding(0, 20, 0, 8);

        content.addView(inputTitle);

        input = new EditText(this);
        input.setHint("Матни худро нависед...");
        input.setHintTextColor(GRAY);
        input.setTextColor(WHITE);
        input.setTextSize(18);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setPadding(20, 18, 20, 18);
        input.setBackground(round(CARD, 25));

        LinearLayout.LayoutParams inputParams =
                new LinearLayout.LayoutParams(-1, 170);

        content.addView(input, inputParams);

        // TRANSLATE BUTTON
        Button translate = new Button(this);
        translate.setText("✨  ТАРҶУМА КУН");
        translate.setTextSize(17);
        translate.setTextColor(WHITE);
        translate.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );
        translate.setAllCaps(false);
        translate.setBackground(round(BLUE, 30));

        LinearLayout.LayoutParams translateParams =
                new LinearLayout.LayoutParams(-1, 65);

        translateParams.setMargins(0, 18, 0, 0);

        content.addView(
                translate,
                translateParams
        );

        // RESULT TITLE
        TextView resultTitle = new TextView(this);
        resultTitle.setText("  Тарҷума");
        resultTitle.setTextSize(16);
        resultTitle.setTextColor(WHITE);
        resultTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );
        resultTitle.setPadding(0, 20, 0, 8);

        content.addView(resultTitle);

        // RESULT
        result = new TextView(this);
        result.setText(
                "Натиҷаи тарҷума дар ин ҷо нишон дода мешавад..."
        );
        result.setTextSize(18);
        result.setTextColor(WHITE);
        result.setGravity(
                Gravity.TOP | Gravity.START
        );
        result.setPadding(20, 20, 20, 20);
        result.setBackground(round(CARD, 25));

        content.addView(
                result,
                new LinearLayout.LayoutParams(-1, 170)
        );

        // BOTTOM BUTTONS
        LinearLayout buttons = new LinearLayout(this);
        buttons.setGravity(Gravity.CENTER);
        buttons.setPadding(0, 15, 0, 10);

        Button copy = new Button(this);
        copy.setText("📋  COPY");
        copy.setTextColor(WHITE);
        copy.setAllCaps(false);

        Button clear = new Button(this);
        clear.setText("🗑  CLEAR");
        clear.setTextColor(WHITE);
        clear.setAllCaps(false);

        buttons.addView(
                copy,
                new LinearLayout.LayoutParams(0, 60, 1)
        );

        buttons.addView(
                clear,
                new LinearLayout.LayoutParams(0, 60, 1)
        );

        content.addView(buttons);

        // TRANSLATE
        translate.setOnClickListener(v -> {

            String text =
                    input.getText().toString().trim();

            if (text.isEmpty()) {

                result.setText(
                        "⚠️ Аввал матнро нависед."
                );

            } else {

                String from =
                        fromLanguage
                                .getSelectedItem()
                                .toString();

                String to =
                        toLanguage
                                .getSelectedItem()
                                .toString();

                result.setText(
                        "Аз: " + from +
                        "\nБа: " + to +
                        "\n\n" + text
                );
            }
        });

        // COPY
        copy.setOnClickListener(v -> {

            ClipboardManager clipboard =
                    (ClipboardManager)
                    getSystemService(
                            Context.CLIPBOARD_SERVICE
                    );

            ClipData clip =
                    ClipData.newPlainText(
                            "translation",
                            result.getText().toString()
                    );

            clipboard.setPrimaryClip(clip);

            Toast.makeText(
                    this,
                    "✓ Нусха шуд",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // CLEAR
        clear.setOnClickListener(v -> {

            input.setText("");

            result.setText(
                    "Натиҷаи тарҷума дар ин ҷо нишон дода мешавад..."
            );
        });

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1, -1)
        );

        setContentView(main);
    }

    GradientDrawable round(
            int color,
            int radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(color);
        g.setCornerRadius(radius);

        return g;
    }
            }
