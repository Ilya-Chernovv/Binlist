package com.example.android.binlist


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.binlist.R.id
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var mList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var textView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mList)
        textView = findViewById<View>(R.id.autoCompleteTextView) as AutoCompleteTextView
        textView.setAdapter(adapter)

    }

    fun submit(view: View) {
        var number: String = textView.text.toString();
        if (!mList.contains(number)) {
            mList.add(number)
            adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mList)
            textView.setAdapter(adapter)
        }
        getWebsite(number)
    }

    private fun setWebsite(text: String) {
        val length = findViewById<TextView>(id.length)
        length.text = "?"
        val scheme = findViewById<TextView>(id.scheme)
        scheme.text = "?"
        val brand = findViewById<TextView>(id.brand)
        brand.text = "?"
        val luhn = findViewById<TextView>(id.luhn)
        luhn.text = "?"
        val type = findViewById<TextView>(id.type)
        type.text = "?"

        val prepaid = findViewById<TextView>(id.prepaid)
        prepaid.text = "?"
        val countryFlag = findViewById<TextView>(id.countryFlag)
        countryFlag.text = "?"
        val countryName = findViewById<TextView>(id.countryName)
        countryName.text = ""
        val latitude = findViewById<TextView>(id.latitude)
        latitude.text = "?"
        val longitude = findViewById<TextView>(id.longitude)
        longitude.text = ""
        val nameBank = findViewById<TextView>(id.nameBank)
        nameBank.text = "?"
        val cityBank = findViewById<TextView>(id.cityBank)
        cityBank.text = ""
        val url = findViewById<TextView>(id.url)
        url.text = "?"
        val phone = findViewById<TextView>(id.phone)
        phone.text = "?"

        val lstValues: List<String> = text.split(",")

        var count: Int = 0;

        for (str in lstValues) {
            count++
            var textValue: String = str

            if (textValue.contains("length")) {
                textValue = textValue.replace("number:length:", "")
                length.text = textValue
            } else if (textValue.contains("scheme")) {
                textValue = textValue.replace("scheme:", "")
                textValue = textValue.capitalize()
                scheme.text = textValue
            } else if (textValue.contains("brand")) {
                textValue = textValue.replace("brand:", "")
                brand.text = textValue
            } else if (textValue.contains("luhn")) {
                textValue = textValue.replace("luhn:", "")
                if (textValue.equals("false")) {
                    luhn.text = "No"
                } else {
                    luhn.text = "Yes"
                }
            } else if (textValue.contains("type")) {
                textValue = textValue.replace("type:", "")
                textValue = textValue.capitalize()
                type.text = textValue
            } else if (textValue.contains("prepaid")) {
                textValue = textValue.replace("prepaid:", "")
                if (textValue.equals("false")) {
                    prepaid.text = "No"
                } else {
                    prepaid.text = "Yes"
                }
            } else if (textValue.contains("name")) {
                if (count <= 10) {
                    textValue = textValue.replace("name:", "")
                    countryName.text = textValue
                } else {
                    textValue = textValue.replace("bank:name:", "")
                    var newStr: String = "$textValue,"
                    nameBank.text = newStr
                }
            } else if (textValue.contains("url")) {
                textValue = textValue.replace("url:", "")
                url.text = textValue
            } else if (textValue.contains("phone")) {
                textValue = textValue.replace("phone:", "")
                phone.text = textValue
            } else if (textValue.contains("latitude")) {
                var nevStr: String = "($textValue,"
                latitude.text = nevStr
            } else if (textValue.contains("longitude")) {
                var nevStr: String = "$textValue)"
                longitude.text = nevStr
            } else if (textValue.contains("city")) {
                textValue = textValue.replace("city:", "")
                cityBank.text = textValue
            } else if (textValue.contains("emoji")) {
                textValue = textValue.replace("emoji:", "")
                countryFlag.text = textValue
            }
        }
    }

    private fun getWebsite(number: String) {

        var lineText: String = ""
        val url: String = "https://lookup.binlist.net/$number"

        Thread {
            val builder = StringBuilder()

            try {
                val doc: Document =
                    Jsoup.connect("$url")
                        .requestBody("JSON")
                        .ignoreContentType(true)
                        .post()

                val links: Elements = doc.select("body")

                for (link in links) {

                    builder.append(link.text())

                }
            } catch (e: IOException) {

            }
            runOnUiThread {
                lineText = builder.toString()
                lineText = lineText.replace("{", "")
                lineText = lineText.replace("}", "")
                lineText = lineText.replace("\"", "")

                setWebsite(lineText)
            }
        }.start()
    }

}