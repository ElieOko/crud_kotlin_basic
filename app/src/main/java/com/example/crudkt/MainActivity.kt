package com.example.crudkt

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudkt.Controleur.Adaptater.StudentAdaptater
import com.example.crudkt.Controleur.Database.Database
import com.example.crudkt.Controleur.SwipeToDeleteCallBack
import com.example.crudkt.Model.Student
import com.example.crudkt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var sqliteInstanceBD= Database(context = this)
    private var adapter :StudentAdaptater? =null
    private lateinit var  recyclerView: RecyclerView
    var key :Int = 0
    val view: View? = null
    var mois :String = ""
    var jour :String = ""
    private var msg :String = "Certains champs sont vides"

    /*
        Fonction principale
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        blockInitialisation()
    }


    /*
        Block d'initisation
     */
   private fun blockInitialisation(){
        InitRecycleView()
        calendarWithEdit()
        addStudentClick()
        clickRowItems()
        updateEventClick()
        readStudentClick()
        deleteEvent()
        optionSelect()
        menu()
    }

    /*
        Fonction menu
     */

    private fun menu(){

    binding.toolbar.icMenu.setOnClickListener{
        val popupMenu = PopupMenu(this, it)
        popupMenu.setOnMenuItemClickListener {
                item->
            when(item.itemId){
                R.id.nav_repartition-> {
                    //  var intent = Intent(this, MainActivity2::class.java)
                    //  startActivity(intent)
                    true
                }
                R.id.nav_doc->{
                    Toast.makeText(this,"Tableau de bord", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_about->{
                    Toast.makeText(this,"Plan comptable Ohada", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.menu_items)
        popupMenu.show()
    }
    }

    /*
        Initialisation du RecycleView
     */
    private fun InitRecycleView(){

            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = StudentAdaptater {
                Log.d("TAG", "2_onCreate: ${it.prenom}")
                binding.nom.setText(it.nom)
                binding.prenom.setText(it.prenom)
                binding.edDate.setText(it.date)
                key = it.id
            }
        recyclerView.adapter =adapter

    }
    /*
        Fonction liée aux zones de saisie et au toast
     */
    private fun checkValue(myField:String):String?{
        var myVal :String?= myField
        if(myField.isNullOrEmpty()){
            myVal = myField
        }
        return myVal
    }

    private fun toast(msgContent:String){
        Toast.makeText(this, msgContent, Toast.LENGTH_SHORT).show()
    }


    private fun nettoyageZoneSasie(){
        binding.nom.setText("")
        binding.prenom.setText("")
        binding.promo.setText("")
        binding.edDate.setText("")
    }
    private fun optionSelect(){
        val items = listOf("L1","L2","L3","M1","M2","D1","D2")
        val selectItems = ArrayAdapter(this,R.layout.item,items)
        binding.promo.setAdapter(selectItems)
    }
    /*
        Fonction calendar
     */
    @SuppressLint("SetTextI18n")
    private fun calendarWithEdit() {
        val c = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        binding.edDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                jour += "0$dayOfMonth"
                if(dayOfMonth > 9){
                    jour =dayOfMonth.toString()
                }
                if(month + 1 > 9){
                    binding.edDate.setText("$jour-${month + 1}-$year")
                }
                else{
                    binding.edDate.setText("$jour-0${month + 1}-$year")
                }

            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    /*
        Evenement Click des differentes actions au niveau de L'IU(Interface Utilisateur)
     */
    private fun clickRowItems(){
        adapter?.setOnClickItem {
            key = it.id
            binding.nom.setText(it.nom)
            binding.prenom.setText(it.prenom)
            binding.promo.setText(it.promo)
            binding.edDate.setText(it.date)
            toast("yes ${it.id}")
        }

    }
    private fun addStudentClick(){
        binding.btnAdd.setOnClickListener {
            saveFormEventAdd()
        }
    }

    private fun readStudentClick(){
        binding.btnView.setOnClickListener {
            readEvent()
        }
    }

    private fun updateEventClick(){
        binding.btnUpdate.setOnClickListener {
            updateEvent()
        }
    }

    /*
        Fonction modulaire des actions du crud
     */
    private fun saveFormEventAdd(){
        var sqliteInstanceBD2= Database(context = this)
        var nom :String?=checkValue(binding.nom.text.toString())
        var prenom :String?=checkValue(binding.prenom.text.toString())
        var promo :String?=checkValue(binding.promo.text.toString())
        var date :String?=checkValue( binding.edDate.text.toString())
        if(nom.isNullOrEmpty() || prenom.isNullOrEmpty() || promo.isNullOrEmpty() || date.isNullOrEmpty()){
            toast(msg)
            return
        }
        toast("Cool")
        val etudiant :Student= Student(nom= nom,prenom = prenom,promo = promo,date = date)
        toast(etudiant.nom.toString())
        val status = sqliteInstanceBD2.create(etudiant)
        toast(status.toString())
        if (status > -1){
            toast("Enregistrement réussi avec succès")
            nettoyageZoneSasie()
            blockInitialisation()
            return
        }
        toast("Pas d'enregistrement !!!")
    }
   private fun updateEvent (){
       if(key != 0){
           var nom :String?=checkValue(binding.nom.text.toString())
           var prenom :String?=checkValue(binding.prenom.text.toString())
           var promo :String?=checkValue(binding.promo.text.toString())
           var date :String?=checkValue( binding.edDate.text.toString())
           if(nom.isNullOrEmpty() || prenom.isNullOrEmpty() || promo.isNullOrEmpty() || date.isNullOrEmpty()){
               toast(msg)
               return
           }
           toast("Arrivederci")
           val etudiant :Student= Student(id=key, nom= nom,prenom = prenom,promo = promo,date = date)
           val status = sqliteInstanceBD.update(etudiant)
           if (status > -1){
               toast("Modification réussi avec succès")
               nettoyageZoneSasie()
               readEvent()
               InitRecycleView()
               return
           }
       }
       toast("Erreur au niveau de l'identifiant, aucun changement")
   }
    private fun readEvent(){
        var sqliteInstanceBD3= Database(context = this)
        val stdList = sqliteInstanceBD3.getAllStudent()
        toast("${stdList.size}")
        adapter?.addItem(stdList)
    }
    private fun deleteEvent(){
        val swipeToDelete = object : SwipeToDeleteCallBack(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //     val stdList = sqliteHelper.readAllSociety()

                var position = viewHolder.adapterPosition
                try{

                    var stdList = sqliteInstanceBD.getAllStudent()
                    sqliteInstanceBD.delete(stdList[position].id)
                    stdList.removeAt(position)
                    recyclerView.adapter?.notifyItemRemoved(position)
                    readEvent()
                    toast("Suppression réussi avec succès")
                }
                catch(e:Exception){
                    toast(e.toString())               }
            }

        }
        val  itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    override fun onSaveInstanceState(outState: Bundle) { // Here You have to save count value
        super.onSaveInstanceState(outState)
        Log.i("MyTag", "onSaveInstanceState")

        blockInitialisation()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Here You have to restore count value
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("MyTag", "onRestoreInstanceState")


    }

}