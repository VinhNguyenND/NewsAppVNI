package com.example.myappnews.Ui.Fragment.Article.downLoad

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.room.TypeConverter
import com.bumptech.glide.Glide
import com.example.myappnews.Data.Local.Article.Down.ArticleDownEntity
import com.example.myappnews.Data.Local.Article.Down.ArticleDownViewModel
import com.example.myappnews.R
import com.example.myappnews.Ui.Fragment.management.Author.Home.showToast
import com.example.myappnews.databinding.ArticleDownLoadedBinding
import com.example.myappnews.databinding.DownloadedfragmentBinding
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class ArticleDown : Fragment() {
    private lateinit var binding: ArticleDownLoadedBinding
    private lateinit var articleDownViewModel: ArticleDownViewModel
    private var speech = "";
    private var mp: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var runable: Runnable;
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var article: ArticleDownEntity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArticleDownLoadedBinding.inflate(inflater, container, false);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setEvent(view)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initContent() {
        if (arguments?.getParcelable("Article", ArticleDownEntity::class.java) != null) {
            article = arguments?.getParcelable("Article", ArticleDownEntity::class.java)!!
            binding.txtPageContent.text = article.content
            binding.txtPageTime.text = article.pubDate.toString()
            Glide.with(requireContext())
                .load(article.imageUrl?.trim())
                .error(R.drawable.uploaderror)
                .fitCenter()
                .into(binding.imgArticlePage)
            speech = article.sourceVoice.toString();
            try {
                val file = File(speech)
                if (file.exists()) {
                    mp?.setDataSource(speech)
                    mp?.prepare()
                    binding.seekPageContent.max = mp!!.duration
                } else {
                    Log.e("MediaPlayer", "File not found: $speech")
                }
            } catch (e: Exception) {
                Log.e("MediaPlayer", "Error setting data source", e)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        initializeMediaPlayer()
        initContent()
        setEventBottomMedia()
        runable = Runnable {
            binding.seekPageContent.progress = mp?.currentPosition ?: 0
            updateSeekBar()
        }
        super.onResume()
    }

    override fun onDestroyView() {
        stopAudio()
        super.onDestroyView()
    }

    override fun onPause() {
        stopAudio()
        super.onPause()
    }


    private fun initializeMediaPlayer() {
        mp = MediaPlayer();
        mp!!.setOnPreparedListener {
            binding.seekPageContent.max = mp!!.duration
        }
    }

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

    private fun setEvent(view: View) {
        binding.btnbackar.setOnClickListener {
            view.findNavController().popBackStack();
        }
        binding.decree5s.setOnClickListener {
            seekBackward(5000)
        }
        binding.btnGo5s.setOnClickListener {
            seekForward(5000)
        }
        binding.deleteDown.setOnClickListener {
            showDeleteDialog(view);
        }
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

    private fun stopAudio() {
        binding.StartOrStop.setImageResource(android.R.drawable.ic_media_play)
        isPlaying = false
        mp?.stop()
        handler.removeCallbacks(runable);
    }

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

    fun deleteFile(filePath: String, view: View) {
        val file = File(filePath)
        val isDelete: Boolean = file.delete();
        if (isDelete) {
            showToast(requireContext(), "Xóa thành công");
            Navigation.findNavController(view).popBackStack();
        } else {
            showToast(requireContext(), "Xóa thất bại");
        }
    }

    private fun showDeleteDialog(view: View) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_approve_pop)
        val window: Window = dialog.window ?: return;
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<TextView>(R.id.textView).text = "Bạn có muốn xóa bài báo";
        val windowAtribute: WindowManager.LayoutParams = window.attributes
        windowAtribute.gravity = Gravity.CENTER
        window.attributes = windowAtribute

        dialog.findViewById<Button>(R.id.btnDongy).setOnClickListener {
            articleDownViewModel.deleteById(article).observe(viewLifecycleOwner, Observer {
                if (it) {
                    article.sourceVoice?.let { it1 -> deleteFile(it1, view) }
                }
            })
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btnLoaiBo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initViewModel() {
        articleDownViewModel = ViewModelProvider(this).get(ArticleDownViewModel::class.java)
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
}