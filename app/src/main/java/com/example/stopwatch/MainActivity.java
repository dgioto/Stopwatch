package com.example.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    //переменная для проверки, работал ли секундомер перед вызовом метода onStop()
    private  boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //сохраняем переменные в объект Bundle
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
    }

    //сохранить состояние секундомера, если он готовится к уничтожению
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        //сохранить информацию о томб работал ли секундомер на момент вызова метода onStop()
//        wasRunning = running;
//        running = false;
//    }

    // переносим код в метод onPause() из onStop(), что бы секундомер приостановливал
    // работу во время сварачивания приложения и приостановки
    @Override
    protected void onPause() {
        super.onPause();
        //сохранить информацию о томб работал ли секундомер на момент вызова метода onStop()
        wasRunning = running;
        running = false;
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        //если секундомер работал, то отсчет времени возобновляется
//        if (wasRunning) running = true;
//    }

    // переносим код в метод onResume() из onStart(), что бы секундомер приостановливал
    // работу во время сварачивания приложения и приостановки
    @Override
    protected void onResume() {
        super.onResume();
        //если секундомер работал, то отсчет времени возобновляется
        if (wasRunning) running = true;
    }

    public void onClickStart(View view){
        running = true;
    }

    public void onClickStop(View view){
        running = false;
    }

    public void onClickReset(View view){
        running = false;
        seconds = 0;
    }

    //обновление показаний таймера
    private void runTimer(){
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        //объект для выполнения кода в другом программном потоке
        final Handler handler = new Handler();
        //запускаем отдельный поток
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if(running) seconds++;
                //повторное выполнение кода с отсрочкой в 1 секунду
                handler.postDelayed(this, 1000);
            }
        });
    }
}