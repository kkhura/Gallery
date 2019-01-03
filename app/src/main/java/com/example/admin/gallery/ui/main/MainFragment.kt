package com.example.admin.gallery.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin.gallery.R
import com.example.admin.gallery.model.ImageModel
import com.example.admin.gallery.quotesHome.adapter.ImageAdapter
import org.json.JSONObject
import androidx.recyclerview.widget.GridLayoutManager
import android.graphics.BitmapFactory
import java.nio.file.Files.exists
import android.graphics.Bitmap
import java.io.*


class MainFragment : Fragment() {

    private lateinit var imagesViewModel: MainViewModel
    private var rowCount = 2
    private val imagesList: ArrayList<ImageModel> = ArrayList()
    private lateinit var rvList: RecyclerView
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private val url = "https://pixabay.com/api/?key=11157264-3f23a341cddc8a257f65b666f"
    private var pageNumber = 1;
    private var searchstring = ""
    private var isApiCall: Boolean = false
    private var totalCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvList = view!!.findViewById<RecyclerView>(R.id.rvList)
        rvList.adapter = ImageAdapter(imagesList, this.activity!!)
        rvList.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, rowCount)

        imagesViewModel = ViewModelProviders.of(this).get(MainViewModel(activity!!.application)::class.java)
        imagesViewModel.getImagesList().observe(this, Observer { listImages ->
            if (listImages != null) {
                imagesList.addAll(listImages)
                rvList.adapter!!.notifyDataSetChanged()
            }
        })
        var scrollListener = object : RecyclerView.OnScrollListener() {
            private var previousTotal = 0
            private var loading = true
            private val visibleThreshold = 5
            var firstVisibleItem: Int = 0
            var visibleItemCount: Int = 0
            var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (totalCount <= imagesList.size) {
                    return
                }
                var visibleItemCount = recyclerView.childCount
                var totalItemCount = recyclerView.layoutManager!!.itemCount
                var firstVisibleItem = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    nextPage()
                    loading = true;
                }
            }

        }
        rvList.addOnScrollListener(scrollListener)
        nextPage()
    }

    private fun nextPage() {
        if (!isApiCall) {
            isApiCall = true
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, url + "&page=" + pageNumber + "&q=" + searchstring,
                    Response.Listener<String> { response ->
                        isApiCall = false
                        val jsonObject = JSONObject(response)
                        val hitsJsonArray = jsonObject.optJSONArray("hits")
                        for (i in 0..(hitsJsonArray.length() - 1)) {
                            val item = hitsJsonArray.getJSONObject(i)
                            val imageModel: ImageModel = ImageModel(item.optInt("id"), item.optString("previewURL"), item.optString("tags"))
                            imagesList.add(imageModel)
                        }

                        totalCount = jsonObject.optInt("total")
                        var handler: Handler = Handler(Looper.getMainLooper())
                        handler.post {
                            rvList.adapter!!.notifyDataSetChanged()
                        }
                        if (totalCount == imagesList.size) {
                            Toast.makeText(context, "Loaded all images", Toast.LENGTH_SHORT).show()
                        }
                        pageNumber++;
                    },
                    Response.ErrorListener {
                        isApiCall = false
                    })

            queue!!.add(stringRequest)
        }
    }

    fun getBitmap(context: Context, url: String): Bitmap? {
        val CACHE_PATH = context.cacheDir.absolutePath + "/picasso-cache/"

        val files = File(CACHE_PATH).listFiles()
        for (file in files) {
            val fname = file.getName()
            if (fname.contains(".") && fname.substring(fname.lastIndexOf(".")) == ".0") {
                try {
                    val br = BufferedReader(FileReader(file))
                    if (br.readLine().equals(url)) {
                        val image_path = CACHE_PATH + fname.replace(".0", ".1")
                        if (File(image_path).exists()) {
                            return BitmapFactory.decodeFile(image_path)
                        }
                    }
                } catch (e: FileNotFoundException) {
                } catch (e: IOException) {
                }

            }
        }
        return null
    }


    private lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        this.menu = menu

        if (searchItem != null) {
            searchView = searchItem!!.getActionView() as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    pageNumber = 1
                    imagesList.clear()
                    searchstring = query
                    nextPage()
                    return true
                }
            }

            var closeListener = object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    pageNumber = 1
                    imagesList.clear()
                    searchstring = ""
                    nextPage()
                    return true
                }
            }
            searchView!!.setOnCloseListener(closeListener)
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_search ->
                // Not implemented here
                return false
            else -> {
                val popup = PopupWindow(context)
                var layout: View = getLayoutInflater().inflate(R.layout.popup_grid, null);
                popup.setContentView(layout);
                popup.setOutsideTouchable(true);
                popup.setFocusable(true);
                popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
                val two = layout.findViewById<TextView>(R.id.two)
                val three = layout.findViewById<TextView>(R.id.three)
                val four = layout.findViewById<TextView>(R.id.four)

                two.setOnClickListener {
                    rowCount = 2
                    rvList.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, rowCount)
                    popup.dismiss()
                }

                three.setOnClickListener {
                    rowCount = 3
                    rvList.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, rowCount)
                    popup.dismiss()
                }

                four.setOnClickListener {
                    rowCount = 4
                    rvList.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, rowCount)
                    popup.dismiss()
                }

            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
