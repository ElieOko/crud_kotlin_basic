package com.example.crudkt.Controleur.Adaptater

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.crudkt.Model.Student
import com.example.crudkt.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StudentAdaptater(val onClick:(Student)->Unit) : RecyclerView.Adapter<StudentAdaptater.ViewHolder>() {

    private var stdList: ArrayList<Student> = ArrayList()
    private var onClickItem: ((Student) -> Unit)? = null
    private var onClickDeleteItem: ((Student) -> Unit)? = null


    fun addItem(items: ArrayList<Student>){
        this.stdList = items
        notifyDataSetChanged()
    }
    fun  setOnClickItem(callback: (Student)->Unit){
        this.onClickItem = callback
        Log.e("","***********************")
        Log.e("","$callback")
    }
    fun  setOnClickDeleteitem(callback: (Student)->Unit){
        this.onClickDeleteItem = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row, parent,
                false
            )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = stdList[position]
        holder.bindView(item)
        //holder.itemView.setOnClickListener{this.onClickItem?.invoke(item)}
        holder.itemView.setOnClickListener{this.onClick?.invoke(item)}

    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return stdList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        private var nom = view.findViewById<TextView>(R.id.rvNom)
        private var  prenom = view.findViewById<TextView>(R.id.rvPrenom)
        private var  promo = view.findViewById<TextView>(R.id.rvPromo)
        private var  age = view.findViewById<TextView>(R.id.rvAge)
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(item: Student) {
            nom.text = item.nom.toString()
            prenom.text = item.prenom.toString()
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            var date = LocalDate.parse(item.date.toString(), formatter)
            val ageStudent = LocalDate.now().year - date.year
            promo.text = item.promo.toString()
            age.text = "$ageStudent ans"
        }
    }





}