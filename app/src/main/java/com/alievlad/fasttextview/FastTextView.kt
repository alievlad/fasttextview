package com.alievlad.fasttextview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.coroutines.*


/**
 * Created by alievlad on 4/13/2022.
 */

class FastTextView(
	context: Context,
	attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {

	private var deferred : Deferred<Unit>? = null

	override fun setText(text: CharSequence, type: BufferType?) {

		deferred?.cancel()

		deferred = CoroutineScope(Dispatchers.Main).async {

			val computedText = withContext(Dispatchers.IO) {
				val params = TextViewCompat.getTextMetricsParams(this@FastTextView)
				PrecomputedTextCompat.create(text, params)
			}

			TextViewCompat.setPrecomputedText(this@FastTextView, computedText)
		}
	}

	override fun onDetachedFromWindow() {
		deferred?.cancel()
		deferred = null

		super.onDetachedFromWindow()
	}
}
