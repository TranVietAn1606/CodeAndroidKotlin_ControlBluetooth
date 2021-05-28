package com.vku.tvan.blutoothcarcontrol

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfomationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infomation_main)
        val tv1 = findViewById<TextView>(R.id.tv1)
        tv1.append("Control Car by Bluetooth" +"\nCreator: TVA"+"\nVs:CB01")
        val tv2 = findViewById<TextView>(R.id.tv2)
        tv2.append("Ứng dụng Control Car By Bluetooth là ứng dụng hỗ trợ trong việc lập trình Arduino tạo xe điều khiển thông qua sóng Bluetooth." +
                " Ứng dụng kết nối với các mudule Bluetooth và truyền các tính hiệu dạng kí tự. Sau khi kết nối thành công," +
                " Arduino thông qua module Bluetooth nhận và cài đặt các tín hiệu đó để điều khiển các hướng chuyển động của xe cũng như các thiết bị đi kèm như đèn và còi")
    }
}