package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.theme.FormDraw
import com.example.matchcast.presentaion.theme.FormLoss
import com.example.matchcast.presentaion.theme.FormWin
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun FormGuide(
    form: ArrayDeque<MatchOutcome>?,
    modifier: Modifier
){
    Row(
        modifier = modifier
            .height(30.dp)
            .width(140.dp)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (form != null) {
            for (outcome in form){
                var color: Color
                var let: String
                when(outcome){
                    MatchOutcome.WIN -> {
                        color = FormWin
                        let = "W"
                    }
                    MatchOutcome.LOSE -> {
                        color = FormLoss
                        let = "L"
                    }

                    MatchOutcome.DRAW -> {
                        color = FormDraw
                        let = "D"
                    }
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = let,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseFormGuide(){
    MatchCastTheme {
        FormGuide(
            form = ArrayDeque(listOf(MatchOutcome.WIN,MatchOutcome.WIN,MatchOutcome.WIN,MatchOutcome.DRAW,MatchOutcome.LOSE)),
            modifier = Modifier)
    }
}