package com.example.konnash

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PhoneNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        setupFooter()
    }

    private fun setupFooter() {
        val tvFooter = findViewById<TextView>(R.id.tvFooter)
        val privacyPolicy = getString(R.string.privacy_policy)
        val termsOfUse = getString(R.string.terms_of_use)
        val fullText = getString(R.string.footer_text, privacyPolicy, termsOfUse)

        val spannableString = SpannableString(fullText)

        val privacyStart = fullText.indexOf(privacyPolicy)
        val termsStart = fullText.indexOf(termsOfUse)

        val blueColor = ContextCompat.getColor(this, R.color.blue_primary)

        if (privacyStart != -1) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@PhoneNumberActivity, privacyPolicy, Toast.LENGTH_SHORT).show()
                }
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                    ds.color = blueColor
                }
            }, privacyStart, privacyStart + privacyPolicy.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (termsStart != -1) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(this@PhoneNumberActivity, termsOfUse, Toast.LENGTH_SHORT).show()
                }
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                    ds.color = blueColor
                }
            }, termsStart, termsStart + termsOfUse.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        tvFooter.text = spannableString
        tvFooter.movementMethod = LinkMovementMethod.getInstance()
        tvFooter.highlightColor = Color.TRANSPARENT
    }
}