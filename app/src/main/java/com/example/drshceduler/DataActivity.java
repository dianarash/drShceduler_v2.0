package com.example.drshceduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.drshceduler.AuthActivity.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    public static ArrayList<Subject> subjectListFull;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ImageView logo = findViewById(R.id.imvLogo);
        int nightModeFlags =  getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // в залежності від теми виводимо відповідний логотип
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                logo.setImageResource(R.drawable.logo_white);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                logo.setImageResource(R.drawable.logo_black);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
        // запускаємо метод зчитування розкладу даного користувача
        readScheduleData();
    }

    public boolean onTouchEvent(MotionEvent event) {
        //при натисканні переходимо до ScheduleActivity
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
        return super.onTouchEvent(event);
    }

    private void readScheduleData(){
        //метод для зчитування розкладу користувача

        subjectListFull = new ArrayList<>();   //масив для зберігання повного розкладу юзера
        //отримуємо з бази даних вітку Schedule
        DatabaseReference dbScheduleFull = FirebaseDatabase.getInstance().getReference().child("Schedule");
        dbScheduleFull.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Group:", student.getGroup());
                //очищаємо масив
                subjectListFull.clear();
                if (dataSnapshot.exists()) {
                    if(student.getLanguage().equals("en")){
                        for(DataSnapshot ds : dataSnapshot.child(student.getGroup() + "_en").getChildren()) {
                            Subject subject = ds.getValue(Subject.class);
                            subjectListFull.add(subject);
                        }
                    }
                    else{
                        for(DataSnapshot ds : dataSnapshot.child(student.getGroup()).getChildren()) {
                            Subject subject = ds.getValue(Subject.class);
                            subjectListFull.add(subject);
                        }
                    }
                    Log.d(" -- Create schedule:",  String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}