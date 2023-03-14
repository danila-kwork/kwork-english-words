package ru.game.english.words.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.game.english.words.data.user.model.User

class AuthDataStore {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database

    fun signIn(email:String,password: String, onSuccess:() -> Unit, onError:(message:String) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                when(it){
                    is FirebaseAuthInvalidCredentialsException -> {
                        when(it.message){
                            "The email address is badly formatted." ->
                                onError("Адрес электронной почты плохо отформатирован.")
                            "The password is invalid or the user does not have a password." ->
                                onError("Пароль неверен.")
                            else -> {
                                onError("Ошибка")
                            }
                        }
                    }
                    is FirebaseAuthInvalidUserException -> {
                        onError("Нет записи пользователя, соответствующей этому идентификатору. Возможно, пользователь был удален.")
                    }
                    else -> {
                        onError("Ошибка")
                    }
                }
            }
    }

    fun registration(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createUser(
                        id = auth.currentUser!!.uid,
                        email = email,
                        password = password
                    ){
                        if(it.isSuccessful){
                            onSuccess()
                        }else{
                            onError(it.exception?.message ?: "Ошибка авторизации")
                        }
                    }
                }else{
                    onError(task.exception?.message ?: "Ошибка авторизации")
                }
            }
            .addOnFailureListener {
                when(it){
                    is FirebaseAuthInvalidCredentialsException -> {
                        when(it.message){
                            "The email address is badly formatted." ->
                                onError("Адрес электронной почты плохо отформатирован.")
                            "The password is invalid or the user does not have a password." ->
                                onError("Пароль неверен.")
                            "The given password is invalid. [ Password should be at least 6 characters ]" ->
                                onError("Пароль должен состоять не менее чем из 6 символов")
                            else -> {
                                onError("Ошибка авторизации")
                            }
                        }
                    }
                    is FirebaseAuthInvalidUserException -> {
                        onError("Нет записи пользователя, соответствующей этому идентификатору. Возможно, пользователь был удален.")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        onError("Адрес электронной почты уже используется другой учетной записью")
                    }
                    else -> {
                        onError("Ошибка авторизации")
                    }
                }
            }
    }

    private fun createUser(
        id: String,
        email: String,
        password: String,
        onCompleteListener: (Task<Void>) -> Unit = {},
    ){
        val user = User(id = id,email = email, password = password)

        database.reference.child("users").child(id).updateChildren(user.dataMap())
            .addOnCompleteListener {
                onCompleteListener(it)
            }
    }
}