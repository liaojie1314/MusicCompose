package io.liaojie1314.musiccompose.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.liaojie1314.musiccompose.viewModel.MainViewModel

/**
 * 解决方案参考
 * https://www.reddit.com/r/android_devs/comments/jff2vp/jetpack_compose_am_i_stupid_or_is_the_by_remember/
 */
@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalAnimationApi
@Composable
fun MainActSV(
    vm: MainViewModel = viewModel()
) {
    // 搜索框可见性
    val visible by vm.expanded.observeAsState(false)

    // 待搜索的歌曲
    var targetSong by rememberSaveable { mutableStateOf("") }

    AnimatedVisibility(
        visible = visible
    ) {
        Surface(
            Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = targetSong,
                    onValueChange = {
                        targetSong = it
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            vm.searchSong(it)
                        }
                    },
                    label = { Text("请输入歌曲名") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, "搜索")
                    },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun DefaultPreview() {

}