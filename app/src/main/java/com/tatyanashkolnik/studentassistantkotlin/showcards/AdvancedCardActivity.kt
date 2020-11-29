package com.tatyanashkolnik.studentassistantkotlin.showcards

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import kotlinx.android.synthetic.main.card_task_image.view.*

class AdvancedCardActivity : AppCompatActivity() {

    private lateinit var photoView: PhotoView
    private lateinit var imageView: ImageView
    private lateinit var tvCardDescription: TextView
    private lateinit var collapsingToolbarLayoutTitle: CollapsingToolbarLayout
    private lateinit var fabPriority: FloatingActionButton
    private lateinit var fabEdit: FloatingActionButton
    private lateinit var fabDone: FloatingActionButton

    private lateinit var card: NoteCard

    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        card = receiveData()
        initViews()
        inflateViews(card)

        // photoView.setImageResource(R.drawable.image)
    }

    private fun receiveData(): NoteCard =
        intent.getSerializableExtra("object") as NoteCard

    private fun initViews() {
        setContentView(R.layout.activity_advanced_card)
        photoView = findViewById(R.id.photo_view)
        imageView = findViewById(R.id.image_view)
        tvCardDescription = findViewById(R.id.text_card_description)
        fabPriority = findViewById(R.id.fab_priority)
        fabEdit = findViewById(R.id.fab_edit)
        fabDone = findViewById(R.id.fab_done)
        collapsingToolbarLayoutTitle = findViewById(R.id.toolbar_layout)
    }

    private fun inflateViews(card: NoteCard) {
        fabPriority.setImageResource(returnDrawablePriorityFromString(card.priority))
        collapsingToolbarLayoutTitle.title = card.time
        tvCardDescription.text = card.subtitle
        when (card.photoAttached) {
            "1" -> {
                Picasso.get()
                    .load(card.photo)
                    .into(photoView)
            }
        }
    }

    private fun returnDrawablePriorityFromString(color: String): Int =
        when (color) {
            "green" -> R.drawable.ic_priority_green_gray
            "yellow" -> R.drawable.ic_priority_yellow_gray
            "red" -> R.drawable.ic_priority_red_gray
            else -> R.drawable.ic_priority_default
        }

//    inner class LoadImage() : AsyncTask<String, String, Bitmap>(){
//        override fun onPostExecute(result: Bitmap?) {
//            super.onPostExecute(result)
//            imageView.setImageBitmap(result)
//        }
//
//        override fun doInBackground(vararg params: String?): Bitmap {
//            return BitmapFactory.decodeStream(URL(card.photo).content as InputStream)
//        }
//
//    }
}
