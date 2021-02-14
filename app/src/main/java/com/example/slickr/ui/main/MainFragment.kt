package com.example.slickr.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.slickr.R
import com.example.slickr.api.model.Photo
import com.example.slickr.api.model.SearchModel
import com.example.slickr.model.ItemModel
import com.example.slickr.ui.utilities.ImageAdapter
import com.example.slickr.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class MainFragment : Fragment() {
    private var gridView: GridView? = null
    private var arrayList: ArrayList<ItemModel>? = null
    private var myAdapter: ImageAdapter? = null
    private var fragView: View? = null
    private var editText: EditText? = null
    private var button: Button? = null
    private var searchTag: String = ""
    private var progressBar: ProgressBar? = null
    private var noResultText: TextView? = null
    private var dialog: AlertDialog? = null


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        fragView = inflater.inflate(R.layout.main_fragment, container, false)
        return fragView!!

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button = fragView?.findViewById(R.id.button)
        button?.setOnClickListener(View.OnClickListener { search() })

        editText = fragView?.findViewById(R.id.search_text)

        gridView = fragView?.findViewById(R.id.gridid)
        arrayList = ArrayList()
        myAdapter = ImageAdapter(activity?.applicationContext, arrayList!!)
        gridView?.adapter = myAdapter

        // Use the ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        excuteApi("Nature")

        // progress dialog
        progressBar = fragView?.findViewById(R.id.progressBar)
        progressBar?.visibility = View.VISIBLE

        //the no result text
        noResultText = fragView?.findViewById(R.id.no_result_text)
        noResultText?.visibility = View.INVISIBLE


        val builder = AlertDialog.Builder(activity?.applicationContext)
        builder.setTitle(" :( ")
        builder.setMessage("something went wrong /n please check your internet connection")
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        dialog = builder.create()
        changedOriantation()
    }

    fun search() {
        searchTag = editText?.text.toString()
        progressBar?.visibility = View.VISIBLE
        excuteApi(searchTag)
    }

    private fun excuteApi(tag: String) {
        try {


            viewModel.getSearchData(tag)
            viewModel.myResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    noResultText?.visibility = View.INVISIBLE
                    //photos == null
                    // photos.photo -> photos list is empty
                    if (response.body()?.photos?.photo?.isEmpty() == true || response.body()?.photos == null) {
                        noResultText?.visibility = View.VISIBLE
                        Log.d("msg", response.message().toString())
                    }

                    setDataList(response)
                    Log.d("response", response.body()?.photos.toString())
                    Log.d("response", response.body()?.stat)
                } else {
                    dialog?.show()
                    //Toast.makeText(applicationContext,"OK",Toast.LENGTH_SHORT).show()
                    Log.d("response", response.errorBody().toString())

                }
            })

        } catch (e: SocketTimeoutException) {
            Log.d("caught", e.toString())
        }
    }

    private fun setDataList(res: Response<SearchModel>) {
        arrayList?.clear()
        res.body()?.photos?.photo?.forEach { item: Photo ->
            //https://live.staticflickr.com/{server-id}/{id}_{secret}.jpg
            var url = "https://live.staticflickr.com/${item.server}/${item.id}_${item.secret}.jpg"
            var itemModel: ItemModel = ItemModel(url)
            Log.d("url", url)
            arrayList?.add(itemModel)

        }
        myAdapter?.notifyDataSetChanged()
        gridView?.adapter = myAdapter
        CoroutineScope(IO).launch {
            delay(1000)
            progressBar?.visibility = View.INVISIBLE
        }

        Log.d("url", res.raw().request.toString())

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        gridView?.numColumns = if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) 3 else 2
        super.onConfigurationChanged(newConfig)
    }
    fun changedOriantation (){
        var curruntOriantation = resources.configuration.orientation
        gridView?.numColumns = if (curruntOriantation === Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    }

}