package io.liaojie1314.musiccompose.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.gcode.vasttools.utils.ToastUtils
import io.liaojie1314.musiccompose.model.LocalMusicBean
import io.liaojie1314.musiccompose.ui.components.MainActRV
import io.liaojie1314.musiccompose.ui.components.MainActSV
import io.liaojie1314.musiccompose.ui.components.MainActTopBar
import io.liaojie1314.musiccompose.ui.theme.MyTheme
import io.liaojie1314.musiccompose.ui.theme.bottom_layout_main_bg_color
import io.liaojie1314.musiccompose.viewModel.MainViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.permissionx.guolindev.PermissionX
import io.liaojie1314.musiccompose.R

class MainActivity : FragmentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window,false)

        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    ToastUtils.showShortMsg(this, "所有权限已经授权,如果没有歌曲显示请重启应用")
                } else {
                    ToastUtils.showShortMsg(this, "以下权限未被授予$deniedList")
                }
            }

        setContent {
            MyTheme(darkTheme = false) {
                ProvideWindowInsets {
                    Surface {
                        rememberSystemUiController().setStatusBarColor(
                            Color.Transparent, darkIcons = MaterialTheme.colors.isLight)

                        var expanded by remember { mutableStateOf(false) }

                        Column {
                            Spacer(modifier = Modifier
                                .statusBarsHeight()
                                .fillMaxWidth().background(MaterialTheme.colors.primarySurface))

                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                scaffoldState = rememberScaffoldState(),
                                topBar = {
                                    val title = stringResource(id = R.string.app_name)

                                    MainActTopBar(
                                        title = { Text(title) },
                                        icon = {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_app),
                                                modifier = Modifier.size(40.dp),
                                                contentDescription = "App图标"
                                            )
                                        },
                                        more = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_search),
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .padding(5.dp)
                                                        .clickable {
                                                            expanded = !expanded
                                                            viewModel.onExpandedChanged(expanded)
                                                        },
                                                    contentDescription = "搜索按钮"
                                                )
                                            }
                                        },
                                    )
                                },
                                content = {
                                    Column {
                                        MainActSV()
                                        MainActRV(Modifier.weight(1f))
                                        BottomControlLayout()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopMusic()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun BottomControlLayout() {

        val localMusicBean: LocalMusicBean by viewModel.localMusicBean.observeAsState(
            LocalMusicBean()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .defaultMinSize(0.dp, 80.dp)
                .background(bottom_layout_main_bg_color),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 歌曲图标
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "歌曲图标",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            // 歌曲信息
            Column(Modifier.fillMaxWidth().weight(2f)) {
                Text(localMusicBean.song, style = TextStyle(color = Color.White))
                Text(localMusicBean.singer, style = TextStyle(color = Color.White))
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically, //设置垂直方向对齐
                horizontalArrangement = Arrangement.spacedBy(10.dp) //设置子项的间距
            ) {

                val isPlaying by viewModel.isPlaying.observeAsState(false)

                // 上一首按钮
                Image(painter = painterResource(id = R.drawable.ic_last),
                    contentDescription = "上一首",
                    modifier = Modifier
                        .clickable { viewModel.playLastMusic() }
                        .size(30.dp)
                )

                // 播放暂停按钮
                Image(painter = if (isPlaying) {
                    painterResource(id = R.drawable.ic_pause)
                } else {
                    painterResource(id = R.drawable.ic_play)
                },
                    contentDescription = "播放或者暂停",
                    modifier = Modifier
                        .clickable { viewModel.playCurrentMusic() }
                        .size(40.dp)
                )

                // 下一首按钮
                Image(painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "下一首",
                    modifier = Modifier
                        .clickable { viewModel.playNextMusic() }
                        .size(30.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun DefaultPreview() {
    MainActTopBar(
        title = { Text("GMusic") },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_app),
                modifier = Modifier.size(40.dp),
                contentDescription = "App图标"
            )
        },
        more = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search), modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp), contentDescription = "搜索按钮"
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_theme), modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp), contentDescription = "主题设置按钮"
                )
            }
        }
    )
}