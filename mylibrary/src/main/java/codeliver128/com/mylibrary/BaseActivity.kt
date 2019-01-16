package codeliver128.com.mylibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this,"Hello welcome to lib ",Toast.LENGTH_SHORT).show()
    }
}
