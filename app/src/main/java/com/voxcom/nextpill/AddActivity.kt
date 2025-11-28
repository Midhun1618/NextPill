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
    lateinit var img: ImageButton
    lateinit var thumb: ImageView
    private lateinit var viewModel: MedicineViewModel
    val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            uri?.let {
                thumb.setImageURI(it)
                runTextRecognition(it)
            }
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

        // Regex pattern: Name + dosage
        val regex = Regex("""([A-Za-z][A-Za-z ]+)\s*(\d+\s*(mg|ml|iu|IU|MG|ML))""")

        val lines = text.lines()

        for (line in lines) {
            val match = regex.find(line)

            if (match != null) {
                val name = match.groupValues[1].trim()
                val dose = match.groupValues[2].trim()

                // detect schedule
                val schedule = when {
                    line.contains("morning", true) -> "Morning"
                    line.contains("evening", true) -> "Evening"
                    line.contains("night", true) -> "Night"
                    line.contains("2 times", true) -> "Twice Daily"
                    line.contains("3 times", true) -> "Thrice Daily"
                    else -> "Unspecified"
                }

                medicines.add(
                    MedicineInfo(
                        name = name,
                        dosage = dose,
                        frequency = schedule
                    )
                )
            }
        }

        return medicines
    }
}
