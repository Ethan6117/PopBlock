package com.example.ethan.popstar;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChooseDifficultActivity extends Activity {
    private RadioGroup GroupDiff;
    private Button choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficult);
        final Intent intent=new Intent(this,MainActivity.class);
        GroupDiff=(RadioGroup)findViewById(R.id.groupDiff);
        choose= (Button) findViewById(R.id.buttonChoose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        GroupDiff.setOnCheckedChangeListener(myListener);
    }

    private RadioGroup.OnCheckedChangeListener myListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radioButtonEasy)
                MainActivity.diff = 0;
            else if (checkedId == R.id.radioButtonSoso)
                MainActivity.diff = 1;
            else if (checkedId == R.id.radioButtonDifficult)
                MainActivity.diff = 2;
        }
    };
}
