package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpaz_neb.R

@Composable
fun HomeScreenCompacta() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.libro),
            contentDescription = "Logo MPAZ NEB",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "MPAZ NEB")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Nivelacion en Lectura y Matematica")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { }) {
            Text("Comenzar")
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HomeScreenCompactaPreview() {
    HomeScreenCompacta()
}