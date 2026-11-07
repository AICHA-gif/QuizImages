package com.example.quizimages

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.graphics.Color
import androidx.activity.ComponentActivity

// Structure d'une question
data class Question(
    val texte: String,
    val image: Int,
    val choix: Array<String>,
    val bonneReponse: Int,
    val description: String
)

class MainActivity : ComponentActivity() {

    // Tableau contenant les 5 questions
    private val questions = arrayOf(

        Question(
            "Quelle est cette forme ?",
            R.drawable.triangle,
            arrayOf("Carré", "Triangle", "Cercle"),
            1,
            "Un triangle bleu"
        ),

        Question(
            "Quelle est cette forme ?",
            R.drawable.carre,
            arrayOf("Carré", "Rectangle", "Étoile"),
            0,
            "Un carré vert"
        ),

        Question(
            "Quelle est cette forme ?",
            R.drawable.cercle,
            arrayOf("Triangle", "Rectangle", "Cercle"),
            2,
            "Un cercle orange"
        ),

        Question(
            "Quelle est cette forme ?",
            R.drawable.etoile,
            arrayOf("Rectangle", "Étoile", "Carré"),
            1,
            "Une étoile jaune"
        ),

        Question(
            "Quelle est cette forme ?",
            R.drawable.rectangle,
            arrayOf("Rectangle", "Cercle", "Triangle"),
            0,
            "Un rectangle violet"
        )
    )

    // Variables du quiz
    private var indiceQuestion = 0
    private var score = 0
    private var dejaRepondu = false

    // Composants graphiques
    private lateinit var txtScore: TextView
    private lateinit var txtProgression: TextView
    private lateinit var txtNumero: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var txtFeedback: TextView

    private lateinit var imgQuestion: ImageView
    private lateinit var progressQuiz: ProgressBar

    private lateinit var btnChoix1: Button
    private lateinit var btnChoix2: Button
    private lateinit var btnChoix3: Button

    private lateinit var btnSuivant: Button
    private lateinit var btnRejouer: Button

    private lateinit var layoutQuiz: View
    private lateinit var layoutResultat: View

    private lateinit var boutons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Récupération des composants
        txtScore = findViewById(R.id.txt_score)
        txtProgression = findViewById(R.id.txt_progression)
        txtNumero = findViewById(R.id.txt_numero)
        txtQuestion = findViewById(R.id.txt_question)
        txtFeedback = findViewById(R.id.txt_feedback)

        imgQuestion = findViewById(R.id.img_question)
        progressQuiz = findViewById(R.id.progress_quiz)

        btnChoix1 = findViewById(R.id.btn_choix1)
        btnChoix2 = findViewById(R.id.btn_choix2)
        btnChoix3 = findViewById(R.id.btn_choix3)

        btnSuivant = findViewById(R.id.btn_suivant)
        btnRejouer = findViewById(R.id.btn_rejouer)

        layoutQuiz = findViewById(R.id.layout_quiz)
        layoutResultat = findViewById(R.id.layout_resultat)

        boutons = listOf(
            btnChoix1,
            btnChoix2,
            btnChoix3
        )

        // Événements des trois boutons
        boutons.forEachIndexed { index, bouton ->
            bouton.setOnClickListener {
                verifierReponse(index)
            }
        }

        // Bouton question suivante / résultat
        btnSuivant.setOnClickListener {

            if (indiceQuestion < questions.size - 1) {

                indiceQuestion++
                afficherQuestion()

            } else {

                afficherResultat()
            }
        }

        // Bouton rejouer
        btnRejouer.setOnClickListener {
            recommencerQuiz()
        }

        // Afficher la première question
        afficherQuestion()
    }

    // Afficher une question
    private fun afficherQuestion() {

        val question = questions[indiceQuestion]

        dejaRepondu = false

        txtNumero.text = getString(
            R.string.question_numero,
            indiceQuestion + 1,
            questions.size
        )

        txtQuestion.text = question.texte

        imgQuestion.setImageResource(question.image)

        imgQuestion.contentDescription = question.description

        // Afficher les trois réponses
        boutons.forEachIndexed { index, bouton ->

            bouton.text = question.choix[index]
            bouton.isEnabled = true
        }

        // Cacher la correction
        txtFeedback.visibility = View.GONE

        // Cacher le bouton suivant
        btnSuivant.visibility = View.GONE

        // Mettre à jour le score et la progression
        actualiserBandeau(indiceQuestion)

    }

    // Vérifier la réponse
    private fun verifierReponse(indiceChoix: Int) {

        // Empêcher plusieurs réponses
        if (dejaRepondu) return

        dejaRepondu = true

        val question = questions[indiceQuestion]

        if (indiceChoix == question.bonneReponse) {

            // Bonne réponse
            score++

            txtFeedback.text = getString(
                R.string.bonne_reponse
            )

            txtFeedback.setTextColor(Color.rgb(0, 150, 0))

        } else {

            // Mauvaise réponse
            txtFeedback.text = getString(
                R.string.mauvaise_reponse,
                question.choix[question.bonneReponse]
            )

            txtFeedback.setTextColor(Color.RED)
        }

        // Désactiver les trois boutons
        boutons.forEach {
            it.isEnabled = false
        }

        // Afficher la correction
        txtFeedback.visibility = View.VISIBLE

        // Mettre à jour le score et la progression
        actualiserBandeau(indiceQuestion + 1)

        // Changer le texte du bouton suivant
        if (indiceQuestion == questions.size - 1) {

            btnSuivant.text = getString(
                R.string.voir_resultat
            )

        } else {

            btnSuivant.text = getString(
                R.string.question_suivante
            )
        }

        btnSuivant.visibility = View.VISIBLE
    }

    // Actualiser le score et la progression
    private fun actualiserBandeau(nombreReponses: Int) {

        txtScore.text = getString(
            R.string.score_format,
            score,
            questions.size
        )

        txtProgression.text = getString(
            R.string.progression_format,
            nombreReponses,
            questions.size
        )

        progressQuiz.progress = nombreReponses
    }

    // Afficher le résultat final
    private fun afficherResultat() {

        layoutQuiz.visibility = View.GONE

        layoutResultat.visibility = View.VISIBLE

        val txtResultat: TextView =
            findViewById(R.id.txt_resultat)

        txtResultat.text = getString(
            R.string.resultat_final,
            score,
            questions.size
        )
    }

    // Recommencer le quiz
    private fun recommencerQuiz() {

        indiceQuestion = 0
        score = 0
        dejaRepondu = false

        layoutResultat.visibility = View.GONE

        layoutQuiz.visibility = View.VISIBLE

        afficherQuestion()
    }
}