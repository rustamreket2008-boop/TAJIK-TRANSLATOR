package com.example.tajiktranslator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
    Spinner fromLanguage;
    Spinner toLanguage;

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

    int dp(float v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable bg(int color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    TextView text(String value, float size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadDictionary();

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(244, 247, 252));

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(dp(18), dp(22), dp(18), dp(30));

        // HEADER
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(20), dp(24), dp(20), dp(24));
        header.setBackground(bg(Color.rgb(35, 105, 220), 28));

        TextView icon = text("🌐", 42, Color.WHITE);
        icon.setGravity(Gravity.CENTER);
        header.addView(icon);

        TextView title = text("TAJIK TRANSLATOR", 25, Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        header.addView(title);

        TextView sub = text("Тоҷикӣ • Русӣ • English", 14, Color.WHITE);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, dp(7), 0, 0);
        header.addView(sub);

        main.addView(header);

        Space s1 = new Space(this);
        main.addView(s1, new LinearLayout.LayoutParams(1, dp(18)));

        // LANGUAGE CARD
        LinearLayout langCard = new LinearLayout(this);
        langCard.setOrientation(LinearLayout.HORIZONTAL);
        langCard.setGravity(Gravity.CENTER_VERTICAL);
        langCard.setPadding(dp(10), dp(10), dp(10), dp(10));
        langCard.setBackground(bg(Color.WHITE, 22));

        fromLanguage = new Spinner(this);
        toLanguage = new Spinner(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languages
        );

        fromLanguage.setAdapter(adapter);
        toLanguage.setAdapter(adapter);
        toLanguage.setSelection(1);

        langCard.addView(
                fromLanguage,
                new LinearLayout.LayoutParams(0, dp(55), 1)
        );

        TextView swap = text("⇄", 28, Color.rgb(35, 105, 220));
        swap.setGravity(Gravity.CENTER);

        langCard.addView(
                swap,
                new LinearLayout.LayoutParams(dp(50), dp(55))
        );

        langCard.addView(
                toLanguage,
                new LinearLayout.LayoutParams(0, dp(55), 1)
        );

        main.addView(langCard);

        Space s2 = new Space(this);
        main.addView(s2, new LinearLayout.LayoutParams(1, dp(18)));

        // INPUT
        LinearLayout inputCard = new LinearLayout(this);
        inputCard.setOrientation(LinearLayout.VERTICAL);
        inputCard.setPadding(dp(18), dp(16), dp(18), dp(16));
        inputCard.setBackground(bg(Color.WHITE, 22));

        TextView inputLabel = text("✍️  Матни шумо", 16, Color.rgb(35, 45, 60));
        inputLabel.setTypeface(null, Typeface.BOLD);
        inputCard.addView(inputLabel);

        input = new EditText(this);
        input.setHint("Матнро ин ҷо нависед...");
        input.setTextSize(18);
        input.setGravity(Gravity.TOP);
        input.setMinHeight(dp(140));
        input.setPadding(dp(5), dp(15), dp(5), dp(5));
        input.setBackgroundColor(Color.TRANSPARENT);

        inputCard.addView(input);

        main.addView(inputCard);

        Space s3 = new Space(this);
        main.addView(s3, new LinearLayout.LayoutParams(1, dp(16)));

        // BUTTON
        Button translate = new Button(this);
        translate.setText("ТАРҶУМА КУН   →");
        translate.setTextSize(17);
        translate.setTypeface(null, Typeface.BOLD);
        translate.setTextColor(Color.WHITE);
        translate.setAllCaps(false);
        translate.setBackground(bg(Color.rgb(35, 105, 220), 20));

        main.addView(
                translate,
                new LinearLayout.LayoutParams(-1, dp(60))
        );

        Space s4 = new Space(this);
        main.addView(s4, new LinearLayout.LayoutParams(1, dp(16)));

        // RESULT
        LinearLayout resultCard = new LinearLayout(this);
        resultCard.setOrientation(LinearLayout.VERTICAL);
        resultCard.setPadding(dp(18), dp(16), dp(18), dp(22));
        resultCard.setBackground(bg(Color.WHITE, 22));

        TextView resultLabel = text("✨  Натиҷа", 16, Color.rgb(35, 45, 60));
        resultLabel.setTypeface(null, Typeface.BOLD);
        resultCard.addView(resultLabel);

        result = text(
                "Натиҷаи тарҷума дар ин ҷо пайдо мешавад...",
                18,
                Color.DKGRAY
        );
        result.setPadding(0, dp(18), 0, 0);

        resultCard.addView(result);

        main.addView(resultCard);

        Space s5 = new Space(this);
        main.addView(s5, new LinearLayout.LayoutParams(1, dp(20)));

        TextView footer = text(
                "🇹🇯  Барои забонҳои тоҷикӣ, русӣ ва англисӣ",
                13,
                Color.GRAY
        );
        footer.setGravity(Gravity.CENTER);

        main.addView(footer);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = input.getText().toString().trim();

                if (value.isEmpty()) {
                    result.setText("⚠️ Аввал матнро нависед.");
                    return;
                }

                String from = fromLanguage.getSelectedItem().toString();
                String to = toLanguage.getSelectedItem().toString();

                String answer = translateText(value, from, to);

                result.setText(
                        from + "  →  " + to +
                        "\n\n" +
                        answer
                );
            }
        });

        scroll.addView(main);
        setContentView(scroll);
    }

    private void loadDictionary() {

        try {

            InputStream stream =
                    getAssets().open("dictionary.json");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(stream)
                    );

            StringBuilder builder = new StringBuilder();
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

        if (from.contains("Тоҷикӣ") && to.contains("Русӣ"))
            return findTranslation(lower, tjToRu);

        if (from.contains("Тоҷикӣ") && to.contains("English"))
            return findTranslation(lower, tjToEn);

        if (from.contains("Русӣ") && to.contains("Тоҷикӣ"))
            return findTranslation(lower, ruToTj);

        if (from.contains("Русӣ") && to.contains("English"))
            return findTranslation(lower, ruToEn);

        if (from.contains("English") && to.contains("Тоҷикӣ"))
            return findTranslation(lower, enToTj);

        if (from.contains("English") && to.contains("Русӣ"))
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

        String[] words = text.split("\\s+");

        StringBuilder output =
                new StringBuilder();

        for (String word : words) {

            String clean =
                    word.replaceAll(
                            "[.,!?;:()\\[\\]{}\"]",
                            ""
                    ).toLowerCase(Locale.ROOT);

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
