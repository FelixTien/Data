package com.felixtien.fam.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.util.UUID

data class User(
    val id: String = "",
    var uid: String? = null,
    var photoURL: String = "",
    var path: String = "",
    val email: String? = null,
    val phone: String? = null,
    var username: String? = null
)
data class Lease(
    val id: String = "",
    val oid: String = "",
    var tids: MutableList<String> = mutableListOf(),
    var requests: MutableList<String> = mutableListOf(),
    var photoURL: String = "",
    var path: String = "",
    var title: String = "",
    var type: String = "",
    var url: MutableState<String>? = null
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "oid" to oid,
            "tids" to tids,
            "requests" to requests,
            "photoURL" to photoURL,
            "path" to path,
            "type" to type,
            "title" to title
        )
    }
}
data class Item(
    var lease: Lease?,
    var isRemoved: MutableState<Boolean> = mutableStateOf(false)
): Serializable
enum class DatabaseException{
    UID_EXIST,
    NO_USER,
    EMPTY_USERNAME
}
@SuppressLint("StaticFieldLeak")
object HomeAPI {
    lateinit var launcher: ActivityResultLauncher<String>

    private val db = Firebase.firestore
    var page = mutableIntStateOf(0)

    lateinit var user: MutableState<User?>
    lateinit var dashboardController: NavHostController
    lateinit var managementController: NavHostController
    lateinit var profileController: NavHostController
    var problem = mutableStateOf(false)
    var mode = mutableIntStateOf(0)
    var leases = mutableStateListOf<Item>()
    fun currentUser(completion: (User?) -> Unit) {
        val id = Firebase.auth.currentUser?.uid ?: ""
        db
            .collection("User")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) completion(document.toObject<User>()) else completion(null)
            }
    }
    fun synchronize() {
        val temp = user.value ?: return
        val authEmail = Firebase.auth.currentUser?.email ?: return
        if (Firebase.auth.currentUser?.phoneNumber != null) return
        if (temp.email != authEmail){
            db
                .collection("User")
                .document(temp.id)
                .update("email", authEmail)
            currentUser { user.value = it }
        }
    }
    fun imagePicker() {
        launcher.launch("image/*")
    }
    fun updateProfile(username: String, uid: String, error: (DatabaseException?) -> Unit) {
        if (username.isEmpty()){
            error(DatabaseException.EMPTY_USERNAME)
            return
        }
        val user = user.value ?: run {
            error(DatabaseException.NO_USER)
            return
        }
        db
            .collection("User")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { query ->
                if (query.count() > 1){
                    error(DatabaseException.UID_EXIST)
                    return@addOnSuccessListener
                }
                val doc = if (query.documents.isEmpty()) null else query.documents.first().data
                if (query.count() == 1 && doc?.get("id") as String != user.id){
                    error(DatabaseException.UID_EXIST)
                    return@addOnSuccessListener
                }
                db
                    .collection("User")
                    .document(user.id)
                    .update(mapOf(
                        "username" to username,
                        "uid" to uid
                    ))
                error(null)
            }
    }
    // MARK: LANDLORD
    fun addLease(){
        val user = user.value ?: return
        val id =  UUID.randomUUID().toString()
        val lease = Lease(
            id = id,
            oid = user.id,
            tids = mutableListOf(),
            requests = mutableListOf(),
            photoURL = "",
            path = "Lease/$id/House.jpeg",
            type = "Due every 5th",
            title = "New Lease",
            url = mutableStateOf("")
        )
        Log.d("TAG", "${lease.url}")
        db
            .collection("Lease")
            .document(id)
            .set(lease.toMap())
            .addOnSuccessListener {
                this.leases.add(Item(lease = lease))
            }
    }
    fun deleteLease(id: String){
        db
            .collection("Lease")
            .document(id)
            .delete()
    }
    fun loadLease(){
        val user = user.value ?: return
        db
            .collection("Lease")
            .whereEqualTo("oid", user.id)
            .get()
            .addOnSuccessListener { query ->
                val temp = query.toObjects<Lease>()
                leases.clear()
                temp.map {
                    it.url = mutableStateOf(it.photoURL)
                    leases.add(Item(lease = it))
                }
            }
    }
    fun updateLease(index: Int, title: String, type: String, completion: () -> Unit){
        val id = leases[index].lease?.id ?: ""
        db
            .collection("Lease")
            .document(id)
            .update(mapOf(
                "title" to title,
                "type" to type
            ))
            .addOnSuccessListener {
                completion()
            }
    }
}
@Composable
fun updateProfilePicture(completion: (String) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { data ->
        data?.let { uri ->
            val cloud = Firebase.storage.reference
            val db = Firebase.firestore
            val user = HomeAPI.user.value ?: return@rememberLauncherForActivityResult
            val name = cloud.child(user.path)

            val inputStream = context.contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            val size = minOf(bitmap.width, bitmap.height)
            val x = (bitmap.width - size) / 2
            val y = (bitmap.height - size) / 2
            bitmap = Bitmap.createBitmap(bitmap, x, y, size, size)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val compressedData = byteArrayOutputStream.toByteArray()
            val type = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()
            name.putBytes(compressedData, type)
                .addOnSuccessListener {
                    cloud.child(user.path).downloadUrl.addOnSuccessListener {
                        completion(it.toString())
                        db
                            .collection("User")
                            .document(user.id)
                            .update("photoURL", it.toString())
                    }
                }
        }
    }
}
@Composable
fun updateLeasePicture(index: Int, completion: (String) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { data ->
        data?.let { uri ->
            val cloud = Firebase.storage.reference
            val db = Firebase.firestore
            val lease = HomeAPI.leases[index].lease ?: return@rememberLauncherForActivityResult
            val name = cloud.child(lease.path)

            val inputStream = context.contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            val size = minOf(bitmap.width, bitmap.height)
            val x = (bitmap.width - size) / 2
            val y = (bitmap.height - size) / 2
            bitmap = Bitmap.createBitmap(bitmap, x, y, size, size)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val compressedData = byteArrayOutputStream.toByteArray()
            val type = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()
            name.putBytes(compressedData, type)
                .addOnSuccessListener {
                    cloud.child(lease.path).downloadUrl.addOnSuccessListener {
                        completion(it.toString())
                        HomeAPI.leases[index].lease?.url?.value = it.toString()
                        db
                            .collection("Lease")
                            .document(lease.id)
                            .update("photoURL", it.toString())
                    }
                }
        }
    }
}