package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    Button answer;
    TextView ex, correctNumTv;
    TextView[] answ = new TextView[3];
    ArrayList<String> answT = new ArrayList<>();
    ArrayList<String> answF = new ArrayList<>();
    EditText oneEt, twoEt, threeEt;
    String[] answS = new String[3];
    int correctNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        answer = findViewById(R.id.answ);
        ex = findViewById(R.id.ex);
        answ[0] = findViewById(R.id.one);
        answ[1] = findViewById(R.id.two);
        answ[2] = findViewById(R.id.three);
        oneEt = findViewById(R.id.oneEt);
        twoEt = findViewById(R.id.twoEt);
        threeEt = findViewById(R.id.threeEt);
        correctNumTv = findViewById(R.id.numAnsw);
        String level = getIntent().getExtras().getString("Level");
        if (level.equals("hard")) ex.setText("Решите урованения. Если в уравнении больше одного корня, введите меньший");
        else ex.setText("Решите пример");
        FirebaseDatabase.getInstance().getReference().child(level).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> temp = (HashMap<String, String>) snapshot.getValue();
                for (Map.Entry entry: temp.entrySet()) answT.add(entry.getKey().toString());
                for (int i = 0; i < answ.length; i++) {
                    answ[i].setText(answT.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child(level).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,String> temp = (HashMap<String, String>) snapshot.getValue();
                        for (Map.Entry entry: temp.entrySet()) answF.add(entry.getValue().toString());
                        answS[0] = oneEt.getText().toString();
                        answS[1] = twoEt.getText().toString();
                        answS[2] = threeEt.getText().toString();
                        for (int i = 0; i < answS.length; i++) if(answS[i].equals(answF.get(i))) correctNum++;
                        correctNumTv.setText("Вы ответили правильно на " + correctNum + " из 3 вопроса");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}