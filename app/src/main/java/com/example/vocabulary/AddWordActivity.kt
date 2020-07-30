package com.example.vocabulary

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_word.*
import java.io.PrintStream

class AddWordActivity : AppCompatActivity() {
    private val WORDS_FILE_NAME = "extrawords.txt"
    private lateinit var wordMap: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        wordMap = intent.getSerializableExtra("wordMap") as HashMap<String, String>
    }

    fun letsAddTheWord(view: View) {
        var word = word_to_add.text.toString()
        if (!wordMap.containsKey(word)) {
            var defination = word_defination.text.toString()

            var line = "$word\t$defination"

            // todo: write new line into the extrawords.txt file
            var outStream = PrintStream(openFileOutput(WORDS_FILE_NAME, Context.MODE_PRIVATE))
            outStream.write(line.toByteArray())
            outStream.close()

            var myIntent = Intent()
            myIntent.putExtra("newWord", word)
            myIntent.putExtra("newDefination", defination)
            setResult(Activity.RESULT_OK, myIntent)
            finish()
            Toast.makeText(this, "Add successfully", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Word already exists", Toast.LENGTH_SHORT).show()
    }
}
