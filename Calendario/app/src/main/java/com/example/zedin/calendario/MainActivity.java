package com.example.zedin.calendario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange ( CalendarView view , int year , int month ,
                                              int dayOfMonth){
                Toast.makeText( getApplicationContext() , dayOfMonth + "/" + month + "/" +
                year , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
