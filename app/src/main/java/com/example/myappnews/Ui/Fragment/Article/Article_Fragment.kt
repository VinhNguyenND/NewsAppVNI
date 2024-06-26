package com.example.myappnews.Ui.Fragment.Article

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Api.Dictionary.DicViewModel
import com.example.myappnews.Data.Api.TextToSpeech.ObjectAudio.FileWriter
import com.example.myappnews.Data.Api.TextToSpeech.Repository
import com.example.myappnews.Data.Firebase.ViewModel.AdminViewModel.AdminViewModel
import com.example.myappnews.Data.Firebase.ViewModel.ArticleViewModel.ArViewModel
import com.example.myappnews.Data.Local.Article.Down.ArticleDownEntity
import com.example.myappnews.Data.Local.Article.Down.ArticleDownViewModel
import com.example.myappnews.Data.Local.Article.History.ArticleEntity
import com.example.myappnews.Data.Local.Article.History.ArticlelocalViewModel
import com.example.myappnews.Data.Local.Dictionary.Entity.DictionaryItem
import com.example.myappnews.Data.Local.Dictionary.Helper.DictionaryViewModel
import com.example.myappnews.Data.Model.Article.Article
import com.example.myappnews.Data.Model.Article.NewsArticle
import com.example.myappnews.Data.SharedPreferences.Shared_Preference
import com.example.myappnews.Interface.Adapter.CommonAdapter
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.Article.Article_Fragment.Companion.fromDateToString
import com.example.myappnews.Ui.Fragment.Article.Article_Fragment.Companion.fromStringToDate
import com.example.myappnews.Ui.Fragment.Home.Adapt.ArticleAdapter
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.ArticleScreenBinding
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class Article_Fragment : Fragment() {
    private lateinit var binding: ArticleScreenBinding
    private lateinit var articlelocalViewModel: ArticlelocalViewModel
    private lateinit var DictionaryFolder1: DictionaryViewModel
    private lateinit var articleDownViewModel: ArticleDownViewModel
    private val _adminViewModel = AdminViewModel.getInstance()
    private val ArticleViewModel = ArViewModel.getInstance();
    private var listArticle = ArrayList<NewsArticle>();
    private lateinit var _articleAdapter: ArticleAdapter
    private lateinit var viewModel: DicViewModel
    private var viewPopup: View? = null
    private var bodyPopup: View? = null;
    private var word: TextView? = null;
    private lateinit var speech: String;
    private var phonetic: TextView? = null;
    private var audioPhontic: String = "";
    private var meanWord: TextView? = null;
    private val apiKey = "76373408e2mshdd1b501acbcbf46p1b09c4jsn6300c60e03f5"
    private val apiHost = "text-to-speech27.p.rapidapi.com"
    private val repository = Repository.getInstance();
    private var mp: MediaPlayer? = null
    private var isPlaying = false
    private var isLike = false
    private lateinit var _shared_Preference: Shared_Preference;
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var article: NewsArticle;
    private lateinit var note: DictionaryItem;
    private var idDoc = "";
    private var startIndex: Int = 0;
    private var endIndex: Int = 0;
    private var content: View? = null;
    private var loading: ShimmerFrameLayout? = null;
    private var nothing: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArticleScreenBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initShimer()
//        setEventforBack()
        setEventBottomMedia()
        initializeMediaPlayer()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _shared_Preference = Shared_Preference(requireActivity());
        setEventTouchText(binding.txtPageContent)
        initViewModel()
        observeDic()
        setEvent(view)
        initRcView(requireContext())
        initContent()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        observeSpeech()
    }


    override fun onPause() {
        super.onPause()
        speech = "";
    }

    private fun initRcView(context: Context) {
        _articleAdapter = ArticleAdapter(listArticle, context)
        binding.arRcv.let {
            it.adapter = _articleAdapter
            it.layoutManager = LinearLayoutManager(
                parentFragment?.requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
        _articleAdapter.setClickListener(object : CommonAdapter {
            override fun setOnClickListener(position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("Article", listArticle[position])
                Navigation.findNavController(binding.root).navigate(R.id.article_Fragment, bundle)
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DicViewModel::class.java]
        articlelocalViewModel = ViewModelProvider(this).get(ArticlelocalViewModel::class.java)
        DictionaryFolder1 = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        articleDownViewModel = ViewModelProvider(this).get(ArticleDownViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", NewsArticle::class.java) != null) {
            article = arguments?.getParcelable("Article", NewsArticle::class.java)!!
            val text: String = article.content!!.replace("\\\\n", "<br/>" + " ");
            if (arguments?.getInt("ishistory") == 0) {
                binding.txtRelate.visibility = View.GONE
                binding.arRcv.visibility = View.GONE
            }
            if (_shared_Preference.getUid()?.let { article.like?.contains(it) } == true) {

                isLike = true;
                binding.btnLike.setImageResource(R.drawable.ic_heart_red_24)
            } else {
                isLike = false
                binding.btnLike.setImageResource(R.drawable.icheart24)
            }
            article.idArticle?.let {
                _adminViewModel.getIdDoc(it).observe(
                    viewLifecycleOwner,
                    Observer {
                        idDoc = it
                    }
                )
            }
            ArticleViewModel.getArticleField(article).observe(
                viewLifecycleOwner,
                Observer {
                    listArticle = it
                    _articleAdapter.submitList(listArticle)
                }
            )
            callTextToSpeech(text)
            articlelocalViewModel.addArticle(toArticleEntity(article))
            binding
            binding.txtPageContent.text = Html.fromHtml(text)
            binding.txtPageTime.text = article.pubDate.toString()
            binding.articlePageTittle.text = article.titleArticle.toString()
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
        }
    }


    private fun initializeMediaPlayer() {
        mp = MediaPlayer();
        mp!!.setOnPreparedListener {
            binding.seekPageContent.max = mp!!.duration
        }
        binding.StartOrStop.visibility = View.INVISIBLE
    }


    private fun observeDic() {
        viewModel.word.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    nothing?.visibility = View.VISIBLE
                    content?.visibility = View.INVISIBLE
                    loading?.stopShimmer()
                    loading?.visibility = View.INVISIBLE
                } else {
                    Log.i("tu dien  getit", it.toString())
                    word!!.text = it[0].wordMean.toString()
                    phonetic!!.text = it[0].phonetic
                    for (doc in it) {
                        for (doc1 in doc.phonetics) {
                            if (doc1.audio.isNotEmpty()) {
                                audioPhontic = doc1.audio
                            }
                        }
                    }
                    meanWord!!.text = it[0].meanings[0].toString()
                    note = DictionaryItem(
                        idDictionaryFolder = 0,
                        idDictionaryItem = 0,
                        word = it[0].word.toString(),
                        phonetic = it[0].phonetic.toString(),
                        audio = "",
                        mean = it[0].meanings[0].toString(),
                        wordMean = it[0].wordMean.toString(),
                    )
                    nothing?.visibility = View.GONE
                    content?.visibility = View.VISIBLE
                    loading?.stopShimmer()
                    loading?.visibility = View.GONE
                }
            } else {
                Log.i("tu dien khong ton tai", "tu dien khong ton tai")
            }

        })
        viewModel.translateWord.observe(viewLifecycleOwner, Observer {
            Log.i("tu da dich la:", it.trans)
        })
    }

    private fun callGetWord(word: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val elapsedTime = measureTimeMillis {
                viewModel.listWord(word)
            }
            println("Elapsed Time: $elapsedTime ms")
        }
    }

    private fun callTextToSpeech(content: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            repository.callTextToSpeech(
                requireActivity(),
                content,
                apiKey,
                apiHost
            )
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setEventTouchText(textView: TextView) {
        textView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                handleTouchEvent(event!!, textView);
                return false
            }
        })
    }

