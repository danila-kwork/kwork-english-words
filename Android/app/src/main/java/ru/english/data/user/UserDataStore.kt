package ru.english.data.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ru.english.data.user.model.User

class UserDataStore {

    private val auth = Firebase.auth
    private val database = Firebase.database
    private val userId = auth.currentUser!!.uid

    fun get(
        onSuccess:(User) -> Unit
    ){
        database.reference.child("users")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess(snapshot.getValue<User>()!!)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun updateCountInterstitialAds(count: Int) {
        database.reference.child("users")
            .child(userId)
            .child("countInterstitialAds")
            .setValue(count)
    }

    fun updateCountInterstitialAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countInterstitialAdsClick")
            .setValue(count)
    }

    fun updateCountRewardedAds(count: Int) {
        database.reference.child("users")
            .child(userId)
            .child("countRewardedAds")
            .setValue(count)
    }

    fun updateCountRewardedAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countRewardedAdsClick")
            .setValue(count)
    }

    fun updateCountBannerAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countBannerAdsClick")
            .setValue(count)
    }

    fun updateCountBannerAds(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countBannerAds")
            .setValue(count)
    }
}