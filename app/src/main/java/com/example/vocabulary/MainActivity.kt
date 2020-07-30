package com.example.vocabulary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private var words = ArrayList<String>()
    private var wordMap = HashMap<String, String>()
    private var defns = ArrayList<String>()
    private lateinit var myAdapter: ArrayAdapter<String>

    private val ADD_WORD_INTENT_CODE = 1931 //between 1-65535

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        readGreWords()

        setupWordAndDefns()

        definations_list.setOnItemClickListener { parent, view, position, id ->
            var word = the_word.text.toString()
            if (defns[position] != wordMap[word]) {
                Toast.makeText(this, "You are wrong!", Toast.LENGTH_SHORT).show()
                defns.removeAt(position)
                myAdapter.notifyDataSetChanged()
            }
            else {
                Toast.makeText(this, "Correct!!", Toast.LENGTH_SHORT).show()
                setupWordAndDefns()
            }
        }
    }


    private fun readGreWords() {
        var reader = Scanner(resources.openRawResource(R.raw.grewords))
        while (reader.hasNextLine()) {
            var line = reader.nextLine()
            // Log.d("Oli", "the line is $line")
            var pieces = line.split('\t')
            if (pieces.size >= 2) {
                words.add(pieces[0])
                wordMap.put(pieces[0], pieces[1])
            }
        }
    }

    private fun setupWordAndDefns() {
        var rand = Random()
        var index = rand.nextInt(words.size)
        var word = words[index]

        the_word.text = word

        defns.clear()
        defns.add(wordMap[word]!!)
        words.shuffle()
        var i = 0
        while (defns.size < 5) {
            var otherWord = words[i]
            if (otherWord == word) {
                i++
                continue
            }
            defns.add(wordMap[otherWord]!!)
            i++
        }
        defns.shuffle()

        myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defns)

        definations_list.adapter = myAdapter
    }

    fun addWordButtonClick(view: View) {
        var myIntent = Intent(this, AddWordActivity::class.java)
        myIntent.putExtra("wordMap", wordMap)
        startActivityForResult(myIntent, ADD_WORD_INTENT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, myIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, myIntent)
        if (requestCode == ADD_WORD_INTENT_CODE) {
            if (myIntent != null) {
                var newWord = myIntent.getStringExtra("newWord")
                var newDefination = myIntent.getStringExtra("newDefination")
                wordMap[newWord] = newDefination
                words.add(newWord)
            }

        }
    }
}
