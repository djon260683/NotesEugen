package ru.eugen.noteseugen.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import ru.eugen.noteseugen.MainActivity
import ru.eugen.noteseugen.Navigation
import ru.eugen.noteseugen.R


class StartFragment : Fragment() {

    // Используется, чтобы определить результат activity регистрации через Google
    val RC_SIGN_IN: Int  = 40404
    val TAG: String  = "GoogleAuth"
    lateinit var navigation: Navigation
    // Клиент для регистрации пользователя через Google
    lateinit var googleSignInClient: GoogleSignInClient
    // Кнопка регистрации через Google
    lateinit var buttonSignIn: com.google.android.gms.common.SignInButton
    lateinit var buttonSingOut: Button
    lateinit var emailView: TextView
    lateinit var continues: Button

    companion object {
        fun newInstance(): StartFragment {
            val fragment: StartFragment = StartFragment()
            return fragment
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context);
        // Получим навигацию по приложению, чтобы перейти на фрагмент со списком карточек
        val activity:MainActivity  = context as MainActivity
        navigation = activity.navigation
    }

    override fun onDetach():Unit {
       // navigation = null
        super.onDetach()
    }

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View  = inflater.inflate(R.layout.fragment_start, container, false)
        initGoogleSign()
        initView(view)
        enableSign()
        return view
    }

    // Инициализация запроса на аутентификацию
    fun initGoogleSign():Unit {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл
        // (регулируется параметром)
        val gso: GoogleSignInOptions  =  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(getContext()!!, gso)
    }

    // Инициализация пользовательских элементов
    fun initView(view: View):Unit {
        // Кнопка регистрации пользователя
        buttonSignIn = view.findViewById(R.id.sign_in_button)
        buttonSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                signIn()
            }
        })

        emailView = view.findViewById(R.id.email)
        // Кнопка «Продолжить», будем показывать главный фрагмент
        continues = view.findViewById(R.id.continues)
        continues.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               navigation.replaceFragment(FragmentNotesList.newInstance(),false)
            }
        })
        // Кнопка выхода
        buttonSingOut = view.findViewById(R.id.sing_out_button)
        buttonSingOut.setOnClickListener(object : View.OnClickListener {
            override fun  onClick(v: View ) {
                signOut()
            }
        });
    }

    override fun onStart() {
        super.onStart();
        // Проверим, входил ли пользователь в это приложение через Google
        val account: GoogleSignInAccount  = GoogleSignIn.getLastSignedInAccount(getContext())!!
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            disableSign()
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            updateUI(account.email.toString())
        }
    }

    // Выход из учётной записи в приложении
    fun signOut():Unit {
        googleSignInClient.signOut()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        updateUI("")
                        enableSign()
                    }
                })
    }

    // Инициируем регистрацию пользователя
    private fun signIn() {
        val signInIntent: Intent  = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Здесь получим ответ от системы, что пользователь вошёл
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже готовы
            val task: Task<GoogleSignInAccount>  = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получаем данные пользователя
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.d("Log", "handleSignInResult")

            val account = completedTask.getResult(ApiException::class.java)
            // Регистрация прошла успешно
            disableSign()
            updateUI(account?.email.toString())
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
            // reference for more information.
            Log.w("Log", "signInResult:failed code=" + e.getStatusCode())
        }
    }

    // Обновляем данные о пользователе на экране
    private fun updateUI(email: String ):Unit {
        emailView.setText(email)
    }

    // Разрешить аутентификацию и запретить остальные действия
    private fun enableSign():Unit{
        buttonSignIn.setEnabled(true)
        continues.setEnabled(false)
        buttonSingOut.setEnabled(false)
    }

    // Запретить аутентификацию (уже прошла) и разрешить остальные действия
    private fun disableSign():Unit{
        buttonSignIn.setEnabled(false)
        continues.setEnabled(true)
        buttonSingOut.setEnabled(true)
    }
}

