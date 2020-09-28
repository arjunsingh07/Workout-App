package com.example.a7minuteworkout

import android.icu.math.BigDecimal
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_bmi.*
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode

class Bmi : AppCompatActivity() {

    val METRICS_UNITS_VIEW = "METRICS_UNITS_VIEW"
    val US_UNITS_VIEW = "US_UNITS_VIEW"
    var currentVisibleView:String = METRICS_UNITS_VIEW



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        setSupportActionBar(toolbar_bmi_activity)

        val actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)//set back button
            actionBar.title = "Calculate BMI"
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_calculate.setOnClickListener {
            if (currentVisibleView.equals(METRICS_UNITS_VIEW)){
                if(validateMetricUnits()){
                    val weight:Float = et_weight.text.toString().toFloat()
                    val height:Float = et_height.text.toString().toFloat()/100

                    val bmi = weight/(height*height)
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validateUSUnits()){
                    val usUnitHeightValueFeet:String = et_USUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch:String = et_USUnitHeightInch.text.toString()
                    val usUnitWeightValue:Float = et_UsWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    val bmi = 703 *(usUnitWeightValue/(heightValue * heightValue))
                    displayBMIResult(bmi)

                }else{
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }

        }
        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetrics){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }



        }


    }



    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRICS_UNITS_VIEW
        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE

        et_height.text!!.clear()
        et_weight.text!!.clear() 

        tilUsUnitWeight.visibility = View.GONE
        llUsUnitHeight.visibility = View.GONE
        ll_output.visibility = View.GONE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = METRICS_UNITS_VIEW
        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE

        et_USUnitHeightFeet.text!!.clear()
        et_USUnitHeightInch.text!!.clear()
        et_UsWeight.text!!.clear()

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitHeight.visibility = View.VISIBLE
        ll_output.visibility = View.GONE

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayBMIResult(bmi:Float){
        val bmiLabel:String
        val bmiDescription:String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }



        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,2).toString()

        tv_bmi_result_number.text = bmiValue
        tv_bmi_result_text.text = bmiLabel
        tv_bmi_result_toast.text = bmiDescription

        ll_output.visibility = View.VISIBLE

    }




    private fun validateMetricUnits(): Boolean{
        var isValid = true
        
        if(et_weight.text.toString().isEmpty())
            isValid = false
        else if(et_height.text.toString().isEmpty())
            isValid = false


        return isValid
    }
    private fun validateUSUnits(): Boolean{
        var isValid = true

        if(et_UsWeight.text.toString().isEmpty())
            isValid = false
        else if(et_USUnitHeightInch.text.toString().isEmpty())
            isValid = false
        else if(et_USUnitHeightFeet.text.toString().isEmpty())
            isValid = false


        return isValid
    }
}