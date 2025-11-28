package com.voxcom.nextpill

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class AddActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    lateinit var img : ImageButton
    lateinit var thumb : ImageView
    private lateinit var viewModel: MedicineViewModel
    val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            uri?.let {
                thumb.setImageURI(it)
                runTextRecognition(it) }
        }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                pickImageLauncher.launch("image/*")
            } else {
                resultText.text = "Permission denied"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        img = findViewById(R.id.btnImg)
        resultText = findViewById(R.id.result_text)
        thumb = findViewById(R.id.thumbnail)

        img.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
        viewModel = MedicineViewModel(application)
    }

    private fun runTextRecognition(uri: Uri) {
        val image = InputImage.fromFilePath(this, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val extractedList = extractMedicineData(visionText.text)

                extractedList.forEach {
                    viewModel.addMedicine(it)
                }

                resultText.text = "Saved ${extractedList.size} medicines 👍"
            }

            .addOnFailureListener { e ->
                resultText.text = "Error: ${e.message}"
                Log.e("OCR", "Error: ", e)
            }
    }
    fun extractMedicineData(text: String): List<MedicineInfo> {
        val medicines = mutableListOf<MedicineInfo>()

        val chunks = text.split(Regex("""\n\s*\d+\)\s*"""))

        for (chunk in chunks) {
            if (chunk.length < 5) continue

            var medName = ""
            var dosage = ""
            var frequency = ""

            val medRegex = Regex("(?i)(tab|cap|inj|syrup|syp|gel|cream|drops)[^\n]+")
            medRegex.find(chunk)?.let {
                medName = it.value.trim()
            }

            if (medName.isEmpty()) {
                val firstLine = chunk.lines().firstOrNull()?.trim() ?: ""
                if (firstLine.matches(Regex("[A-Z .0-9/-]+"))) {
                    medName = firstLine
                }
            }

            val dosageRegex = Regex("(?i)(morning|night|noon|evening|after food|before food|\\d-\\d-\\d|\\b\\d dose\\b)[^\n]*")
            dosageRegex.find(chunk)?.let {
                dosage = it.value.trim()
            }

            val durationRegex = Regex("(?i)(\\d+\\s*days|\\d+\\s*day|\\d+\\s*weeks|for\\s*\\d+\\s*days)")
            durationRegex.find(chunk)?.let {
                frequency = it.value.trim()
            }

            resultText.text = "${medName} ${dosage} ${frequency}"

        }

        return medicines
    }

}
