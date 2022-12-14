package com.example.drshceduler;

import static com.example.drshceduler.AuthActivity.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class AccountFragment extends Fragment {
    private TextView txtvFullName, txtvStudentID, txtvFaculty, txtvSpeciality, txtvTerm,
            txtvGroup, txtvFoS, txtvToS, txtvEmail;
    private ImageView imPhoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtvFullName = view.findViewById(R.id.txtvFullName);
        txtvStudentID = view.findViewById(R.id.txtvStudentID);
        txtvFaculty = view.findViewById(R.id.txtvFaculty);
        txtvSpeciality = view.findViewById(R.id.txtvSpecialty);
        txtvGroup = view.findViewById(R.id.txtvGroup);
        txtvTerm = view.findViewById(R.id.txtvTerm);
        txtvFoS = view.findViewById(R.id.txtvFoS);
        txtvToS = view.findViewById(R.id.txtvToS);
        txtvEmail = view.findViewById(R.id.txtvEmail);
        imPhoto = view.findViewById(R.id.imvPhoto);

        txtvFullName.setText(student.getFullName());
        txtvStudentID.setText(student.getStudentId());
        txtvFaculty.setText(student.getFaculty());
        txtvSpeciality.setText(student.getSpecialty());
        txtvGroup.setText(student.getGroup());
        txtvTerm.setText(student.getTerm());
        txtvFoS.setText(student.getFormOfEducation());
        txtvToS.setText(student.getTermOfEducation());
        txtvEmail.setText(student.getEmail());

        // Завантажуємо фотографію користувача із сховища Firebase
        StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("Photo");
        StorageReference photo = photoRef.child(student.getUserID() + ".jpg");

        try {
            final File localFile = File.createTempFile("photo", "jpg");
            photo.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap photoBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imPhoto.setImageBitmap(photoBitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // якщо не вдалося завантажити фотографію, відображаємо анонімну
                    imPhoto.setImageResource(R.drawable.anonymous);
                    Toast.makeText(view.getContext(), "Error download photo", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}