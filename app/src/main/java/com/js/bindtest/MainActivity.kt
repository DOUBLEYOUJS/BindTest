package com.js.bindtest

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.js.lib_annotations.BindLayout
import com.js.lib_annotations.BindView
import com.js.lib_annotations.OnClick
import com.js.lib_api.BindApi
import kotlinx.android.synthetic.main.activity_main.*

@BindLayout(R.layout.activity_main)
class MainActivity : AppCompatActivity() {

    @BindView(R.id.tv_hello)
    lateinit var textView: TextView
    @BindView(R.id.bt_click)
    lateinit var btClick: Button

    private var mClickBtNum = 0
    private var mClickTvNum = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        BindApi.bind(this)

        textView.text = "测试成功......"
        btClick.text = "点击0次"
    }

    @OnClick(R.id.bt_click, R.id.tv_hello)
    fun onClick(view: View) {
        when (view.id) {
            R.id.bt_click -> {
                mClickBtNum++
                btClick.text = "点击${mClickBtNum}次"
            }
            R.id.tv_hello -> {
                mClickTvNum++
                textView.text = "点击文字${mClickTvNum}次"
            }
        }
    }
}
