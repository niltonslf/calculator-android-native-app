package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /*
    * Declara os atributos em seus devidos tipos
    */
    Button btn0, btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnSum,btnMultiply,btnDivide,btnSubtract,btnClear, btnDot,btnEqual;
    TextView textResult, textOperation;
    String process;
    Boolean checkParentheses = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Relacionando atributos com os botões de número
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        // Relacionando atributos com os botões de ação
        btnSum = findViewById(R.id.btnSum);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnClear = findViewById(R.id.btnClear);
        btnDot = findViewById(R.id.btnDot);
        btnEqual = findViewById(R.id.btnEqual);

        // Relacionando atributos com os inputs de texto
        textResult = findViewById(R.id.textResult);
        textOperation = findViewById(R.id.textOperation);


        clear();
        finalResult();


        handleInputButton(btn0);
        handleInputButton(btn1);
        handleInputButton(btn2);
        handleInputButton(btn3);
        handleInputButton(btn4);
        handleInputButton(btn5);
        handleInputButton(btn6);
        handleInputButton(btn7);
        handleInputButton(btn8);
        handleInputButton(btn9);

        handleInputButton(btnDivide);
        handleInputButton(btnMultiply);
        handleInputButton(btnSubtract);
        handleInputButton(btnSum);
        handleInputButton(btnDot);

    }

    /**
     * Cria um listener para o evento de click do botão fornecido
     * Adiciona o label do botão clicado no input de texto de operação
     * @param button
     */
    public void handleInputButton(final Button button) {
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String btnLabel = button.getText().toString(); // obtém o label do botão
                process = textOperation.getText().toString(); // salva o valor atual do input
                textOperation.setText(process + btnLabel); // concatena o valor novo e o atual no input
            }
        });
    }


    /**
     * Limpa os inputs de operação e resultado
     */
    public void clear() {
        // Adiciona listener no click do botão
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOperation.setText(""); // limpa a caixa de texto
                textResult.setText(""); // limpa a caixa de texto
            }
        });
    }

    public void finalResult(){
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempNumber = null;
                String currentOperation = textOperation.getText().toString();
                ArrayList<String> numbers = new ArrayList<String>(); // Pilha com os números
                ArrayList<String> operations = new ArrayList<String>(); // Pilha com as operações


                /**
                 * Percorre a string que tem as operações a serem realizadas
                 * separando o que for número numa pilha de números
                 * e o que for operador numa pilha de operadores
                 */
                for (char ch: currentOperation.toCharArray()) {
                    // salva o numero atual em tempNumber
                    // Se o próximo também for um número então:
                    // 1 - concatenar os dois numeros juntos. Ex: 2 e 2 = 22
                    // 2-se o próximo for um simbolo então deve salvar o numero na tempNumber
                    // depois voltar ela para Null

                    if (ch == '+' || ch == '-'  || ch == '/'  || ch == '*' ) {
                        if(tempNumber != null) tempNumber = null;
                        operations.add(String.valueOf(ch));
                    } else {
                        if (tempNumber == null) {
                            numbers.add(String.valueOf(ch));
                        } else {
                            String concatenated = tempNumber + ch;
                            int lastIndex = numbers.size() - 1;

                            numbers.remove(lastIndex); // remove ultimo numero adicionado (o que tá somente com o primeiro digito)
                            numbers.add(concatenated); // adiciona os digitos concatenados
                        }
                        tempNumber = String.valueOf(ch);
                    }
                }


                /**
                 * Percorre a pilha de numeros realizando as operações
                 * salvas na pilha de operação
                 */

                // Regra: Pegar o primero numero da pilha de numeros
                // pegar o primeiro operador da pilha de operadores para descobrir o método necessário
                // pegar o segundo número da pilha de numeros e realizar a operação correta

                String firstNumber = null;

                for (String number : numbers) {
                    if(firstNumber == null) {
                        firstNumber = number;
                    }  else {
                        String operation = operations.get(0); // pega a primeira operação
                        operations.remove(0); // remove ela da pilha

                        // decide qua operação deve ser executada
                        switch (operation){
                            case "+":
                                firstNumber = sum(firstNumber, number);
                                break;
                            case "-":
                                firstNumber = subtract(firstNumber, number);
                                break;
                            case "*":
                                firstNumber = multiply(firstNumber, number);
                                break;
                            case "/":
                                firstNumber = divide(firstNumber, number);
                                break;

                            default:
                                textResult.setText("Operação inválida");
                        }
                    }
                }
                textResult.setText(firstNumber);
                textOperation.setText(""); // limpa input de operação
            }
        });
    }


    /**
     * Realiza operação de soma
     * @param FirstNumber - Numero a esquerda do operador
     * @param secondNumber - Numero a direita do operador
     * @return retorna uma string com o resultado da operação
     */
    public String sum(String FirstNumber, String secondNumber) {
        Float leftNumber = Float.parseFloat(FirstNumber);
        Float rightNumber = Float.parseFloat(secondNumber);

        Float result =  Float.sum(leftNumber , rightNumber);

        return result.toString();
    }
    /**
     * Realiza operação de subtração
     * @param FirstNumber - Numero a esquerda do operador
     * @param secondNumber - Numero a direita do operador
     * @return retorna uma string com o resultado da operação
     */
    public String subtract(String FirstNumber, String secondNumber) {
        Float leftNumber = Float.parseFloat(FirstNumber);
        Float rightNumber = Float.parseFloat(secondNumber);
        Float result =  leftNumber -  rightNumber;

        return result.toString();
    }

    /**
     * Realiza operação de multiplicação
     * @param FirstNumber - Numero a esquerda do operador
     * @param secondNumber - Numero a direita do operador
     * @return retorna uma string com o resultado da operação
     */
    public String multiply(String FirstNumber, String secondNumber) {
        Float leftNumber = Float.parseFloat(FirstNumber);
        Float rightNumber = Float.parseFloat(secondNumber);
        Float result =  leftNumber *  rightNumber;

        return result.toString();
    }

    /**
     * Realiza operação de divisão
     * @param FirstNumber - Numero a esquerda do operador
     * @param secondNumber - Numero a direita do operador
     * @return retorna uma string com o resultado da operação
     */
    public String divide(String FirstNumber, String secondNumber) {
        Float leftNumber = Float.parseFloat(FirstNumber);
        Float rightNumber = Float.parseFloat(secondNumber);
        Float result =  leftNumber /  rightNumber;

        return result.toString();
    }

}