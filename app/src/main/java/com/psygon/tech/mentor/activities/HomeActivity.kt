package com.psygon.tech.mentor.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.psygon.tech.mentor.MainActivity
import com.psygon.tech.mentor.R
import com.psygon.tech.mentor.helpers.GlideApp
import com.psygon.tech.mentor.helpers.SUBJECT_PATH
import com.psygon.tech.mentor.helpers.log
import com.psygon.tech.mentor.helpers.toast
import com.psygon.tech.mentor.models.Subject
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var imageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)
        imageView = headerView.findViewById(R.id.imageView)
        tvName = headerView.findViewById(R.id.tvName)
        tvEmail = headerView.findViewById(R.id.tvEmail)
    }

    private fun createSubject(name: String): Subject {
        val subject = Subject()

        subject.subjectName = name
        subject.status = true

        val date = Calendar.getInstance().time
        subject.creationDate = date.toString()

        if (auth.currentUser != null) {
            subject.subjectCreatorID = auth.currentUser!!.uid
            subject.subjectCreator = auth.currentUser!!.displayName.toString()
        }

        val database = FirebaseDatabase.getInstance().getReference(SUBJECT_PATH)
        subject.subjectID = database.push().key.toString()
        database.child(subject.subjectID).setValue(subject)

        return subject
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            tvName.text = name
            tvEmail.text = email

            GlideApp.with(this)
                .load(photoUrl)
                .into(imageView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sign_out -> {
                auth.signOut()

                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
