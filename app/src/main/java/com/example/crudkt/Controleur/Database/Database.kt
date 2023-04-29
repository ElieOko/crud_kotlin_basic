package com.example.crudkt.Controleur.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.crudkt.Model.Student

class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "StudentBD"
        private const val TABLE_STUDENT = "TableStudent"
        private const val ID = "_id"
        private const val NOM = "name"
        private const val PRENOM = "prenom"
        private const val PROMO = "promotion"
        private const val DATE_NAIS = "date_nais"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_STUDENT_TABLE = ("CREATE TABLE " + TABLE_STUDENT + "("
                + ID + " INTEGER PRIMARY KEY," + NOM + " TEXT,"
                + PRENOM + " TEXT," +  PROMO + " TEXT," +  DATE_NAIS + " TEXT" + ")")
        db?.execSQL(CREATE_STUDENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    //Insertion
    fun create(emp: Student): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOM, emp.nom)
        contentValues.put(PRENOM, emp.prenom)
        contentValues.put(PROMO, emp.promo)
        contentValues.put(DATE_NAIS, emp.date)
        val success = db.insert(TABLE_STUDENT, null, contentValues)

        return success
    }

    //Update
    fun update(emp: Student): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOM, emp.nom)
        contentValues.put(PRENOM, emp.prenom)
        contentValues.put(PROMO, emp.promo)
        contentValues.put(DATE_NAIS, emp.date)
        val success = db.update(TABLE_STUDENT, contentValues, ID + "=" + emp.id, null)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllStudent(): ArrayList<Student> {
        val empList: ArrayList<Student> = ArrayList<Student>()
        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_STUDENT"
        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var nom: String
        var prenom: String
        var promo: String
        var date_nais: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID))
                nom = cursor.getString(cursor.getColumnIndex(NOM))
                prenom = cursor.getString(cursor.getColumnIndex(PRENOM))
                promo = cursor.getString(cursor.getColumnIndex(PROMO))
                date_nais = cursor.getString(cursor.getColumnIndex(DATE_NAIS))
                val emp = Student(id = id, nom = nom, prenom = prenom,promo = promo, date = date_nais)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    fun delete(id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        val success = db.delete(TABLE_STUDENT, "_id=$id", null)
        db.close()
        return success
    }
}
