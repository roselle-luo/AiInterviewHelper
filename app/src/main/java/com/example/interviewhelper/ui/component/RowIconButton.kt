package com.example.interviewhelper.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interviewhelper.ui.theme.Peach60

@Composable
fun RowButtonWithIcon(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 56.dp),
    onClick: () -> Unit,
    icon: Int,
    text: String,
    textColor: Color = Color.White,
    backGroundColor: Color = Peach60,
    borderColor: Color = Peach60
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backGroundColor),
        modifier = modifier,
        border = BorderStroke(1.dp, color = borderColor),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(ImageVector.vectorResource(icon), contentDescription = text, tint = Color.Unspecified, modifier = Modifier.size(46.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, letterSpacing = 2.sp, fontSize = 16.sp, color = textColor)
        }
    }
}