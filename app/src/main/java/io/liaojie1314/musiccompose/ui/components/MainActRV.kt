package io.liaojie1314.musiccompose.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import io.liaojie1314.musiccompose.model.LocalMusicBean
import io.liaojie1314.musiccompose.viewModel.MainViewModel
import java.util.*

/**
 * 歌曲播放列表
 * @param modifier
 * @param vm
 */
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainActRV(
    modifier: Modifier = Modifier,
    vm: MainViewModel = viewModel()
) {
    val targetSong by vm.targetSong.observeAsState(ArrayList<LocalMusicBean>())

    LazyColumn(modifier = modifier.background(Color(0xFFdcdde1))) {
        items(
            items = targetSong
        ){
            LocalMusicRVItem(localMusicBean = it)
        }
    }
}