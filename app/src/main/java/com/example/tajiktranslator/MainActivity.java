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
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends Activity {

    EditText input;
    TextView result;
    Spinner fromLanguage, toLanguage;

    HashMap<String, String> tjToRu = new HashMap<>();
    HashMap<String, String> tjToEn = new HashMap<>();
    HashMap<String, String> ruToTj = new HashMap<>();
    HashMap<String, String> ruToEn = new HashMap<>();
    HashMap<String, String> enToTj = new HashMap<>();
    HashMap<String, String> enToRu = new HashMap<>();

    String[] languages = {
            "🇹🇯  Тоҷикӣ",
            "🇷🇺  Русӣ",
            "🇬🇧  English"
    };

    int BLUE = Color.rgb(45, 125, 245);
    int DARK = Color.rgb(10, 19, 40);
    int CARD = Color.rgb(25, 38, 65);
    int WHITE = Color.WHITE;

    int dp(float v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable box(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    TextView label(String text, float size) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(WHITE);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadDictionary();

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(DARK);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(dp(20), dp(25), dp(20), dp(30));

        // HEADER
        TextView icon = label("🌐", 42);
        icon.setGravity(Gravity.CENTER);

        main.addView(icon,
                new LinearLayout.LayoutParams(-1, dp(55)));

        TextView title = label("TAJIK TRANSLATOR", 27);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        main.addView(title);

        TextView subtitle = label(
                "Тоҷикӣ  •  English  •  Русский",
                15
        );
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);

        main.addView(subtitle);

        addSpace(main, 20);

        // LANGUAGE CARD
        LinearLayout langCard = new LinearLayout(this);
        langCard.setOrientation(LinearLayout.HORIZONTAL);
        langCard.setGravity(Gravity.CENTER_VERTICAL);
        langCard.setPadding(dp(8), dp(8), dp(8), dp(8));
        langCard.setBackground(box(CARD, 22));

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

        // Пешфарз: Тоҷикӣ → Русӣ
        fromLanguage.setSelection(0);
        toLanguage.setSelection(1);

        langCard.addView(
                fromLanguage,
                new LinearLayout.LayoutParams(
                        0, dp(55), 1
                )
        );

        TextView swap = label("⇄", 32);
        swap.setTextColor(BLUE);
        swap.setGravity(Gravity.CENTER);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = fromLanguage.getSelectedItemPosition();
                int b = toLanguage.getSelectedItemPosition();

                fromLanguage.setSelection(b);
                toLanguage.setSelection(a);
            }
        });

        langCard.addView(
                swap,
                new LinearLayout.LayoutParams(
                        dp(55), dp(55)
                )
        );

        langCard.addView(
                toLanguage,
                new LinearLayout.LayoutParams(
                        0, dp(55), 1
                )
        );

        main.addView(langCard);

        addSpace(main, 18);

        // INPUT TITLE
        TextView inputTitle = label("Матн", 18);
        inputTitle.setTypeface(null, Typeface.BOLD);

        main.addView(inputTitle);

        addSpace(main, 8);

        // INPUT
        LinearLayout inputCard = new LinearLayout(this);
        inputCard.setPadding(
                dp(18), dp(12), dp(18), dp(12)
        );
        inputCard.setBackground(box(CARD, 22));

        input = new EditText(this);
        input.setTextColor(WHITE);
        input.setHintTextColor(Color.rgb(170, 180, 200));
        input.setHint("Матнро ин ҷо нависед...");
        input.setTextSize(20);
        input.setGravity(Gravity.TOP);
        input.setMinHeight(dp(130));
        input.setBackgroundColor(Color.TRANSPARENT);

        inputCard.addView(
                input,
                new LinearLayout.LayoutParams(
                        -1, dp(130)
                )
        );

        main.addView(inputCard);

        addSpace(main, 16);

        // TRANSLATE BUTTON
        Button translate = new Button(this);
        translate.setText("✨  ТАРҶУМА КУН");
        translate.setTextSize(17);
        translate.setTypeface(null, Typeface.BOLD);
        translate.setTextColor(WHITE);
        translate.setAllCaps(false);
        translate.setBackground(box(BLUE, 20));

        main.addView(
                translate,
                new LinearLayout.LayoutParams(
                        -1, dp(60)
                )
        );

        addSpace(main, 18);

        // RESULT TITLE
        TextView resultTitle = label("Тарҷума", 18);
        resultTitle.setTypeface(null, Typeface.BOLD);

        main.addView(resultTitle);

        addSpace(main, 8);

        // RESULT CARD
        LinearLayout resultCard = new LinearLayout(this);
        resultCard.setOrientation(LinearLayout.VERTICAL);
        resultCard.setPadding(
                dp(18), dp(16), dp(18), dp(18)
        );
        resultCard.setBackground(box(CARD, 22));

        result = label(
                "Натиҷаи тарҷума дар ин ҷо пайдо мешавад.",
                19
        );
        result.setTextColor(Color.rgb(235, 240, 250));
        result.setPadding(0, dp(5), 0, dp(10));

        resultCard.addView(result);

        main.addView(resultCard);

        addSpace(main, 14);

        // COPY + CLEAR
        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button copy = new Button(this);
        copy.setText("📋  COPY");
        copy.setTextSize(15);
        copy.setAllCaps(false);
        copy.setTextColor(WHITE);
        copy.setBackground(box(Color.rgb(45, 55, 80), 16));

        Button clear = new Button(this);
        clear.setText("🗑  CLEAR");
        clear.setTextSize(15);
        clear.setAllCaps(false);
        clear.setTextColor(WHITE);
        clear.setBackground(box(Color.rgb(45, 55, 80), 16));

        LinearLayout.LayoutParams bp =
                new LinearLayout.LayoutParams(
                        0, dp(55), 1
                );

        bp.setMargins(0, 0, dp(6), 0);
        buttons.addView(copy, bp);

        LinearLayout.LayoutParams bp2 =
                new LinearLayout.LayoutParams(
                        0, dp(55), 1
                );

        bp2.setMargins(dp(6), 0, 0, 0);
        buttons.addView(clear, bp2);

        main.addView(buttons);

        addSpace(main, 20);

        TextView footer = label(
                "🇹🇯  Тоҷикӣ  •  🇷🇺  Русӣ  •  🇬🇧  English",
                13
        );
        footer.setTextColor(Color.GRAY);
        footer.setGravity(Gravity.CENTER);

        main.addView(footer);

        // TRANSLATE
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text =
                        input.getText().toString().trim();

                if (text.isEmpty()) {
                    result.setText("⚠️ Аввал матнро нависед.");
                    return;
                }

                String from =
                        fromLanguage.getSelectedItem().toString();

                String to =
                        toLanguage.getSelectedItem().toString();

                result.setText(
                        from + "  →  " + to +
                        "\n\n" +
                        translateText(text, from, to)
                );
            }
        });

        // COPY
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = result.getText().toString();

                ClipboardManager clipboard =
                        (ClipboardManager)
                                getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                );

                clipboard.setPrimaryClip(
                        ClipData.newPlainText(
                                "Translation",
                                text
                        )
                );

                Toast.makeText(
                        MainActivity.this,
                        "✅ Натиҷа нусха шуд",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // CLEAR
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input.setText("");

                result.setText(
                        "Натиҷаи тарҷума дар ин ҷо пайдо мешавад."
                );
            }
        });

        scroll.addView(main);
        setContentView(scroll);
    }

    private void addSpace(
            LinearLayout parent,
            int height
    ) {
        Space space = new Space(this);

        parent.addView(
                space,
                new LinearLayout.LayoutParams(
                        1, dp(height)
                )
        );
    }

    private void loadDictionary() {

        try {

            InputStream stream =
                    getAssets().open("dictionary.json");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(stream)
                    );

            StringBuilder builder =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            JSONArray array =
                    new JSONArray(builder.toString());

            for (int i = 0; i < array.length(); i++) {

                JSONObject word =
                        array.getJSONObject(i);

                String tj =
                        word.getString("tj")
                                .toLowerCase(Locale.ROOT)
                                .trim();

                String ru =
                        word.getString("ru")
                                .toLowerCase(Locale.ROOT)
                                .trim();

                String en =
                        word.getString("en")
                                .toLowerCase(Locale.ROOT)
                                .trim();

                tjToRu.put(tj, ru);
                tjToEn.put(tj, en);

                ruToTj.put(ru, tj);
                ruToEn.put(ru, en);

                enToTj.put(en, tj);
                enToRu.put(en, ru);
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "dictionary.json ёфт нашуд!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private String translateText(
            String text,
            String from,
            String to
    ) {

        String lower =
                text.toLowerCase(Locale.ROOT).trim();

        if (from.contains("Тоҷикӣ") &&
                to.contains("Русӣ"))
            return findTranslation(lower, tjToRu);

        if (from.contains("Тоҷикӣ") &&
                to.contains("English"))
            return findTranslation(lower, tjToEn);

        if (from.contains("Русӣ") &&
                to.contains("Тоҷикӣ"))
            return findTranslation(lower, ruToTj);

        if (from.contains("Русӣ") &&
                to.contains("English"))
            return findTranslation(lower, ruToEn);

        if (from.contains("English") &&
                to.contains("Тоҷикӣ"))
            return findTranslation(lower, enToTj);

        if (from.contains("English") &&
                to.contains("Русӣ"))
            return findTranslation(lower, enToRu);

        if (from.equals(to))
            return text;

        return "❌ Тарҷума ёфт нашуд.";
    }

    private String findTranslation(
            String text,
            HashMap<String, String> dictionary
    ) {

        if (dictionary.containsKey(text))
            return dictionary.get(text);

        String[] words =
                text.split("\\s+");

        StringBuilder output =
                new StringBuilder();

        for (String word : words) {

            String clean =
                    word.replaceAll(
                            "[.,!?;:()\\[\\]{}\"]",
                            ""
                    )
                    .toLowerCase(Locale.ROOT);

            String translated =
                    dictionary.get(clean);

            if (translated != null)
                output.append(translated);
            else
                output.append(word);

            output.append(" ");
        }

        String finalText =
                output.toString().trim();

        if (finalText.equals(text))
            return "❌ Ин калима дар луғат нест.";

        return finalText;
    }
            }
