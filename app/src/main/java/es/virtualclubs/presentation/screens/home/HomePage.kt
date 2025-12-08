package es.virtualclubs.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.presentation.components.VCScaffold

@Composable
fun HomePage(
  viewModel: HomeViewModel = hiltViewModel()
) {
  VCScaffold {
      Column(
          modifier = Modifier.padding(it)
      ) {
          Text("Hola esto es el panel de home")
      }
  }
}