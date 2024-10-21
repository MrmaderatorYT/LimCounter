package com.ccs.limcounter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText functionEditText;
    private Button calculateButton;
    private TextView limValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        limValue = findViewById(R.id.limValue);
        functionEditText = findViewById(R.id.functionInput);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String function = functionEditText.getText().toString();

                // Регулярні вирази для різних випадків

                // 1. Конструкції виду m(x+y+...)^n, де n > 0, m > 0
                Pattern pattern1 = Pattern.compile("\\d+\\s*\\$[a-zA-Z]+(\\s*\\+\\s*[a-zA-Z]+)*\\$\\s*\\^\\s*\\d+");

                // 2. Конструкції виду 5m^n, де n, m > 0
                Pattern pattern2 = Pattern.compile("\\d+\\s*[a-zA-Z]+\\s*\\^\\s*\\d+");

                // 3. Конструкції виду m(x+y+...)^n, де n < 0, m > 0
                Pattern pattern3 = Pattern.compile("\\d+\\s*\\$[a-zA-Z]+(\\s*\\+\\s*[a-zA-Z]+)*\\$\\s*\\^\\s*-\\d+");

                // 4. Конструкції виду 5m^n, де n < 0, m > 0
                Pattern pattern4 = Pattern.compile("\\d+\\s*[a-zA-Z]+\\s*\\^\\s*-\\d+");

                // 5. Конструкції виду (x+y+...)^0 або x^0
                Pattern pattern5 = Pattern.compile("(\\$[a-zA-Z]+(\\s*\\+\\s*[a-zA-Z]+)*\\$|[a-zA-Z]+)\\s*\\^\\s*0");

                // 6. Конструкції виду k^n, де k > 1
                Pattern pattern6 = Pattern.compile("(?<!-)\\b([2-9]\\d*|\\d+\\.\\d+)\\s*\\^\\s*[a-zA-Z]+");

                // 7. Конструкції виду k^n, де k = 1
                Pattern pattern7 = Pattern.compile("\\b1\\s*\\^\\s*[a-zA-Z]+");

                // 8. Конструкції виду k^n, де 0 < k < 1
                Pattern pattern8 = Pattern.compile("\\b0\\.\\d+\\s*\\^\\s*[a-zA-Z]+");

                // 9. Конструкції виду k^n, де k ≤ -1
                Pattern pattern9 = Pattern.compile("-\\d+(\\.\\d+)?\\s*\\^\\s*[a-zA-Z]+");

                // 10. Конструкції виду x/k, де k ≠ 0
                Pattern pattern10 = Pattern.compile("[a-zA-Z]+\\s*/\\s*-?\\d+(\\.\\d+)?");

                // 11. Конструкції виду sin(x)
                Pattern patternSin = Pattern.compile("sin\\$(.*?)\\$");

                // 12. Конструкції виду cos(x)
                Pattern patternCos = Pattern.compile("cos\\$(.*?)\\$");

                // 13. Конструкції виду tg(x) або tan(x)
                Pattern patternTg = Pattern.compile("tg\\$(.*?)\\$|tan\\$(.*?)\\$");

                // 14. Конструкції виду ctg(x) або cot(x)
                Pattern patternCtg = Pattern.compile("ctg\\$(.*?)\\$|cot\\$(.*?)\\$");

                // Створення матчерів
                Matcher matcher1 = pattern1.matcher(function);
                Matcher matcher2 = pattern2.matcher(function);
                Matcher matcher3 = pattern3.matcher(function);
                Matcher matcher4 = pattern4.matcher(function);
                Matcher matcher5 = pattern5.matcher(function);
                Matcher matcher6 = pattern6.matcher(function);
                Matcher matcher7 = pattern7.matcher(function);
                Matcher matcher8 = pattern8.matcher(function);
                Matcher matcher9 = pattern9.matcher(function);
                Matcher matcher10 = pattern10.matcher(function);
                Matcher matcherSin = patternSin.matcher(function);
                Matcher matcherCos = patternCos.matcher(function);
                Matcher matcherTg = patternTg.matcher(function);
                Matcher matcherCtg = patternCtg.matcher(function);

                // Визначення результату
                if (matcherSin.find() || matcherCos.find() || matcherTg.find() || matcherCtg.find()) {
                    limValue.setText("Не підтримується функція тригонометрична");
                }
                else if (matcher9.find()) {
                    limValue.setText("Не існує (∅)");
                }
                else if (matcher5.find() || matcher7.find()) {
                    limValue.setText("1");
                }
                else if (matcher3.find() || matcher4.find() || matcher8.find() || matcher10.find()) {
                    limValue.setText("0");
                }
                else if (matcher1.find() || matcher2.find() || matcher6.find()) {
                    limValue.setText("+∞");
                }
                else {
                    limValue.setText("Не можу обчислити функцію :/");
                }
            }
        });
    }
}