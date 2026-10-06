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
    HashMap<String, String> enToTj = new HashMap<>();
    HashMap<String, String> ruToEn = new HashMap<>();
    HashMap<String, String> enToRu = new HashMap<>();

    String[] languages = {
            "🇹🇯  Тоҷикӣ",
            "🇷🇺  Русӣ",
            "🇬🇧  English"
    };

    int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable box(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadDictionary();

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(245, 247, 250));

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(dp(20), dp(25), dp(20), dp(30));

        TextView title = new TextView(this);
        title.setText("🌐 TAJIK TRANSLATOR");
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.rgb(25, 35, 50));
        title.setGravity(Gravity.CENTER);
        main.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Тоҷикӣ  •  Русӣ  •  English");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.GRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(6), 0, dp(22));
        main.addView(subtitle);

        LinearLayout languageCard = new LinearLayout(this);
        languageCard.setOrientation(LinearLayout.HORIZONTAL);
        languageCard.setGravity(Gravity.CENTER_VERTICAL);
        languageCard.setPadding(dp(12), dp(12), dp(12), dp(12));
        languageCard.setBackground(box(Color.WHITE, 22));

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

        languageCard.addView(
                fromLanguage,
                new LinearLayout.LayoutParams(0, dp(55), 1)
        );

        TextView arrow = new TextView(this);
        arrow.setText("⇄");
        arrow.setTextSize(25);
        arrow.setTextColor(Color.rgb(40, 100, 220));
        arrow.setGravity(Gravity.CENTER);

        languageCard.addView(
                arrow,
                new LinearLayout.LayoutParams(dp(55), dp(55))
        );

        languageCard.addView(
                toLanguage,
                new LinearLayout.LayoutParams(0, dp(55), 1)
        );

        main.addView(languageCard);

        Space space1 = new Space(this);
        main.addView(space1, new LinearLayout.LayoutParams(1, dp(18)));

        LinearLayout inputCard = new LinearLayout(this);
        inputCard.setOrientation(LinearLayout.VERTICAL);
        inputCard.setPadding(dp(18), dp(15), dp(18), dp(15));
        inputCard.setBackground(box(Color.WHITE, 22));

        TextView inputTitle = new TextView(this);
        inputTitle.setText("Матни шумо");
        inputTitle.setTextSize(16);
        inputTitle.setTypeface(null, Typeface.BOLD);
        inputTitle.setTextColor(Color.rgb(40, 50, 65));
        inputCard.addView(inputTitle);

        input = new EditText(this);
        input.setHint("Матнро ин ҷо нависед...");
        input.setTextSize(18);
        input.setGravity(Gravity.TOP);
        input.setPadding(dp(5), dp(15), dp(5), dp(5));
        input.setMinHeight(dp(150));
        input.setBackgroundColor(Color.TRANSPARENT);
        inputCard.addView(input);

        main.addView(inputCard);

        Space space2 = new Space(this);
        main.addView(space2, new LinearLayout.LayoutParams(1, dp(18)));

        Button translate = new Button(this);
        translate.setText("ТАРҶУМА КУН  →");
        translate.setTextSize(17);
        translate.setTypeface(null, Typeface.BOLD);
        translate.setTextColor(Color.WHITE);
        translate.setGravity(Gravity.CENTER);
        translate.setBackground(box(Color.rgb(35, 105, 220), 20));

        main.addView(
                translate,
                new LinearLayout.LayoutParams(-1, dp(62))
        );

        Space space3 = new Space(this);
        main.addView(space3, new LinearLayout.LayoutParams(1, dp(18)));

        LinearLayout resultCard = new LinearLayout(this);
        resultCard.setOrientation(LinearLayout.VERTICAL);
        resultCard.setPadding(dp(18), dp(15), dp(18), dp(20));
        resultCard.setBackground(box(Color.WHITE, 22));

        TextView resultTitle = new TextView(this);
        resultTitle.setText("Натиҷаи тарҷума");
        resultTitle.setTextSize(16);
        resultTitle.setTypeface(null, Typeface.BOLD);
        resultTitle.setTextColor(Color.rgb(40, 50, 65));
        resultCard.addView(resultTitle);

        result = new TextView(this);
        result.setText("Тарҷумаи шумо дар ин ҷо пайдо мешавад...");
        result.setTextSize(18);
        result.setTextColor(Color.DKGRAY);
        result.setPadding(0, dp(18), 0, 0);
        resultCard.addView(result);

        main.addView(resultCard);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = input.getText().toString().trim();

                if (text.isEmpty()) {
                    result.setText("⚠️ Аввал матнро нависед.");
                    return;
                }

                String from = fromLanguage.getSelectedItem().toString();
                String to = toLanguage.getSelectedItem().toString();

                String translated = translateText(text, from, to);

                result.setText(
                        "🔄 " + from + "  →  " + to +
                        "\n\n" +
                        translated
                );
            }
        });

        scroll.addView(main);
        setContentView(scroll);
    }

    private void loadDictionary() {

        try {

            InputStream inputStream = getAssets().open("dictionary.json");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream)
            );

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            JSONArray array = new JSONArray(builder.toString());

            for (int i = 0; i < array.length(); i++) {

                JSONObject word = array.getJSONObject(i);

                String tj = word.getString("tj").toLowerCase(Locale.ROOT).trim();
                String ru = word.getString("ru").toLowerCase(Locale.ROOT).trim();
                String en = word.getString("en").toLowerCase(Locale.ROOT).trim();

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

    private String translateText(String text, String from, String to) {

        String lowerText = text.toLowerCase(Locale.ROOT).trim();

        if (from.contains("Тоҷикӣ") && to.contains("Русӣ")) {
            return findTranslation(lowerText, tjToRu);
        }

        if (from.contains("Тоҷикӣ") && to.contains("English")) {
            return findTranslation(lowerText, tjToEn);
        }

        if (from.contains("Русӣ") && to.contains("Тоҷикӣ")) {
            return findTranslation(lowerText, ruToTj);
        }

        if (from.contains("Русӣ") && to.contains("English")) {
            return findTranslation(lowerText, ruToEn);
        }

        if (from.contains("English") && to.contains("Тоҷикӣ")) {
            return findTranslation(lowerText, enToTj);
        }

        if (from.contains("English") && to.contains("Русӣ")) {
            return findTranslation(lowerText, enToRu);
        }

        if (from.equals(to)) {
            return text;
        }

        return "❌ Барои ин тарҷума калима ёфт нашуд.";
    }

    private String findTranslation(
            String text,
            HashMap<String, String> dictionary
    ) {

        if (dictionary.containsKey(text)) {
            return dictionary.get(text);
        }

        String[] words = text.split("\\s+");
        StringBuilder resultText = new StringBuilder();

        for (String word : words) {

            String clean = word
                    .replaceAll("[.,!?;:()\\[\\]{}\"]", "")
                    .toLowerCase(Locale.ROOT);

            String translated = dictionary.get(clean);

            if (translated != null) {
                resultText.append(translated);
            } else {
                resultText.append(word);
            }

            resultText.append(" ");
        }

        String finalText = resultText.toString().trim();

        if (finalText.equals(text)) {
            return "❌ Ин калима дар луғат ёфт нашуд.";
        }

        return finalText;
    }
          }
