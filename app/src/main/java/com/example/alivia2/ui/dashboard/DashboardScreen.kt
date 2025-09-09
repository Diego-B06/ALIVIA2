package com.example.alivia2.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush // Added import
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alivia2.R
import com.example.alivia2.ui.theme.ALIVIA2Theme
import com.example.alivia2.ui.theme.LightGreen
import com.example.alivia2.ui.theme.DarkBlue

// Main Composable for the Dashboard Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit = {}) {
    var currentScreen by remember { mutableStateOf(DashboardScreens.Home) }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFBB86FC), Color.White),
        start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-left
        end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-right
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent, // Make TopAppBar transparent
                        titleContentColor = MaterialTheme.colorScheme.onBackground // Or a color that contrasts well with your gradient
                    )
                )
            },
            bottomBar = {
                DashboardBottomNavigationBar(
                    currentScreen = currentScreen,
                    containerColor = Color.Transparent, // Make BottomNav transparent
                    onScreenSelected = { screen ->
                        currentScreen = screen
                    }
                )
            },
            containerColor = Color.Transparent // Make Scaffold container transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { WellbeingLevelSection() }
                item { TodaysFocusSection() }
                item { CalendarViewSection() }
                item { AliviaAssistantSection() }
            }
        }
    }
}

// --- Sections of the Dashboard ---

@Composable
fun WellbeingLevelSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Wellbeing Level", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        WellbeingCircularProgressBar(progress = 0.7f, levelText = "Good")
    }
}

@Composable
fun WellbeingCircularProgressBar(progress: Float, levelText: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(180.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 20.dp.toPx()
            // Background arc
            drawArc(
                color = Color.LightGray.copy(alpha = 0.5f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Foreground arc (progress)
            drawArc(
                color = LightGreen, // Using LightGreen from Color.kt
                startAngle = -90f, // Start from the top
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(levelText, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
    }
}

@Composable
fun TodaysFocusSection() {
    DashboardCard {
        Text("Today's Focus", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))
        FocusItem(text = "Meditate 10 min", checked = true)
        FocusItem(text = "Walk in the park", checked = true)
        FocusItem(text = "Read 20 pages", checked = false)
    }
}

@Composable
fun FocusItem(text: String, checked: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Icon(
            painter = painterResource(id = if (checked) R.drawable.ic_check_circle_filled else R.drawable.ic_check_circle_outline),
            contentDescription = if (checked) "Checked" else "Unchecked",
            tint = if (checked) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CalendarViewSection() {
    DashboardCard {
        Text("October 2024", fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        // Placeholder for a simple calendar grid - a more complex calendar would require more logic or a library
        SimpleCalendarView()
    }
}

@Composable
fun SimpleCalendarView() {
    val days = (1..31).toList()
    val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    val highlightedDay = 26 // Example highlighted day

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(day, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
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
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (dayOfMonth == highlightedDay) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(8.dp),
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
            Image(
                painter = painterResource(id = R.drawable.ic_alivia_logo), 
                contentDescription = "Alivia Assistant",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Alivia's Assistant", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Hi there! How are you feeling today? Text text...",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DashboardCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

enum class DashboardScreens(val title: String, val icon: ImageVector) {
    Home("Dashboard", Icons.Filled.Home),
    Calendar("Calendar", Icons.Filled.DateRange),
    Chat("Chat", Icons.Filled.MailOutline) 
}

@Composable
fun DashboardBottomNavigationBar(
    currentScreen: DashboardScreens,
    containerColor: Color = MaterialTheme.colorScheme.surface, // Added containerColor parameter with default
    onScreenSelected: (DashboardScreens) -> Unit
) {
    NavigationBar(
        containerColor = containerColor, // Use the parameter here
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
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
                    indicatorColor = if (containerColor == Color.Transparent) Color.White.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current + NavigationBarDefaults.Elevation) // Adjust indicator for transparent BG
                )
            )
        }
    }
}


@Preview(showBackground = true, device = "spec:shape=Normal,width=360,height=740,unit=dp,dpi=480")
@Composable
fun DashboardScreenPreview() {
    ALIVIA2Theme {
        DashboardScreen()
    }
}
