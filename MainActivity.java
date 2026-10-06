package com.example.tajiktranslator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 40, 30, 30);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("🌐 TAJIK TRANSLATOR");
        title.setTextSize(26);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Тоҷикӣ • Русӣ • English");
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        layout.addView(subtitle);

        Spinner from = new Spinner(this);
        String[] languages = {"Тоҷикӣ", "Русӣ", "English"};
        from.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languages
        ));
        layout.addView(from);

        EditText input = new EditText(this);
        input.setHint("Матнро нависед...");
        input.setTextSize(18);
        input.setMinHeight(150);
        layout.addView(input);

        Button translate = new Button(this);
        translate.setText("ТАРҶУМА КУН");
        layout.addView(translate);

        TextView result = new TextView(this);
        result.setText("Натиҷаи тарҷума ин ҷо пайдо мешавад...");
        result.setTextSize(18);
        result.setPadding(10, 30, 10, 10);
        layout.addView(result);

        translate.setOnClickListener(v -> {
            String text = input.getText().toString();

            if (text.isEmpty()) {
                result.setText("Аввал матнро нависед.");
            } else {
                result.setText("Матн қабул шуд:\n\n" + text);
            }
        });

        setContentView(layout);
    }
  }
