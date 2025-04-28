package com.android.hw_converter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //    поле для ввода пользователя
    private EditText user_input_view;
    //    поля с результами
    private TextView res_ton, res_kg, res_gr;
    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// Получаем ссылки на объекты
        user_input_view = findViewById(R.id.user_input_view);
        res_ton = findViewById(R.id.res_ton);
        res_kg = findViewById(R.id.res_kg);
        res_gr = findViewById(R.id.res_gr);
        dropdown = findViewById(R.id.dropDown);

// Список всех элементов Spinner
        String[] items = new String[]{"Тонны", "Килограммы", "Граммы"};
//        подключаем свой стиль для spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);

// Устанавливаем адаптер для выпадающего списка
        dropdown.setAdapter(adapter);
// При старте программы будет выбран первый элемент из списка
        dropdown.setSelection(0);

//        отслеживания ввода
        user_input_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                вызываем метод
                convertWeight();
            }
        });

//        обработчик изменений Spinner
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                вызываем метод, если изменили выбор
                convertWeight();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetResults();
            }
        });
    }

    //    метод конвертации
    @SuppressLint("SetTextI18n")
    private void convertWeight() {
        try {
            String inputString = user_input_view.getText().toString();
//            если не ввел
            if (inputString.isEmpty()) {
                resetResults();
                return;
            }
//      приводим к float
            float user_number = Float.parseFloat(inputString);
//        выбор значений спиннера
            int selectedIndex = dropdown.getSelectedItemPosition();

//        тонны
//        String.valueOf(user_number) - будут лишние нули, поэтому отдельный метод
            if (selectedIndex == 0) {
                res_ton.setText(removeZeros(user_number));
                res_kg.setText(removeZeros(user_number * 1000));
                res_gr.setText(removeZeros(user_number * 1000 * 1000));
            }
            //        килограммы
            else if (selectedIndex == 1) {
                res_ton.setText(removeZeros(user_number / 1000));
                res_kg.setText(removeZeros(user_number));
                res_gr.setText(removeZeros(user_number * 1000));
            }
            //        граммы
            else {
                res_ton.setText(removeZeros(user_number / 1000 / 1000));
                res_kg.setText(removeZeros(user_number / 1000));
                res_gr.setText(removeZeros(user_number));
            }
        } catch (NumberFormatException e) {
            resetResults();
            Toast.makeText(this, "Введите корректоное число", Toast.LENGTH_LONG).show();
        }
    }

//    метод для удаления лишних нулей
    private String removeZeros (float number) {
        String str = String.valueOf(number);
            return str.contains(".") ?
                    str.replaceAll("0*$", "")
                            .replaceAll("\\.$", "") : str;
    }

//  обнуление результатов
    private void resetResults() {
        res_ton.setText("0");
        res_kg.setText("0");
        res_gr.setText("0");
    }
}

