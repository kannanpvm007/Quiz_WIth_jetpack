package com.example.quizwithjetpack.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizwithjetpack.model.QuizMbo

/**
 * Created by kannanpvm007 on 11-07-2023.
 */

@Composable
fun Questions(viewModel: QuizViewModel) {
    val qustions = viewModel.data.value.data?.toMutableList()

    val qustionsIndex= remember { mutableStateOf(0) }
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val qus = try {
            qustions?.get(qustionsIndex.value)
        }catch (e:Exception){null}
        Log.e("TAG", "Questions: $qus", )
        if (qus != null) {
            QuestionDisplay(qustions = qus,qustionsIntex=qustionsIndex,viewModel=viewModel, onNextClicked ={
                Log.d("TAG", "Questions: current:${qustionsIndex.value} total: ${qustions?.size} ")
                if (qustionsIndex.value< qustions?.size?.minus(1)!!)
                qustionsIndex.value=qustionsIndex.value+1
            } )
        }

    }
}

@Composable
fun QuestionDisplay(
    qustions: QuizMbo,
    qustionsIntex: MutableState<Int>,
    viewModel: QuizViewModel,
    onNextClicked: (Int) -> Unit
) {
    val choicesState = remember(qustions) {
        qustions.choices.toMutableList()
    }
    val answerSate = remember(qustions) {
        mutableStateOf<Int?>(null)
    }
    val correnctAnswerState = remember(qustions) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(qustions) {
        {
            answerSate.value = it
            correnctAnswerState.value = choicesState[it] == qustions.answer
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = Color.Gray
    ) {

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        Column(
            modifier = Modifier.padding(2.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker(counter =qustionsIntex.value)
            DrawPath(pathEffect = pathEffect)

            Column() {
                Text(
                    modifier = Modifier
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    text = "${qustions.question}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = Color.White
                )

                choicesState.forEachIndexed { index, anserText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(Color.Blue, Color.Magenta)
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50

                                )
                            )
                            .clickable { updateAnswer(index) }
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerSate.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (correnctAnswerState.value == true && index == answerSate.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else Color.Red.copy(alpha = 0.3f)
                            )
                        )
                        val annotatedString= buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                color =
                                if (correnctAnswerState.value == true && index == answerSate.value) {Color.Green}
                                else if (correnctAnswerState.value == false && index == answerSate.value){
                                    Color.Red}
                                else{
                                    Color.White}
                            )){
                                append(anserText)

                            }

                        }

                        Text(text = annotatedString, modifier = Modifier.padding(16.dp))
                    }
                }
                Button(onClick = { onNextClicked(qustionsIntex.value)}
                , modifier = Modifier.padding(3.dp),
                colors= ButtonDefaults.buttonColors(containerColor = Color.Blue,)) {

                    Text(text = "Next", modifier = Modifier.padding(4.dp), color = Color.White)
                }
            }
        }

    }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp
                )
            ) {
                append("Questions ${counter+1}/")
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 24.sp
                    )
                ) {
                    append("$outOf")

                }
            }
        }
    }, modifier = Modifier.padding(20.dp))

}

@Composable
fun DrawPath(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp), onDraw = {
        drawLine(
            color = Color.White,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )

    })

}