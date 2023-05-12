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
    private EditText variableEditText;
    private EditText limitEditText;
    private Button calculateButton;
    private TextView resultTextView, limValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        limValue = findViewById(R.id.limValue);
        functionEditText = findViewById(R.id.functionInput);
        limitEditText = findViewById(R.id.limitInput);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String function = functionEditText.getText().toString();
                String limit = limitEditText.getText().toString();


                // Вычисляем лимит
                // задаем регулярное выражение для поиска конструкций вида m(x+y+...)^n, где n>0,m > 0
                Pattern pattern = Pattern.compile("\\d+[^\\d^]*\\([^a-zA-Z]+\\+?\\)*\\)\\^(?!0|-)\\d+");
                // задаем регулярное выражение для поиска конструкций вида 5m^n, где n,m > 0
                Pattern pattern1 = Pattern.compile("\\d*[^\\d^]*\\^(?!0)\\d+");
                // задаем регулярное выражение для поиска конструкций вида m(x+y+...)^n, где n<0,m > 0
                Pattern pattern2 = Pattern.compile("\\d*\\w*\\(((\\w+)(\\+*))+\\)\\^(?!0$)\\d+");
                // задаем регулярное выражение для поиска конструкций вида 5m^n, где n < 0,m > 0
                Pattern pattern3 = Pattern.compile("\\d*[^\\d^]*\\^(?!0$|\\d+$)-?\\d+");
                // задаем регулярное выражение для поиска конструкций вида (x+y+...)^0 или x^0
                Pattern pattern4 = Pattern.compile("\\d*\\w*\\((\\w+\\+*)*\\)\\^(-?\\d+|0)|\\d*[^\\d^]*\\^(-?\\d+|0)");
                // когда  конструкция вида k^n, k > 1
                Pattern pattern5 = Pattern.compile("(?<![-\\d])((?!1\\b)\\d+(\\.\\d+)?|\\d*\\.\\d+)\\s*\\^\\s*[a-z]+\\b");
                // когда  конструкция вида k^n, k = 1
                Pattern pattern6 = Pattern.compile("(![-.\\d])1\\s*\\^\\s*[a-z]+\\b");
                // когда  конструкция вида k^n, -1 < k < 1
                Pattern pattern7 = Pattern.compile("(?<![-.\\d])0*(?:\\.\\d+|[01](?:\\.\\d+)?)(?![.\\d])\\s*\\^\\s*[a-z]+\\b");
                // когда  конструкция вида k^n, k меньше равна -1
                Pattern pattern8 = Pattern.compile("-([1-9]\\d*\\.?\\d*|0\\.0*[1-9]\\d*)\\s*\\^\\s*[a-z]+");
                // когда типо такойй вид  (a_n*m^k+a_(n-1)*m^(k-1)+...+a_0) искала самый большой и самый маленькую степень и искала произведение этих 2х чисел
                Pattern pattern9 = Pattern.compile("\\((\\s*[+-]?\\s*(\\d*\\.\\d+|\\d+)_?[a-zA-Z]\\^\\s*[+-]?\\d+\\s*)+\\)\\s*\\.{3}");
                //(a_k*n^k+a_(k-1)*n^(k-1)+...+a_1*n+a_0)/(b_p*n^p+b_(p-1)*n^(n-1)+...+b_p*n+b_0) получало самую большую степень числа n и умножало на самое маленькое число n, а потом делило на  самую большую степень числа n и умножало на самое маленькое число b и если k < p, то получаем 0
//                Pattern pattern10 = Pattern.compile("\\((\\s*[+-]?\\s*(\\d*\\.\\d+|\\d+)_?[a-zA-Z]\\^(\\s*[+-]?\\d+)\\s*)+\\)\\s*\\.{3}\\s*(k\\s*>\\s*p|k\\s*=\\s*0|k\\s*<\\s*0\\s+and\\s+(\\s*k\\s*<=\\s*(p-1)\\s+or\\s+k\\s*>=\\s*(-1)\\s*))");
//                //(a_k*n^k+a_(k-1)*n^(k-1)+...+a_1*n+a_0)/(b_p*n^p+b_(p-1)*n^(n-1)+...+b_p*n+b_0) получало самую большую степень числа n и умножало на самое маленькое число n, а потом делило на  самую большую степень числа n и умножало на самое маленькое число b и если k = p, то делим a^k на b^p
//                Pattern pattern11 = Pattern.compile("(?<=/)\\d*[^\\d^]*\\((\\w+\\+*)+\\)\\^(\\d*[1-9]\\d*\\.?\\d*|0\\.\\d*[1-9]\\d*)\\b");
//                //(a_k*n^k+a_(k-1)*n^(k-1)+...+a_1*n+a_0)/(b_p*n^p+b_(p-1)*n^(n-1)+...+b_p*n+b_0) получало самую большую степень числа n и умножало на самое маленькое число n, а потом делило на  самую большую степень числа n и умножало на самое маленькое число b и если k < p, то получаем +/- бесконечность
//                Pattern pattern12 = Pattern.compile("(?<=\\^)\\d*[1-9]\\d*\\.?\\d*(?=\\b)");
                //для типа x/k, где k є 0
                Pattern pattern13 = Pattern.compile("\\d+\\D+\\/\\D+");
                        Pattern patternSin = Pattern.compile("sin\\((.*?)\\)");

                // Нахождение cos(x), где x - выражение в скобках
                Pattern patternCos = Pattern.compile("cos\\((.*?)\\)");

                // Нахождение tg(x), где x - выражение в скобках
                Pattern patternTg = Pattern.compile("tg\\((.*?)\\)|tan\\((.*?)\\)");

                // Нахождение ctg(x), где x - выражение в скобках
                Pattern patternCtg = Pattern.compile("ctg\\((.*?)\\)|cot\\((.*?)\\)");


                Matcher matcher = pattern.matcher(function);
                Matcher matcher1 = pattern1.matcher(function);
                Matcher matcher2 = pattern2.matcher(function);
                Matcher matcher3 = pattern3.matcher(function);
                Matcher matcher4 = pattern4.matcher(function);
                Matcher matcher5 = pattern5.matcher(function);
                Matcher matcher6 = pattern6.matcher(function);
                Matcher matcher7 = pattern7.matcher(function);
                 Matcher matcher8 = pattern8.matcher(function);
                Matcher matcher9 = pattern9.matcher(function);
//                Matcher matcher10 = pattern10.matcher(function);
//                Matcher matcher11 = pattern11.matcher(function);
//                Matcher matcher12 = pattern12.matcher(function);
                Matcher matcher13 = patternCos.matcher(function);
                Matcher matcher14 = patternSin.matcher(function);
                Matcher matcher15 = patternTg.matcher(function);
                Matcher matcher16 = patternCtg.matcher(function);
                Matcher matcher17 = pattern13.matcher(function);


                    // Отображаем результат на экране
                    if (matcher.find() || matcher1.find() || matcher5.find()) {
                        // если есть, выводим знак бесконечности
                        limValue.setText("+ \u221E");
                    }
                    else if (matcher2.find() || matcher3.find() || matcher7.find() || matcher9.find() || matcher17.find()) {
                        limValue.setText("0");
                    } else if (matcher4.find() || matcher6.find()) {
                        limValue.setText("1");
                    } else if (matcher8.find()) {
                        limValue.setText("∅");
                    } else {
                        limValue.setText("Very interesting function because I can not resolve it :/");
                    }}



        });


    }


}
