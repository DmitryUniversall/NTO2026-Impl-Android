package ru.myitschool.work.ui.screen.main.device.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.myitschool.work.R
import ru.myitschool.work.ui.common.borderBottom
import ru.myitschool.work.ui.common.muted
import ru.myitschool.work.ui.common.objectClickableNoAnimation
import ru.myitschool.work.ui.screen.main.device.DeviceMainIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
private fun ScheduleNavItem(
    modifier: Modifier = Modifier,
    title: String,
    date: LocalDate,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val formatter = remember { DateTimeFormatter.ofPattern("EE, d MMMM", Locale.forLanguageTag("ru")) }

    Column(
        modifier = modifier
            .borderBottom(
                width = if (isActive) 3.dp else 1.dp,
                color = if (isActive) colors.primary else colors.onSurface.muted(alpha = 0.1f)
            )
            .padding(16.dp)
            .objectClickableNoAnimation(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = typography.headlineMedium,
            color = colors.onSurface
        )

        Text(
            text = formatter.format(date),
            style = typography.bodyLarge,
            color = colors.onSurface.muted()
        )
    }
}

@Composable
fun ScheduleNav(
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate
) {
    val colors = MaterialTheme.colorScheme
    val firstDate = remember { LocalDate.now() }
    val secondDate = remember { firstDate.plusDays(1) }
    val thirdDate = remember { secondDate.plusDays(1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ScheduleNavItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.today),
            date = firstDate,
            isActive = selectedDate == firstDate
        ) {
            viewModel.onIntent(DeviceMainIntent.SelectDate(date = firstDate))
        }

        VerticalDivider(modifier = Modifier.fillMaxHeight(0.7f), color = colors.onSurface.muted(alpha = 0.1f))

        ScheduleNavItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.tomorrow),
            date = secondDate,
            isActive = selectedDate == secondDate
        ) {
            viewModel.onIntent(DeviceMainIntent.SelectDate(date = secondDate))
        }

        VerticalDivider(modifier = Modifier.fillMaxHeight(0.7f), color = colors.onSurface.muted(alpha = 0.1f))

        ScheduleNavItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.dayAfterTomorrow),
            date = thirdDate,
            isActive = selectedDate == thirdDate
        ) {
            viewModel.onIntent(DeviceMainIntent.SelectDate(date = thirdDate))
        }
    }
}
