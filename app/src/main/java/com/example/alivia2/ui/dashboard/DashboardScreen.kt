package com.example.alivia2.ui.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut // Nueva importación para animación de salida
import androidx.compose.animation.shrinkVertically // Nueva importación para animación de salida
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Necesario para usar key en LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alivia2.R
import com.example.alivia2.ui.theme.ALIVIA2Theme
import com.example.alivia2.ui.theme.LightGreen
import com.example.alivia2.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Constantes para Notificaciones
const val TASK_NOTIFICATION_CHANNEL_ID = "task_completed_channel"
const val TASK_NOTIFICATION_CHANNEL_NAME = "Task Completions"
const val TASK_NOTIFICATION_ID_BASE = 1000

// Duraciones de animación
const val TASK_COMPLETE_ANIM_DELAY = 1500L // Retraso antes de que la tarea desaparezca
const val TASK_EXIT_ANIM_DURATION = 500L   // Duración de la animación de desaparición

// Data class for a focus task - ahora con isVisible
data class FocusTask(
    val id: Int,
    val text: String,
    var isChecked: Boolean, // var para poder modificarla directamente en la lista
    var isVisible: Boolean = true // var para controlar la animación de visibilidad
)

fun createTaskNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = TASK_NOTIFICATION_CHANNEL_NAME
        val descriptionText = "Notifications for completed tasks"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(TASK_NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showTaskCompletedNotification(context: Context, task: FocusTask) {
    createTaskNotificationChannel(context)
    val builder = NotificationCompat.Builder(context, TASK_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("¡Felicidades!")
        .setContentText("Has completado tu tarea: \"${task.text}\". ¿Listo para la siguiente?")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)) {
        notify(TASK_NOTIFICATION_ID_BASE + task.id, builder.build())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit = {}) {
    var currentScreen by remember { mutableStateOf(DashboardScreens.Home) }
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFBB86FC), Color.White),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.mipmap.alivia2_logo),
                            contentDescription = "Alivia2 Logo",
                            modifier = Modifier.height(55.dp),
                            contentScale = ContentScale.Fit
                        )
                    },
                    actions = {
                        Image(
                            painter = painterResource(id = R.mipmap.logo_alivia),
                            contentDescription = "Logo de Alivia",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(end = 24.dp)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            bottomBar = {
                DashboardBottomNavigationBar(
                    currentScreen = currentScreen,
                    containerColor = Color.Transparent,
                    onScreenSelected = { screen -> currentScreen = screen }
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            var contentVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(200L)
                contentVisible = true
            }
            AnimatedVisibility(
                visible = contentVisible,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight / 2 },
                    animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300, delayMillis = 150)
                )
            ) {
                when (currentScreen) {
                    DashboardScreens.Home -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            item { WellbeingLevelSection() }
                            item { TodaysFocusSection() }
                            item { CalendarViewSection() }
                            item { AliviaAssistantSection() }
                        }
                    }
                    DashboardScreens.Calendar -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Contenido Pantalla Calendario", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    DashboardScreens.Chat -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Contenido Pantalla Chat", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WellbeingLevelSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("Wellbeing Level", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        WellbeingCircularProgressBar(progress = 0.7f, levelText = "Good")
    }
}

