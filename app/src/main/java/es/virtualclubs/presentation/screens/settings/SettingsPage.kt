//package es.iesfernandoaguilar.ui.pages
//
//import android.content.Context
//import androidx.annotation.StringRes
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.defaultMinSize
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Wifi
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Slider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import es.iesfernandoaguilar.R
//import es.iesfernandoaguilar.models.User
//import es.iesfernandoaguilar.models.objects.SessionData
//import es.iesfernandoaguilar.ui.navigation.Destination
//import es.iesfernandoaguilar.ui.viewmodel.SettingsViewModel
//import es.virtualclubs.ScreenType
//import es.virtualclubs.presentation.components.AppBar
//import es.virtualclubs.presentation.components.RoundedTextField
//import kotlin.math.roundToInt
//
//@Composable
//fun SettingsPage(
//    viewModel: SettingsViewModel = hiltViewModel(),
//    @StringRes title: Int,
//    onNavigateBack: () -> Unit,
//    screenType: ScreenType
//) {
//    val state by viewModel.state.collectAsState()
//    val context = LocalContext.current
//
//    Scaffold(
//        topBar = {
//            AppBar(
//                canGoBack = true,
//                onNavigateBack = onNavigateBack,
//                titleResId = title
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            val contentWidth = when (screenType) {
//                ScreenType.Medium -> 0.5f
//                ScreenType.Small -> 0.8f
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(contentWidth)
//                    .align(Alignment.Center)
//                    .padding(horizontal = 16.dp)
//            ) {
//                if (SessionData.currentUser != null) {
//                    PersonalData(SessionData.currentUser!!)
//                }
//
//                // Theme selection buttons
//                ThemeOptions(
//                    isDarkTheme = state.darkTheme,
//                    onOptionSelected = { viewModel.changeTheme(it) },
//                    screenType = screenType
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Contrast slider
//                ContrastOptions(
//                    value = state.contrastType,
//                    onValueChanged = viewModel::changeContrast
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Font size slider
//                FontSizeMultiplierOptions(
//                    value = state.fontSizeMultiplier,
//                    onValueChanged = viewModel::changeFontSize
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Server IP input
//                ServerIpTextField(
//                    value = state.serverIp,
//                    onChangedServerIp = viewModel::changeIp
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // App info
//                Text(stringResource(R.string.developedBy))
//                Spacer(modifier = Modifier.height(5.dp))
//                Text(stringResource(R.string.actualVersion) + getAppVersion(context))
//            }
//        }
//    }
//}
//
//fun getAppVersion(context: Context): String {
//    val packageManager = context.packageManager
//    val packageName = context.packageName
//    val packageInfo = packageManager.getPackageInfo(packageName, 0)
//    return packageInfo.versionName ?: "Unknown"
//}
//
//@Composable
//fun FontSizeMultiplierOptions(
//    value: Double,
//    onValueChanged: (Double) -> Unit
//) {
//    val min = 0.75f
//    val max = 2f
//    val step = 0.1f
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = stringResource(R.string.font_size),
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Slider(
//            value = value.toFloat(),
//            onValueChange = {
//                val rounded = ((it / step).roundToInt() * step).toDouble()
//                onValueChanged(rounded)
//            },
//            valueRange = min..max,
//            steps = ((max - min) / step).toInt() - 1,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = String.format("%.1fx", value),
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//    }
//}
//
//@Composable
//fun ContrastOptions(
//    value: Int,
//    onValueChanged: (Int) -> Unit
//) {
//    val labels = listOf(
//        stringResource(R.string.lowContrast),
//        stringResource(R.string.mediumContrast),
//        stringResource(R.string.highContrast)
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = stringResource(R.string.contrast),
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Slider(
//            value = value.toFloat(),
//            onValueChange = { onValueChanged(it.roundToInt()) },
//            valueRange = 0f..2f,
//            steps = 1,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = labels.getOrElse(value) { "" },
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//    }
//}
//
//@Composable
//fun ThemeOptions(
//    isDarkTheme: Boolean?,
//    screenType: ScreenType,
//    onOptionSelected: (Boolean?) -> Unit
//) {
//    val options = listOf(
//        true to R.string.darkTheme,
//        null to R.string.defaultTheme,
//        false to R.string.lightTheme
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = stringResource(R.string.theme),
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (screenType == ScreenType.Small) {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                options.forEach { (value, labelRes) ->
//                    ThemeOptionButton(value, labelRes, isDarkTheme, onOptionSelected)
//                }
//            }
//        } else {
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                options.forEach { (value, labelRes) ->
//                    ThemeOptionButton(value, labelRes, isDarkTheme, onOptionSelected)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ThemeOptionButton(
//    value: Boolean?,
//    @StringRes labelRes: Int,
//    currentSelection: Boolean?,
//    onClick: (Boolean?) -> Unit
//) {
//    val isSelected = currentSelection == value
//
//    val modifier = Modifier.defaultMinSize(minWidth = 100.dp)
//    val text = stringResource(labelRes)
//
//    if (isSelected) {
//        Button(
//            onClick = { onClick(value) },
//            shape = RoundedCornerShape(12.dp),
//            modifier = modifier
//        ) {
//            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
//        }
//    } else {
//        OutlinedButton(
//            onClick = { onClick(value) },
//            shape = RoundedCornerShape(12.dp),
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
//            colors = ButtonDefaults.outlinedButtonColors(
//                contentColor = MaterialTheme.colorScheme.primary,
//                containerColor = Color.Transparent
//            ),
//            modifier = modifier
//        ) {
//            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Normal)
//        }
//    }
//}
//
//@Composable
//fun ServerIpTextField(
//    value: String,
//    onChangedServerIp: (String) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = stringResource(R.string.config_server_ip),
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        RoundedTextField(
//            value = value,
//            onValueChange = onChangedServerIp,
//            placeholder = R.string.defaultIp,
//            leadingIcon = Icons.Filled.Wifi,
//            keyboardType = KeyboardType.Number
//        )
//    }
//}
//
//@Composable
//fun PersonalData(
//    user: User
//) {
//    Text(stringResource(R.string.username_is, user.userName))
//    Spacer(modifier = Modifier.height(5.dp))
//    Text(stringResource(R.string.email_is, user.email))
//}