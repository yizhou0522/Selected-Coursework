package com.example.user.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";//键
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private int mNumCheat = 3;//反向思维，先设定最大值
    //关键是把变量放在onActivity 里面，用户点开答案算一次，而不是点击cheat 按钮就算一次
    private TextView mCheatTextView;
    private TextView mResultTextView;
    private int mScores;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");//记录日志
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        // int question =mQuestionBank[mCurrentIndex].getTextResId();
        // mQuestionTextView.setText(question);//令textview 显示数组中的内容

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == mQuestionBank.length - 1) {
                    mNextButton.setEnabled(false);
                    mResultTextView = (TextView) findViewById(R.id.scores_text_view);
                    mResultTextView.setText("You've got " + mScores * 100.0 / mQuestionBank.length + "% correct!");

                }
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;//防indexoutofbound
                mIsCheater = false;//设默认值
                //int question=mQuestionBank[mCurrentIndex].getTextResId();
                //mQuestionTextView.setText(question);
                updateQuestion();
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);

            }
        });
        updateQuestion();
        mCheatTextView = (TextView) findViewById(R.id.cheats_text_view);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNumCheat > 0) {
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    //Intent intent=new Intent(QuizActivity.this, CheatActivity.class);
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                    //startActivity(intent);
                    startActivityForResult(intent, REQUEST_CODE_CHEAT);
                } else {
                    mCheatButton.setEnabled(false);
                }
                Log.d(TAG, Integer.toString(mNumCheat) + "xxxxxxxxxxx");
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mNumCheat--;
            mCheatTextView.setText(mNumCheat + " cheats left");
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }//代码封装以及高效率调用

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScores++;
            } else
                messageResId = R.string.incorrect_toast;
            //这里直接用this 因为他不在匿名内部类，故this 即为quizActivity
        }
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInatanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);//键值对调用put方法储存数据
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


}