@Composable
fun WellbeingCircularProgressBar(progress: Float, levelText: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20.dp.toPx()
            drawArc(color = Color.LightGray.copy(alpha = 0.5f), 0f, 360f, false, style = Stroke(strokeWidth, cap = StrokeCap.Round))
            drawArc(LightGreen, -90f, 360 * progress, false, style = Stroke(strokeWidth, cap = StrokeCap.Round))
        }
        Text(levelText, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Necesario para AnimatedVisibility en LazyColumn items
@Composable
fun TodaysFocusSection() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusTasks = remember {
        mutableStateListOf(
            FocusTask(id = 1, text = "Meditate 10 min", isChecked = false),
            FocusTask(id = 2, text = "Walk in the park", isChecked = false),
            FocusTask(id = 3, text = "Read 20 pages", isChecked = false)
        )
    }

    DashboardCard {
        Text("Today's Focus", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))
        if (focusTasks.none { it.isVisible }) { // Mostrar mensaje si no hay tareas visibles
            Text(
                "All tasks completed or none added!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        } else {
            // Usamos LazyColumn para que las animaciones de eliminación sean más suaves con `key`
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = focusTasks, key = { task -> task.id }) { task ->
                    FocusItem(
                        task = task,
                        onTaskCheckChanged = { currentTask, newCheckedState ->
                            val taskIndex = focusTasks.indexOfFirst { it.id == currentTask.id }
                            if (taskIndex != -1) {
                                val taskToUpdate = focusTasks[taskIndex]
                                val previouslyChecked = taskToUpdate.isChecked

                                // Actualizar estado de isChecked e isVisible
                                focusTasks[taskIndex] = taskToUpdate.copy(
                                    isChecked = newCheckedState,
                                    isVisible = if (newCheckedState) taskToUpdate.isVisible else true // Si se desmarca, siempre visible
                                )

                                if (newCheckedState && !previouslyChecked) {
                                    showTaskCompletedNotification(context, taskToUpdate)
                                    coroutineScope.launch {
                                        delay(TASK_COMPLETE_ANIM_DELAY)
                                        // Volver a encontrar la tarea para asegurarse de que sigue marcada y debe ocultarse
                                        val taskInList = focusTasks.find { it.id == currentTask.id }
                                        if (taskInList != null && taskInList.isChecked) {
                                            focusTasks[taskIndex] = taskInList.copy(isVisible = false)
                                            delay(TASK_EXIT_ANIM_DURATION)
                                            focusTasks.remove(taskInList) // Eliminar de la lista después de la animación
                                        }
                                    }
                                } else if (!newCheckedState) {
                                    // Si se desmarca, asegurar que es visible y cancelar la ocultación (si estaba en progreso)
                                    // El copy anterior ya maneja la visibilidad al desmarcar
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FocusItem(task: FocusTask, onTaskCheckChanged: (task: FocusTask, isChecked: Boolean) -> Unit) {
    // AnimatedVisibility para la animación de entrada/salida del ítem completo
    AnimatedVisibility(
        visible = task.isVisible,
        exit = shrinkVertically(animationSpec = tween(durationMillis = TASK_EXIT_ANIM_DURATION.toInt())) +
               fadeOut(animationSpec = tween(durationMillis = TASK_EXIT_ANIM_DURATION.toInt())),
        modifier = Modifier.fillMaxWidth() // Asegurar que AnimatedVisibility ocupe el espacio
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTaskCheckChanged(task, !task.isChecked) }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = task.text,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textDecoration = if (task.isChecked) TextDecoration.LineThrough else null,
                color = if (task.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurface
            )
            Icon(
                painter = painterResource(id = if (task.isChecked) R.drawable.ic_check_circle_filled else R.drawable.ic_check_circle_outline),
                contentDescription = if (task.isChecked) "Checked" else "Unchecked",
                tint = if (task.isChecked) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun CalendarViewSection() {
    DashboardCard {
        Text("October 2024", fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        SimpleCalendarView()
    }
}

@Composable
fun SimpleCalendarView() {
    val days = (1..31).toList()
    val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    val highlightedDay = 26
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day -> Text(day, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val firstDayOffset = 3
        val totalCells = (days.size + firstDayOffset + 6) / 7 * 7
        for (week in 0 until (totalCells / 7)) {
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                for (dayIndexInWeek in 0..6) {
                    val cellIndex = week * 7 + dayIndexInWeek
                    val dayOfMonth = cellIndex - firstDayOffset + 1
                    if (dayOfMonth in days) {
                        Text(
                            text = dayOfMonth.toString(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.size(36.dp).clip(CircleShape).background(if (dayOfMonth == highlightedDay) MaterialTheme.colorScheme.primary else Color.Transparent).padding(8.dp),
                            color = if (dayOfMonth == highlightedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Spacer(modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AliviaAssistantSection() {
    DashboardCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.mipmap.logo_alivia), contentDescription = "Alivia Assistant", modifier = Modifier.size(40.dp).clip(CircleShape))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Alivia's Assistant", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Hi there! How are you feeling today? Text text...", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun DashboardCard(content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) { content() }
    }
}

enum class DashboardScreens(val title: String, val icon: ImageVector) {
    Home("Dashboard", Icons.Filled.Home),
    Calendar("Calendar", Icons.Filled.DateRange),
    Chat("Chat", Icons.Filled.MailOutline)
}

@Composable
fun DashboardBottomNavigationBar(currentScreen: DashboardScreens, containerColor: Color = MaterialTheme.colorScheme.surface, onScreenSelected: (DashboardScreens) -> Unit) {
    NavigationBar(containerColor = containerColor, contentColor = MaterialTheme.colorScheme.onSurface) {
        DashboardScreens.values().forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = if (containerColor == Color.Transparent) Color.White.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current + NavigationBarDefaults.Elevation)
                )
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:shape=Normal,width=360,height=740,unit=dp,dpi=480")
@Composable
fun DashboardScreenPreview() {
    ALIVIA2Theme { DashboardScreen() }
}
