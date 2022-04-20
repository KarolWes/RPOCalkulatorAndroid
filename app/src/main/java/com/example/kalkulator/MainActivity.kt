package com.example.kalkulator


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.pow
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE=10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IdFieldsSet()
        setSupportActionBar(findViewById(R.id.mainToolbar))
        }
    var dot: Boolean = false
    var negative: Boolean = false
    var errorFlag: Boolean = false
    var inputFlag: Boolean = false
    var decimalPlaces = 0
    var tmp = 0
    var input:Double = 0.0
    var s = Stack<Double>()
    var t: String = ""
    var eps: Int = 2
    var darkMode: Boolean = false
    var last: Double = input

    private fun showActivity() {
        val i = Intent(this, SettingsActivity2::class.java)
        val b = Bundle()
        b.putBoolean("DarkMode", darkMode)
        b.putInt("Eps", eps)
        i.putExtras(b)
        startActivityForResult(i, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data != null){
                val b = data.extras
                eps = b?.getInt("Eps") ?: eps
                darkMode = b?.getBoolean("DarkMode") ?: darkMode
            }
        }
    }
    fun settingsClicked(v:View){
        showActivity()
    }

    fun ResetVariables(){
        negative = false
        dot = false
        inputFlag = false
        decimalPlaces = 0
        input = 0.0
        t = ""
        tmp = 0
    }
    fun IdFieldsSet(){
        val id1Field: TextView = findViewById(R.id.id1)
        val id2Field: TextView = findViewById(R.id.id2)
        val id3Field: TextView = findViewById(R.id.id3)
        val id4Field: TextView = findViewById(R.id.id4)
        if(!inputFlag){
            if(errorFlag){
            id1Field.text = "!"
            }
            else{
                id1Field.text = "1"
            }
            id2Field.text = "2"
            id3Field.text = "3"
            id4Field.text = "4"
        }
        else{
            if(errorFlag){
                id1Field.text = "!"
            }
            else{
                id1Field.text = "➔"
            }
            id2Field.text = "1"
            id3Field.text = "2"
            id4Field.text = "3"
        }
    }
    fun StackFieldsSet(){
        val stack1Field: TextView = findViewById(R.id.stack1)
        val stack2Field: TextView = findViewById(R.id.stack2)
        val stack3Field: TextView = findViewById(R.id.stack3)
        val stack4Field: TextView = findViewById(R.id.stack4)
        val alertColor = resources.getColor(R.color.Alert)
        val fontColor =  resources.getColor(R.color.font)
        if(errorFlag)
        {
            stack1Field.setTextColor(alertColor)
        }
        else{
            stack1Field.setTextColor(fontColor)
        }
        var stackList = s.toDoubleArray()
        stackList.reverse()
        if(!inputFlag && !errorFlag){
            when(stackList.size){
                0 -> {stack1Field.text = ""; stack2Field.text = ""; stack3Field.text = ""; stack4Field.text = "";}
                1 -> {stack1Field.text = stackList[0].toString(); stack2Field.text = ""; stack3Field.text = ""; stack4Field.text = "";}
                2 -> {stack1Field.text = stackList[0].toString(); stack2Field.text = stackList[1].toString(); stack3Field.text = ""; stack4Field.text = "";}
                3 -> {stack1Field.text = stackList[0].toString(); stack2Field.text = stackList[1].toString(); stack3Field.text = stackList[2].toString(); stack4Field.text = "";}
                else -> {stack1Field.text = stackList[0].toString(); stack2Field.text = stackList[1].toString(); stack3Field.text = stackList[2].toString(); stack4Field.text = stackList[3].toString();}
            }
        }
        else {
            when (stackList.size) {
                0 -> {
                    stack1Field.text = t; stack2Field.text = ""; stack3Field.text =
                        ""; stack4Field.text = "";
                }
                1 -> {
                    stack1Field.text = t; stack2Field.text =
                        stackList[0].toString(); stack3Field.text = ""; stack4Field.text = "";
                }
                2 -> {
                    stack1Field.text = t; stack2Field.text =
                        stackList[0].toString(); stack3Field.text = stackList[1].toString(); stack4Field.text = "";
                }
                else -> {
                    stack1Field.text = t; stack2Field.text =
                        stackList[0].toString(); stack3Field.text =
                        stackList[1].toString(); stack4Field.text = stackList[2].toString();
                }
            }
        }
    }

    fun Update(){
        IdFieldsSet()
        StackFieldsSet()
    }


    fun AddNum(num:Int){
        errorFlag = false
        if(t.isNotEmpty() && t[0] == 'e'){
            t = ""
        }
        inputFlag = true
        val n: Int = num
        tmp *= 10
        tmp += n
        if (dot) {
            decimalPlaces++
        }
        t += num.toString()
    }
    fun Execute(){
        if(inputFlag){
            if(negative){
                tmp *= -1
            }
            val mul = 10.0.pow(eps)
            input = round(tmp /(10.0.pow(decimalPlaces.toDouble()))*mul)/mul
            s.push(input)
            last = input
        }
        else{
            s.push(last)
        }

        ResetVariables()
    }

    fun Operation(o: Char): String{
        if (s.size < 2) {
            errorFlag = true
            return "err: not enough args"
        }
        val mul = 10.0.pow(eps)
        val b: Double = s.pop()
        val a: Double = s.pop()
        var res = 0.0
        when (o) {
            '+' -> res = a + b
            '-' -> res = a - b
            '*' -> res = a * b
            '/' -> {
                if (b == 0.0) {errorFlag = true; return  "err: div by 0"}
                res = a / b
            }
            '^' -> {
                if (b == 0.0 && a == 0.0) {errorFlag = true; return "err: pow 0^0"}
                res = a.pow(b)
            }
            'r' -> {
                if (a <= 0) {errorFlag = true; return "err: root of neg"}
                res = a.pow(1.0 / b)
            }
        }
        res = round(res*mul)/mul
        s.push(res)
        return res.toString()
    }

    fun Swp():Boolean{
        if (s.size > 1) {
            val a: Double = s.pop()
            val b: Double = s.pop()
            s.push(a)
            s.push(b)
            return true
        }
        return false
    }

    fun AC(){
        while(!s.empty()){
            s.pop()
        }
        ResetVariables()
        errorFlag = false;
    }

    fun Drp():Boolean{
        errorFlag = false
        if(!s.empty()){
            s.pop()
            ResetVariables()

            return true
        }
        return false
    }

    fun Del(){
        if(t != ""){
            tmp/=10
            if(dot){
                decimalPlaces--
            }
            t = t.substring(0, t.length-1)
        }
        errorFlag = false;
    }

    fun Dot(): Boolean{
        if(!dot){
            dot = true
            t+="."
            return true
        }
        else{
            return false
        }
    }

    fun Rev(){
        if(inputFlag){
            negative = negative xor true
            if(negative){
                t = "-$t"
            }else{
                t = t.substring(1)
            }
        }
        else{
            var a = s.pop()
            a*= -1
            s.push(a)
        }
    }

    fun Convert(v: Float):String{
        var res: String = v.toString()
        if (dot) {
            res = res.substring(0,res.length - decimalPlaces) +
                    '.' + res.substring(res.length - decimalPlaces)
        }
        return res
    }

    fun NumClicked(v:View){
        when(v.id){
            R.id.button0 -> {AddNum(0)}
            R.id.button1 -> {AddNum(1)}
            R.id.button2 -> {AddNum(2)}
            R.id.button3 -> {AddNum(3)}
            R.id.button4 -> {AddNum(4)}
            R.id.button5 -> {AddNum(5)}
            R.id.button6 -> {AddNum(6)}
            R.id.button7 -> {AddNum(7)}
            R.id.button8 -> {AddNum(8)}
            R.id.button9 -> {AddNum(9)}
        }
        Update()
    }
    fun OpClicked(v:View){
        when(v.id){
            R.id.buttonPlus->{t = Operation('+')
                Log.i("Operation", t)}
            R.id.buttonMinus->{t = Operation('-')
                Log.i("Operation", t)}
            R.id.buttonMultiplication->{t = Operation('*')
                Log.i("Operation", t)}
            R.id.buttonDivision->{t = Operation('/')
                Log.i("Operation", t)}
            R.id.buttonRoot->{t = Operation('r')
                Log.i("Operation", t)}
            R.id.buttonPower->{t = Operation('^')
                Log.i("Operation", t)}
        }
        Update()
        ResetVariables()
    }
    fun SpecialClicked(v:View){
        when(v.id){
            R.id.buttonAC->{AC()}
            R.id.buttonSwap->{
                val test = Swp()
                if(!test){
                    Log.i("Err", "Swap")
                    t = "err: SWP: not enough args"
                    errorFlag = true
                }
            }
            R.id.buttonDrop-> {
                val test = Drp()
                if (!test) {
                    Log.i("Err", "Drop")
                    t = "err: DRP: not enough args"
                    errorFlag = true
                }
            }
            R.id.buttonDel->{Del()}
            R.id.buttonDot->{
                val test = Dot()
                if(!test){
                    Log.i("Err", "Dot")
                    t = "err: DOT: multiple dot"
                    errorFlag = true
                }
            }
            R.id.buttonPlusMinus->{Rev()}
            R.id.buttonEnter->{Execute()}
        }
        Update()
    }
}