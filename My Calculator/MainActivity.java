package com.example.calculatornewversion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class MainActivity extends Activity {

    TextView header, result;

    Button one, two, three, four, five, six, seven, eight, nine,zero, plus, minus, multiply, division,delete,clr,equal;
    boolean isEqualClick = false;
    private String op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.calc_header);
        result = findViewById(R.id.calc_result);
        one = findViewById(R.id.btn1);
        two= findViewById(R.id.btn2);
        three= findViewById(R.id.btn3);
        four= findViewById(R.id.btn4);
        five=findViewById(R.id.btn5);
        six=findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight= findViewById(R.id.btn8);
        nine=findViewById(R.id.btn9);
        zero= findViewById(R.id.btn0);
        plus= findViewById(R.id.btn_plus);
        minus= findViewById(R.id.btn_minus);
        multiply=findViewById(R.id.btn_multiply);
        division= findViewById(R.id.btn_div);
        delete= findViewById(R.id.delete_btn);
        clr= findViewById(R.id.btn_AC);
        equal=findViewById(R.id.btn_equal);

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendNumber(((Button) v).getText().toString());
            }
        };
        one.setOnClickListener(numberClickListener);
        two.setOnClickListener(numberClickListener);
        three.setOnClickListener(numberClickListener);
        four.setOnClickListener(numberClickListener);
        five.setOnClickListener(numberClickListener);
        six.setOnClickListener(numberClickListener);
        seven.setOnClickListener(numberClickListener);
        eight.setOnClickListener(numberClickListener);
        nine.setOnClickListener(numberClickListener);
        zero.setOnClickListener(numberClickListener);

        plus.setOnClickListener(v -> appendOperator("+"));
        minus.setOnClickListener(v -> appendOperator("-"));
        multiply.setOnClickListener(v -> appendOperator("x"));
        division.setOnClickListener(v -> appendOperator("÷"));


        clr.setOnClickListener(view -> {
            header.setText("");
            result.setText("");
        });

        equal.setOnClickListener(v -> Result());
        delete.setOnClickListener(v -> deleteLastChar());
    }
    private void appendNumber(String number){
        header.append(number);
    }
    private void appendOperator(String operator){
        if(!header.getText().toString().isEmpty() && !isLastCharOperator()){
            op=operator;
            header.append(op);
        }else{
            Toast.makeText(this,"Invalid placement of operator",Toast.LENGTH_SHORT).show();
        }
    }

    private void Result(){
        String input = header.getText().toString();
        if(input.isEmpty() || op==null || !input.contains(op)){
            Toast.makeText(this,"Error: Invalid Input",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            String[] operands = input.split(Pattern.quote(op));
            if(operands.length!=2){
                Toast.makeText(this,"Error: Invalid operation",Toast.LENGTH_SHORT).show();
                return;
            }
            int num1=Integer.parseInt(operands[0].trim());
            int num2=Integer.parseInt(operands[1].trim());
            int calculate = performCalculation(num1,num2,op);
            result.setText(String.valueOf(calculate));
        }catch (NumberFormatException e) {
            Toast.makeText(this,"Error: Invalid number format",Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            result.setText(e.getMessage());
        }
    }

    private boolean isLastCharOperator(){
        String currentText=header.getText().toString();
        if (currentText.isEmpty()){
            return false;
        }
        char currentLastChar = currentText.charAt(currentText.length()-1);
        return currentLastChar=='+' || currentLastChar=='-' || currentLastChar=='x' || currentLastChar=='÷';
    }
    private void deleteLastChar(){
        String currentText=header.getText().toString();
        if(!currentText.isEmpty()){
            header.setText(currentText.substring(0,currentText.length()-1));
        }
    }

    private int performCalculation(int num1, int num2, String operation){
        switch(operation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "÷":
                if (num2 == 0)
                    Toast.makeText(this, "Error: Division by zero", Toast.LENGTH_SHORT).show();
                return num1 / num2;
            default:
                Toast.makeText(this, "Error: Unknown operator", Toast.LENGTH_SHORT).show();
                return 0;

        }


    }
}