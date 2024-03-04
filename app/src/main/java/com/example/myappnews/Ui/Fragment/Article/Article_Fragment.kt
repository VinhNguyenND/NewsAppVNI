package com.example.myappnews.Ui.Fragment.Article

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myappnews.Data.Api.Dictionary.DicRepository
import com.example.myappnews.Data.Api.Dictionary.DicViewModel
import com.example.myappnews.Data.Api.TextToSpeech.Repository
import com.example.myappnews.Data.Model.Dictionary.DictionaryItem
import com.example.myappnews.R
import com.example.myappnews.databinding.ArticleScreenBinding
import com.example.myappnews.databinding.PopupArticleBinding

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class Article_Fragment : Fragment() {
    private lateinit var binding: ArticleScreenBinding;
    private lateinit var viewModel: DicViewModel
    private lateinit var popupWindow: PopupWindow
    private var viewPopup: View? = null
    private var loadingButton:View?=null;
    private var bodyPopup:View?=null;
    private var word:TextView?=null;
    private var phonetic:TextView?=null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ArticleScreenBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeDic()
        setEventTouchText(binding.txtPageContent)
    }


    private fun init() {
        viewModel = ViewModelProvider(this)[DicViewModel::class.java]
    }

    private fun observeDic() {
        viewModel.word.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                     Log.i("tu dien nhan ve", "null")
                }else{
                    Log.i("tu dien nhan ve", it[0].toString())
                    word!!.text=it[0].word.toString()

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

    private fun callTextToSpeech() {
        val repository: Repository = Repository()
        viewLifecycleOwner.lifecycleScope.launch {
            repository.callTextToSpeech(
                binding.txtPageContent.text.toString(),
                "76373408e2mshdd1b501acbcbf46p1b09c4jsn6300c60e03f5",
                "text-to-speech27.p.rapidapi.com"
            )
        }
    }

    private fun setPopupWindow() {
        val inflater: LayoutInflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val popupView = inflater.inflate(R.layout.popup_article, null)
        val focusable = false
        popupWindow = PopupWindow(popupView, 1250, 1250, focusable)
        popupWindow.overlapAnchor = true
        popupWindow.elevation = 0.0f
        popupWindow.isClippingEnabled = false
        popupWindow.isOutsideTouchable = true
    }

    private fun showPopup(view: View, x: Int, y: Int) {
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y)
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

//    private fun handleTouch(event: MotionEvent?, textView: TextView) {
//        if (event != null) {
//            if (event.action == MotionEvent.ACTION_UP) {
//                val x = event.x.toInt()
//                val y = event.y.toInt()
//                val layout = textView.layout
//                val line = layout?.getLineForVertical(y)
//                val offset = line?.let { layout.getOffsetForHorizontal(it, x.toFloat()) }
//                val xOnScreen = offset?.let { layout.getPrimaryHorizontal(it).toInt() }
//                val yOnScreen = line?.let { layout.getLineBottom(it).toInt() }
//                if (offset != null) {
//                    val (left, right) = findWordAtPosition(
//                        textView.text.toString(),
//                        offset
//                    )
//                    if (xOnScreen != null && yOnScreen != null) {
//                        showPopup(binding.root, xOnScreen, yOnScreen)
//                    }
//                    Log.d("get get article when click:", textView.text.substring(left, right));
//                }
//            }
//        }
//    }

//    private fun handleTouch(event: MotionEvent?, textView: TextView) {
//        val density = resources.displayMetrics.density
//
//        // Lấy offset tương ứng với vị trí chạm của sự kiện
//        val offset = getOffsetForEvent(textView, event!!)
//
//        Log.d("getOffsetForEvent",offset.toString())
//
//        // Lấy layout của TextView
//        val layout = textView.layout
//
//        // Lấy dòng tương ứng với offset
//        val line = layout?.getLineForOffset(offset)
//
//        Log.d("getLineForOffset",offset.toString())
//
//
//        // Tính khoảng cách từ top của từ đến top của TextView
//        val distanceTop = layout!!.getLineTop(line!!).toFloat() / density
//
//        // Tính khoảng cách từ left của từ đến left của TextView
//        val distanceLeft = layout.getPrimaryHorizontal(offset).toFloat() / density
//
//        Log.d("DistanceTop", distanceTop.toInt().toString())
//        Log.d("DistanceLeft", distanceLeft.toInt().toString())
////        addView(distanceTop.toInt(),distanceLeft.toInt())
//    }

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
                    Log.d("get get article when click:", textView.text.substring(left, right));
                    val margin_top = pxToDp(distanceToTop as Float).toInt() - getStatusBarHeight()
                    println("Distance to top: " + margin_top.toString())
                    if (textView.text.substring(left, right) != " ") {
                        callGetWord(textView.text.substring(left, right))
                        addView(0, distanceToTop.toInt())
                        startCountdown()
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

    private fun addView(x: Int, y: Int) {

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        val width = metrics.widthPixels
        val height = metrics.heightPixels

        binding.rlLayout.removeView(viewPopup)
        viewPopup = LayoutInflater.from(context).inflate(R.layout.popup_article, null)
        loadingButton= viewPopup!!.findViewById<ProgressBar>(R.id.popupLoading);
        bodyPopup=viewPopup!!.findViewById(R.id.linearBody);
        word=viewPopup!!.findViewById(R.id.englishWord);
        phonetic=viewPopup!!.findViewById(R.id.phonetic1);

        val layoutParams = RelativeLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
        )
        layoutParams.leftMargin = pxToDp(x.toFloat()).toInt() + width / 15 // x là tọa độ x bạn muốn
        layoutParams.topMargin = y + width / 5 // y là tọa độ y bạn muốn

        binding.rlLayout.addView(viewPopup, layoutParams)

    }

    private fun lazyLoadingData() {


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

    private fun getSpannableText(text: String): SpannableStringBuilder {
        return SpannableStringBuilder(text)
    }

    private fun startCountdown() {
        val countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Mỗi lần đếm (mỗi giây), bạn có thể thực hiện các hành động cần thiết ở đây
            }

            override fun onFinish() {
                loadingButton!!.visibility=View.GONE
            }
        }

        // Bắt đầu bộ đếm
        countDownTimer.start()
    }


}