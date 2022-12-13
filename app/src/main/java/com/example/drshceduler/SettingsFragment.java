package com.example.drshceduler;

import static com.example.drshceduler.AuthActivity.dbUser;
import static com.example.drshceduler.AuthActivity.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.drshceduler.DataActivity.subjectListFull;

public class SettingsFragment extends Fragment {
    private RadioButton rbtnDark, rbtnLight, rbtnLang1, rbtnLang2;
    private RadioGroup rdgrTheme, rdgrLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rdgrTheme = view.findViewById(R.id.rdgrTheme);
        rdgrLng = view.findViewById(R.id.rdgrLng);
        rbtnDark = view.findViewById(R.id.rbtnDark);
        rbtnLight = view.findViewById(R.id.rbtnLight);
        rbtnLang1 = view.findViewById(R.id.rbtnLang1);
        rbtnLang2 = view.findViewById(R.id.rbtnLang2);

        //встановлюємо властивості віджетів у відповідності до параметрів із база даних
        rbtnDark.setChecked(student.isThemeDark());
        rbtnLight.setChecked(!student.isThemeDark());
        switch(student.getLanguage()){
            case "en":
                rbtnLang1.setChecked(false);
                rbtnLang2.setChecked(true);
                break;
            default:
                rbtnLang1.setChecked(true);
                rbtnLang2.setChecked(false);
                break;
        }


        rdgrTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //опрацьовуємо подію зміни теми
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbtnDark){
                    dbUser.child(student.getUserID()).child("ThemeDark").setValue(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    dbUser.child(student.getUserID()).child("ThemeDark").setValue(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

            }
        });

        rdgrLng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //опрацьовуємо подію зміни мови
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbtnLang2:
                        dbUser.child(student.getUserID()).child("Language").setValue("en");
                        break;
                    default:
                        dbUser.child(student.getUserID()).child("Language").setValue("uk");
                        break;
                }
                // після зміни мови перечитуємо відповідний розклад
                readScheduleData();
            }
        });

    }

    private void readScheduleData(){
        //метод для зчитування розкладу юзера

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