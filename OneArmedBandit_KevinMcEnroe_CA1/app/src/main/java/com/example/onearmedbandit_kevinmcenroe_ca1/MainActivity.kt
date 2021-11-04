package com.example.onearmedbandit_kevinmcenroe_ca1

/*Image Sources
Cherry: https://www.flaticon.com/premium-icon/cherries_3137038?
Banana: https://www.flaticon.com/search?word=banana&type=icon
Watermelon: https://www.flaticon.com/free-icon/watermelon_1054114
Grapes: https://www.flaticon.com/free-icon/grapes_765634
You Lose: http://thebenefitsourcellc.com/2018/02/20/inflation-you-lose/
You Win: https://www.vectorstock.com/royalty-free-vector/you-win-text-sign-on-explosion-blast-as-game-vector-28287821
Button icon custom-made by me using: https://www.clickminded.com/button-generator
*/

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("KMcE", "The onCreate function has been entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var spinCount = 0
        var winCount = 0
        var winSpinRatio: String
        updateStats(0, 0, "0:0")

        val rollButton: Button = findViewById(R.id.spinButton)
        rollButton.setOnClickListener {
            val latestSpinValues: MutableList<Int> =  spinSlots()
            val slotImage: ImageView = findViewById(R.id.resultImage)
            spinCount += 1

            if(checkWin(latestSpinValues)){
                slotImage.setImageResource(R.drawable.win)
                winCount+=1
            }
            else
                slotImage.setImageResource(R.drawable.badluck)

            winSpinRatio = getRatio(winCount, spinCount)
            updateStats(spinCount, winCount, winSpinRatio)
        }
    }

    private fun spinSlots(): MutableList<Int> {
        val slotA2 = Slot(4, R.id.screenSlotA2)
        val slotB2 = Slot(4, R.id.screenSlotB2)
        val slotC2 = Slot(4,  R.id.screenSlotC2)

        val slotCenterRow = listOf(slotA2, slotB2, slotC2)
        val allSpinValues: MutableList<Int> = ArrayList()

        for (slot in slotCenterRow) {
            val spinValue = slot.spin()
            Log.d("KMcE", "Random number generated for slot during spin = $spinValue")
            allSpinValues.add(spinValue)
            val drawableResource = when (spinValue) {
                1 -> R.drawable.banana
                2 -> R.drawable.watermelon
                3 -> R.drawable.grapes
                else -> R.drawable.cherries
            }
            val slotImage: ImageView = findViewById(slot.imageId)
            slotImage.setImageResource(drawableResource)
            slotImage.contentDescription = spinValue.toString()

            var topSlotImage: ImageView
            var bottomSlotImage: ImageView
            when (slot) {
                slotA2 -> {
                    topSlotImage = findViewById(R.id.screenSlotA1)
                    bottomSlotImage = findViewById(R.id.screenSlotA3)
                }
                slotB2 -> {
                    topSlotImage = findViewById(R.id.screenSlotB1)
                    bottomSlotImage = findViewById(R.id.screenSlotB3)
                }
                else -> {
                    topSlotImage = findViewById(R.id.screenSlotC1)
                    bottomSlotImage = findViewById(R.id.screenSlotC3)
                }
            }

            spinToweredSlot(spinValue, topSlotImage, false)
            spinToweredSlot(spinValue, bottomSlotImage, true)
        }

        return allSpinValues
    }

    private fun spinToweredSlot(spinValue: Int, bottomSlotImage: ImageView, isBottom: Boolean){
        var slotValue: Int
        slotValue = if(isBottom)
            spinValue + 1
        else
            spinValue - 1

        if (slotValue >= 5)
            slotValue = 1
        if (slotValue <= -2)
            slotValue = 3

        val drawableResourceA3 = when (slotValue) {
            1 -> R.drawable.banana
            2 -> R.drawable.watermelon
            3 -> R.drawable.grapes
            else -> R.drawable.cherries
        }
        bottomSlotImage.setImageResource(drawableResourceA3)
        bottomSlotImage.contentDescription = slotValue.toString()
    }

    private fun checkWin(allSpinValues: MutableList<Int>): Boolean{
        val firstValue = allSpinValues.elementAt(0)
        return allSpinValues.all { it == firstValue }
    }

    private fun updateStats(spinCount: Int, winCount: Int, winLoseRatio: String){
        val spinText: TextView = findViewById(R.id.numSpins)
        val winText: TextView = findViewById(R.id.numWins)
        val winLoseRatioText: TextView = findViewById(R.id.winSpinRatio)

        spinText.text = spinCount.toString()
        winText.text = winCount.toString()
        winLoseRatioText.text = winLoseRatio
    }

    //Adapted from: https://codereview.stackexchange.com/questions/26697/
    private fun highestCommonDivider(x: Int, y: Int): Int{
        return if (y == 0) {
            x
        } else
            return highestCommonDivider(y, x % y)
    }

    //Adapted from: https://codereview.stackexchange.com/questions/26697/
    private fun getRatio(a: Int, b: Int): String{
        val highestCommonDivider = highestCommonDivider(a, b)
        val leftHandSide: Int
        val rightHandSide: Int

        if(a > b){
            leftHandSide = a/highestCommonDivider
            rightHandSide = b/highestCommonDivider
        }
        else{
            leftHandSide = b/highestCommonDivider
            rightHandSide = a/highestCommonDivider
        }

        return "$rightHandSide : $leftHandSide"
    }
}

class Slot(private val numTiles: Int, id : Int) {
    val imageId = id
    fun spin(): Int {
        return (1..numTiles).random()
    }
}