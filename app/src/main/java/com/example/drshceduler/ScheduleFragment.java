package com.example.drshceduler;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import static com.example.drshceduler.DataActivity.subjectListFull;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;


public class ScheduleFragment extends Fragment {
    private Button btnCalendar;
    private Calendar c = Calendar.getInstance();
    private String selectedDay, selectedDate, nameOfDay;
    private ArrayList<Subject> onDaySchedule = new ArrayList<>();
    private ListView subjectView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Масив для формування назв днів
        String[] dayName = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] dayName_uk = {"Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"};

        btnCalendar = view.findViewById(R.id.btnCalendar);
        subjectView = view.findViewById(R.id.lvwSchedule);

        // будуємо календар для вибору дати
        MaterialDatePicker.Builder calBuilder = MaterialDatePicker.Builder.datePicker();
        calBuilder.setTitleText(R.string.calendar_title);
        final MaterialDatePicker materialDatePicker = calBuilder.build();

        //опрацьовувач події натискання кнопки з датою - виводимо календар
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
            }
        });

        //опрацьовувач події натискання кнопки Ok на календарі
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Log.d("Create schedule:", String.valueOf(selection));
                c.setTimeInMillis((Long) selection);
                selectedDay = dayName[c.get(Calendar.DAY_OF_WEEK) - 1];
                if(Locale.getDefault().getLanguage().equals("uk")){
                    nameOfDay = dayName_uk[c.get(Calendar.DAY_OF_WEEK) - 1];
                }
                else{
                    nameOfDay = selectedDay;
                }
                selectedDate = String.format("%s.%s.%s", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                btnCalendar.setText(String.format("%s %s", nameOfDay, selectedDate));
                formDaySchedule();
            }
        });

        //визначаємо день поточної дати та поточну дату
        selectedDay = dayName[c.get(Calendar.DAY_OF_WEEK) - 1];

        if(Locale.getDefault().getLanguage().equals("uk")){
            nameOfDay = dayName_uk[c.get(Calendar.DAY_OF_WEEK) - 1];
        }
        else{
            nameOfDay = selectedDay;
        }
        selectedDate = String.format("%s.%s.%s", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));

        btnCalendar.setText(String.format("%s %s", nameOfDay, selectedDate));

        formDaySchedule();
    }


    private void formDaySchedule(){
        //масив римських цифр для порядкового номеру предмету
        String[] romeNumber = {"I", "II", "III", "IV", "V", "VI", "VII"};
        //очищаємо масив від попередніх даних
        onDaySchedule.clear();
        //для визначення парності тижня
        int odd_even = c.get(Calendar.WEEK_OF_MONTH) % 2;
        int i = 0;
        //перебираємо всі предмети загального розкладу групи юзера
        for (Subject subject : subjectListFull) {
            System.out.println(subject);
            //умова для перевірки відповідності предмета даному тижню
            boolean checkWeek = (subject.getWeek().equals("Odd") && odd_even == 0) ||
                    (subject.getWeek().equals("Even") && odd_even == 1);
            if(selectedDay.equals(subject.getDay()) && !checkWeek){
                //Додаємо номер предмету
                subject.setNumber(romeNumber[i]);
                i++;
                //заносимо предмет в денний розклад
                onDaySchedule.add(subject);
            }
            //Створюємо адаптер для конвертації масиву до views
            ScheduleAdapter adapter = new ScheduleAdapter(this, onDaySchedule);
            //Прикріпляємо адаптер до ListView
            subjectView.setAdapter(adapter);
        }
    }

    public class ScheduleAdapter extends ArrayAdapter<Subject> {
        public ScheduleAdapter(ScheduleFragment context, ArrayList<Subject> subjects) {
            super(context.getContext(), 0, subjects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // метод для отримання власного View для адаптера
            Subject subject = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_of_subjects, parent, false);
            }

            TextView txtvSubName = convertView.findViewById(R.id.txtvSubSubject);
            TextView txtvSubTeacher = convertView.findViewById(R.id.txtvSubTeacher);
            TextView txtvSubTime = convertView.findViewById(R.id.txtvTime);
            TextView txtvSubType = convertView.findViewById(R.id.txtvSubType);
            TextView txtvSubCabinet = convertView.findViewById(R.id.txtvSubCabinet);
            TextView txtvSubLink = convertView.findViewById(R.id.txtvSubLink);
            TextView txtvSubNumber = convertView.findViewById(R.id.txtvSubNumber);

            txtvSubName.setText(subject.getSubject());
            txtvSubTeacher.setText(subject.getTeacher());
            txtvSubTime.setText(subject.getTime());
            txtvSubType.setText(subject.getType());
            txtvSubCabinet.setText(subject.getCabinet());
            txtvSubLink.setText(subject.getLink());
            txtvSubLink.setPaintFlags(txtvSubLink.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            txtvSubNumber.setText(subject.getNumber());

            txtvSubLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("===== Selected Link:", String.valueOf(txtvSubLink.getText()));
                    Uri uri = Uri.parse(String.valueOf(txtvSubLink.getText()));
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }

            });

            return convertView;
        }
    }
}