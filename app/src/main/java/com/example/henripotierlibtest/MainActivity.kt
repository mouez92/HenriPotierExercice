package com.example.henripotierlibtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import com.example.henripotierlibtest.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.example.henripotierlibtest.books.Book
import com.example.henripotierlibtest.books.MainViewModel
import com.example.henripotierlibtest.books.MyViewModelFactory
import com.example.henripotierlibtest.di.RetrofitService
import com.example.henripotierlibtest.offers.OfferActivity
import com.example.henripotierlibtest.repository.MainRepository
import com.example.henripotierlibtest.rv_tools.MyItemDetailsLookup
import com.example.henripotierlibtest.rv_tools.MyItemKeyProvider

class MainActivity : AppCompatActivity() , ActionMode.Callback {
    private var selectedPostItems: MutableList<Book> = mutableListOf()
    private var actionMode: ActionMode? = null

    lateinit var viewModel: MainViewModel
    //private val adapter = BookAdapter()
    private val adapter = BookAdapterNew()

    lateinit var binding: ActivityMainBinding

    private var tracker: SelectionTracker<Book>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(MainViewModel::class.java)


        viewModel.bookList.observe(this) {
            adapter.setMovies(it)


        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.getAllBooks()

        //************************
        val booksRecyclerView: RecyclerView = findViewById(R.id.recyclerview)

        tracker = SelectionTracker.Builder<Book>(
            "mySelection",
            booksRecyclerView,
            MyItemKeyProvider(adapter),
            MyItemDetailsLookup(booksRecyclerView),
            StorageStrategy.createParcelableStorage(Book::class.java)
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Book>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    tracker?.let {
                        selectedPostItems = it.selection.toMutableList()
                        if (selectedPostItems.isEmpty()) {
                            actionMode?.finish()
                        } else {
                            if (actionMode == null) actionMode =
                                startSupportActionMode(this@MainActivity)

                            actionMode?.title =
                                "${selectedPostItems.size}"
                        }
                    }
                }
            })

        //*/
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_view_offer -> {
                //Log.e("action_view_delete","selectedPostItems size = "+selectedPostItems.size)
                //Log.e("action_view_delete","selectedPostItems toString = "+  selectedPostItems.toString())
                /*
                Toast.makeText(
                    this,
                    selectedPostItems.toString(),
                    Toast.LENGTH_LONG
                ).show()
                */
                var ISBNs:String =""
                var value:Int =0
                selectedPostItems.forEachIndexed { index, element ->
                    ISBNs = ISBNs+element.isbn
                    value = value+element.price

                    if(index<selectedPostItems.size-1){
                        ISBNs = ISBNs+","
                    }
                }
                //Log.e("action_view_delete","ISBNs = "+ ISBNs)

                val intent = Intent(this, OfferActivity::class.java)
                intent.putExtra("ISBNs", ISBNs)
                intent.putExtra("price", value)

                startActivity(intent)
            }


        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

        mode?.let {
            val inflater: MenuInflater = it.menuInflater
            inflater.inflate(R.menu.action_mode_menu, menu)
            return true
        }
        return false
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        adapter.tracker?.clearSelection()
        adapter.notifyDataSetChanged()
        actionMode = null
    }
}