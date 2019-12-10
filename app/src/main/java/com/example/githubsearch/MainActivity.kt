package com.example.githubsearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton.setOnClickListener {
            startListViewActivity()
        }
    }

    private fun startListViewActivity() {
        val searchFieldText = searchField.text

        if(searchFieldText == null || searchFieldText.isEmpty()){
            Toast.makeText(this,  "Enter a repository name!", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(this, ListOfRepositoriesActivity::class.java)
            intent.putExtra("keyword", searchFieldText)
            startActivity(intent)
        }
    }

}







