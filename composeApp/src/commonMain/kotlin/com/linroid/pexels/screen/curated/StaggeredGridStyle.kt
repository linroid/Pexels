package com.linroid.pexels.screen.curated

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class StaggeredGridStyle(
	val columnCount: Int,
	val horizontalPadding: Dp,
	val verticalPadding: Dp,
	val horizontalMargin: Dp,
	val verticalMargin: Dp,
	val avatarSize: Dp,
	val cardRadius: Dp,
	val photographerFontSize: TextUnit,
	val photographerPadding: Dp,
) {
	Compact(
		columnCount = 1,
		horizontalPadding = 4.dp,
		verticalPadding = 8.dp,
		horizontalMargin = 2.dp,
		verticalMargin = 8.dp,
		avatarSize = 28.dp,
		cardRadius = 4.dp,
		photographerFontSize = 14.sp,
		photographerPadding = 4.dp
	),
	Medium(
		columnCount = 2,
		horizontalPadding = 4.dp,
		verticalPadding = 8.dp,
		horizontalMargin = 2.dp,
		verticalMargin = 8.dp,
		avatarSize = 28.dp,
		cardRadius = 4.dp,
		photographerFontSize = 14.sp,
		photographerPadding = 4.dp
	),
	Expanded(
		columnCount = 3,
		horizontalPadding = 16.dp,
		verticalPadding = 16.dp,
		horizontalMargin = 8.dp,
		verticalMargin = 16.dp,
		avatarSize = 48.dp,
		cardRadius = 8.dp,
		photographerFontSize = 24.sp,
		photographerPadding = 8.dp
	);

	companion object {
		fun fromWidth(width: Dp): StaggeredGridStyle {
			return if (width < 200.dp) {
				Compact
			} else if (width < 600.dp) {
				Medium
			} else {
				Expanded
			}
		}
	}
}