//    private fun setEventforBack() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                isEnabled = false
//                isEnabled = if (viewPopup != null) {
//                    binding.rlLayout.removeView(viewPopup)
//                    false
//                } else {
//                    true
//                }
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//    }


    private fun setEventBottomMedia() {
        binding.StartOrStop.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }
        binding.seekPageContent.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                if (fromUser) {
                    mp?.seekTo(progress)
                    binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                }
                if (seekBar != null) {
                    if (progress == seekBar.max) {
                        seekBar.progress = 0;
                        binding.textNow.text = "0:00"
                        binding.StartOrStop.setImageResource(R.drawable.ic_play24)
                        isPlaying = false
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        updateSeekBar()
    }


    private fun initShimer() {
        binding.audioHide.visibility = View.VISIBLE
        binding.audioVisible.visibility = View.GONE
        binding.audioHide.showShimmer(true)
        binding.audioHide.startShimmer()
    }

    private fun setEvent(view: View) {
        binding.btnCapture.setOnClickListener {
            getBitmapFromView(view, requireActivity()) {
                shareBitmap(it)
            }
        }
        binding.btnbackar.setOnClickListener {
            view.findNavController().popBackStack();
        }
        binding.btnArshare.setOnClickListener {
            if (article.linkArticle != null) {
                shareUri(article.linkArticle.toString())
            }
        }
        binding.decree5s.setOnClickListener {
            seekBackward(5000)
        }
        binding.btnGo5s.setOnClickListener {
            seekForward(5000)
        }
        binding.comment.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Article", article)
            bundle.putInt("ishistory", 1)
            Navigation.findNavController(binding.root).navigate(R.id.commentFragment, bundle)
        }

        binding.downArticle.setOnClickListener {
            if (_shared_Preference.getUid()?.isNotEmpty() == true) {
                FileWriter.saveToInternal(article.idArticle.toString() + ".tmp", requireContext())
                    .observe(viewLifecycleOwner,
                        Observer {
                            if (it != null) {
                                val Article = ArticleDownEntity(
                                    idArticle = article.idArticle.toString(),
                                    titleArticle = article.titleArticle.toString(),
                                    linkArticle = article.linkArticle.toString(),
                                    creator = article.creator.toString(),
                                    content = article.content.toString(),
                                    pubDate = article.pubDate.toString(),
                                    imageUrl = article.imageUrl.toString(),
                                    sourceUrl = article.sourceUrl.toString(),
                                    sourceId = article.sourceId.toString(),
                                    country = article.country.toString(),
                                    field = article.field.toString(),
                                    sourceVoice = it.toString(),
                                )
                                articleDownViewModel.addArticle(Article)
                                articleDownViewModel.isArticleInserted.observe(
                                    viewLifecycleOwner,
                                    Observer { isSuccess ->
                                        if (isSuccess) {
                                            showToast(requireContext(), "Article down successfully")
                                        } else {
                                            showToast(requireContext(), "Failed to down article")
                                        }
                                    })
                            } else {
                                showToast(requireContext(), "Failed to down article")
                            }
                        })
            } else {
                showToast(requireContext(), "Bạn cần phải đăng nhập")
            }

        }

        binding.btnLike.setOnClickListener {
            if (_shared_Preference.getUid()?.isNotEmpty() == true) {
                if (isLike) {
                    _shared_Preference.getUid()
                        ?.let { it1 ->
                            ArticleViewModel.doLike(
                                idDoc,
                                newsArticle = article,
                                it1,
                                false
                            )
                        }
                    isLike = false;
                    binding.btnLike.setImageResource(R.drawable.icheart24)
                } else {
                    _shared_Preference.getUid()
                        ?.let { it1 ->
                            ArticleViewModel.doLike(
                                idDoc,
                                newsArticle = article,
                                it1,
                                true
                            )
                        }
                    isLike = true;
                    binding.btnLike.setImageResource(R.drawable.ic_heart_red_24)
                }
            } else {
                showToast(requireContext(), "Bạn cần phải đăng nhập")
            }

        }
    }

    private fun observeSpeech() {
        repository.TextToSpechApi.observe(viewLifecycleOwner, Observer {
            speech = it;
            mp?.setDataSource(it)
            mp?.prepare()
            binding.textEnd.text = mp?.duration?.let { it1 -> formatDuration(it1.toLong()) }
            binding.StartOrStop.visibility = View.VISIBLE
            binding.downArticle.visibility = View.VISIBLE
            binding.accelerate.visibility = View.INVISIBLE
            binding.audioHide.stopShimmer()
            binding.audioHide.hideShimmer()
            binding.audioHide.visibility = View.GONE
            binding.audioVisible.visibility = View.VISIBLE
        })
    }

    private fun updateSeekBar() {
        handler.postDelayed({
            binding.seekPageContent.progress = mp?.currentPosition ?: 0
            updateSeekBar()
        }, 100)
    }

    private fun playAudio() {
        mp?.start()
        isPlaying = true
        binding.StartOrStop.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseAudio() {
        mp?.pause()
        isPlaying = false
        binding.StartOrStop.setImageResource(android.R.drawable.ic_media_play)
    }

//    private fun stopAudio() {
//        mp?.stop()
//        mp?.prepareAsync()
//        mp?.seekTo(0)
//        isPlaying = false
//        binding.StartOrStop.setImageResource(android.R.drawable.ic_media_play)
//    }

    private fun seekForward(milliSeconds: Int) {
        val currentPosition = mp?.currentPosition
        val duration = mp?.duration
        if (currentPosition != null) {
            if (currentPosition + milliSeconds < duration!!) {
                mp?.seekTo(currentPosition + milliSeconds)
                binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                binding.seekPageContent.progress = currentPosition + milliSeconds
            } else {
                mp?.seekTo(duration)
                binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                binding.seekPageContent.progress = duration
                mp?.pause()
            }
        }

    }

    private fun seekBackward(milliSeconds: Int) {
        val currentPosition = mp?.currentPosition

        if (currentPosition != null) {
            if (currentPosition - milliSeconds > 0) {
                mp?.seekTo(currentPosition - milliSeconds)
                binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                binding.seekPageContent.progress = currentPosition - milliSeconds
            } else {
                mp?.seekTo(0)
                binding.textNow.text = mp?.currentPosition?.let { formatDuration(it.toLong()) }
                binding.seekPageContent.progress = 0
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speech = null.toString();
        binding.StartOrStop.visibility = View.INVISIBLE
        binding.accelerate.visibility = View.VISIBLE
        val tempFilePath = speech
        val fileToDelete = File(tempFilePath)
        if (fileToDelete.exists()) {
            fileToDelete.delete()
        }
        mp!!.release();
        mp = null;
    }


    private fun handleTouchEvent(event: MotionEvent, textView: TextView): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val touchX = event.x.roundToInt()
            val touchY = event.y.roundToInt()

            val layout: Layout? = textView.layout
            val line: Int = layout?.getLineForVertical(touchY) ?: -1
            val offset: Int = layout?.getOffsetForHorizontal(line, touchX.toFloat()) ?: -1

            if (line != -1 && offset != -1) {
                val charSequence: CharSequence = textView.text
                if (offset < charSequence.length) {
                    val touchedChar: Char = charSequence[offset]
                    val distanceToTop = (textView.y + layout?.getLineTop(line)!!) ?: 0

                    // In ra để kiểm tra
                    val (left, right) = findWordAtPosition(
                        textView.text.toString(),
                        offset
                    )
                    setSpanForWord(left, right);
                    startIndex = left;
                    endIndex = right;
                    Log.d("get get article when click:", textView.text.substring(left, right));
                    val margin_top = pxToDp(distanceToTop as Float).toInt() - getStatusBarHeight()
                    println("Distance to top: " + margin_top.toString())
                    if (textView.text.substring(left, right) != " ") {
                        callGetWord(textView.text.substring(left, right))
                        nothing?.visibility = View.INVISIBLE
                        loading?.visibility = View.VISIBLE
                        content?.visibility = View.INVISIBLE
                        addView(0, distanceToTop.toInt())
                    } else {
                        binding.rlLayout.removeView(viewPopup)
                    }
                }
            }
        }
        return true
    }

    fun pxToDp(px: Float): Float {
        val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics
        return px / (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            // Nếu không thể tìm thấy thông tin về status bar trong resources, bạn có thể trả về giá trị mặc định hoặc 0.
            0
        }
    }

    private fun getOffsetForEvent(textView: TextView, event: MotionEvent): Int {
        val x = event.x.toInt()
        val y = event.y.toInt()

        // Lấy layout của TextView
        val layout = textView.layout

        // Lấy dòng tương ứng với vị trí chạm
        val line = layout?.getLineForVertical(y)

        // Lấy offset cho vị trí chạm
        return line?.let {
            layout.getOffsetForHorizontal(it, x.toFloat())
        } ?: 0
    }

    private fun findWordAtPosition(input: String, position: Int): Pair<Int, Int> {
        var start = position
        var end = position
        while (start > 0 && input[start - 1].isLetter() && input[start].isLetter()) {
            start--
        }
        while (end < input.length - 1 && input[end + 1].isLetter() && input[end].isLetter()) {
            end++
        }
        return Pair(start, end + 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addView(x: Int, y: Int) {

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        val width = metrics.widthPixels
        val height = metrics.heightPixels

        binding.rlLayout.removeView(viewPopup)
        viewPopup = LayoutInflater.from(context).inflate(R.layout.popup_article, null)
        bodyPopup = viewPopup!!.findViewById(R.id.linearBody);
        word = viewPopup!!.findViewById(R.id.englishWord);
        content = viewPopup!!.findViewById(R.id.mainPopWord)
        loading = viewPopup!!.findViewById(R.id.shimmer_word_container)
        nothing = viewPopup!!.findViewById(R.id.popNothing)
        meanWord = viewPopup!!.findViewById(R.id.meanWordVietNam)
        phonetic = viewPopup!!.findViewById(R.id.phonetic1);
        val heart: ImageView = viewPopup!!.findViewById(R.id.heart);
        val audio: ImageView = viewPopup!!.findViewById(R.id.imageVoice);

        val layoutParams = RelativeLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
        )

        layoutParams.leftMargin = pxToDp(x.toFloat()).toInt() + width / 20 // x là tọa độ x bạn muốn
        layoutParams.topMargin = y + width / 5 // y là tọa độ y bạn muốn

        nothing?.visibility = View.INVISIBLE
        loading?.visibility = View.VISIBLE
        content?.visibility = View.INVISIBLE
        loading?.startShimmer()
        binding.rlLayout.addView(viewPopup, layoutParams)

        audio.setOnClickListener {
            playAudioFromUrl(audioPhontic)
        }
        heart.setOnClickListener {
            val bundle = Bundle();
            bundle.putParcelable("dictionaryItem", note)
            Navigation.findNavController(binding.root).navigate(R.id.addNote, bundle)
        }

        binding.rlLayout.setOnTouchListener { v, event ->
            val x = event.x.toInt()
            val y = event.y.toInt()

            val viewLocation = IntArray(2)
            viewPopup?.getLocationOnScreen(viewLocation)

            val viewLeft = viewLocation[0]
            val viewTop = viewLocation[1]
            val viewRight = viewLeft + viewPopup?.width!!
            val viewBottom = viewTop + viewPopup?.height!!

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (x < viewLeft || x > viewRight || y < viewTop || y > viewBottom) {
                        binding.rlLayout.removeView(viewPopup)
                        removeSpanForWord(startIndex, endIndex);
                        return@setOnTouchListener true
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (x < viewLeft || x > viewRight || y < viewTop || y > viewBottom) {
                        return@setOnTouchListener false
                    }
                }
            }
            false
        }

    }


    private fun setSpanForWord(start: Int, end: Int) {
        binding.txtPageContent.setOnClickListener {
            val startIndex = start
            val endIndex = end
            val contentInfo = binding.txtPageContent.text.toString();
            val notSpace = contentInfo.substring(startIndex, endIndex);
            val spannable = getSpannableText(contentInfo)
            if (notSpace != " ") {
                spannable.setSpan(
                    BackgroundColorSpan(resources.getColor(android.R.color.holo_green_light)),
                    startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.txtPageContent.text = spannable
            }
        }

    }

    private fun removeSpanForWord(start: Int, end: Int) {
        val spannableString = getSpannableText(binding.txtPageContent.text.toString())
        val spans = spannableString.getSpans(start, end, BackgroundColorSpan::class.java)

        spans?.forEach { span ->
            spannableString.removeSpan(span)
        }

        binding.txtPageContent.text = spannableString
    }

    private fun getSpannableText(text: String): SpannableStringBuilder {
        return SpannableStringBuilder(text)
    }


    companion object {
        @SuppressLint("SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        @TypeConverter
        @JvmStatic
        fun fromDateToString(date: Date?): String? {
            return date?.let { dateFormat.format(it) }
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToDate(dateString: String?): Date? {
            return dateString?.let { dateFormat.parse(it) }
        }

        fun formatDuration(duration: Long): String {
            val minutes = (duration / 1000 / 60).toInt()
            val seconds = (duration / 1000 % 60).toInt()
            return String.format("%d:%02d", minutes, seconds)
        }
    }

    fun getBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {
        activity.window?.let { window ->
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                PixelCopy.request(
                    window,
                    Rect(
                        locationOfViewInWindow[0],
                        locationOfViewInWindow[1],
                        locationOfViewInWindow[0] + view.width,
                        locationOfViewInWindow[1] + view.height
                    ),
                    bitmap,
                    { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            callback(bitmap)
                        }
                        // possible to handle other result codes ...
                    },
                    Handler(Looper.myLooper()!!)
                )
            } catch (e: IllegalArgumentException) {
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
        }
    }

    private fun shareBitmap(bitmap: Bitmap) {
        // Lưu bitmap vào bộ nhớ và lấy Uri
        val uri: Uri? = context?.let { saveBitmapToStorage(it, bitmap) }

        // Tạo Intent để chia sẻ ảnh
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

        // Khởi chạy Intent
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ ảnh qua"))
    }

    private fun saveBitmapToStorage(context: Context, bitmap: Bitmap): Uri? {
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")

            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName.toString() + ".provider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return uri
    }

    private fun shareUri(uriText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, uriText)
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ đường dẫn qua:"))
    }
}

public fun toArticleEntity(article: NewsArticle): ArticleEntity {
    val articleEntity = ArticleEntity("null")
    articleEntity.idArticle = article.idArticle.toString()
    articleEntity.linkArticle = article.linkArticle
    articleEntity.titleArticle = article.titleArticle
    articleEntity.content = article.content
    articleEntity.country = article.country
    articleEntity.creator = article.creator
    articleEntity.field = article.field
    articleEntity.imageUrl = article.imageUrl
    articleEntity.pubDate = fromDateToString(article.pubDate)
    articleEntity.sourceId = article.sourceId
    articleEntity.sourceUrl = article.sourceUrl
    return articleEntity
}

public fun toNewsArticle(articleEntity: ArticleEntity): NewsArticle {
    return NewsArticle(
        idArticle = articleEntity.idArticle,
        linkArticle = articleEntity.linkArticle,
        titleArticle = articleEntity.titleArticle,
        content = articleEntity.content,
        country = articleEntity.country,
        creator = articleEntity.creator,
        field = articleEntity.field,
        imageUrl = articleEntity.imageUrl,
        pubDate = fromStringToDate(articleEntity.pubDate),
        sourceId = articleEntity.sourceId,
        sourceUrl = articleEntity.sourceUrl
    )
}

fun playAudioFromUrl(audioUrl: String) {
    var mediaPlayer: MediaPlayer? = null
    if (mediaPlayer == null) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }
    try {
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}