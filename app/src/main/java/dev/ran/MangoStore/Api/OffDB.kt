package dev.ran.MangoStore.Api
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import dev.ran.MangoStore.Model.OffDBlist

class OffDB(context: Context): SQLiteOpenHelper(context,DBName,null,DBVersion) {
    companion object{
        private const val DBVersion = 1
        private const val DBName = "DBMangos"
        private const val MANGOtable = "DataMangos"

        private const val mid = "mId"
        private const val mtrackname = "mTrackname"
        private const val martistname = "mArtistname"
        private const val mgenre = "mGenre"
        private const val mprice = "mPrice"
        private const val mPic = "mPicture"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        //Create table
        val tbldataMango = ("CREATE TABLE $MANGOtable( mId INTEGER PRIMARY KEY AUTOINCREMENT, mTrackname TEXT, mArtistname TEXT, mGenre TEXT, mPrice TEXT, mPicture TEXT)")
        db?.execSQL(tbldataMango)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Drop table
        db!!.execSQL("DROP TABLE IF EXISTS $MANGOtable")
        onCreate(db)
    }

    //add Favorite to Table DataMango
    fun addMango(data: OffDBlist): Long{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(mtrackname, data.trackName)
        cv.put(martistname, data.artistName)
        cv.put(mgenre, data.primaryGenreName)
        cv.put(mprice, data.trackPrice)
        cv.put(mPic, data.urlPicture)
        cv.put(mPic, data.urlPicture)
        val success = db.insert(MANGOtable, null, cv)
        db.close()
        return success
    }

    //Get All data from table DataMango
    fun getDataMango(): List<OffDBlist>{
        val mangoList:ArrayList<OffDBlist> = ArrayList()
        val selectQuery = "SELECT  * FROM $MANGOtable"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var mId: Int
        var mtrackName: String
        var martistName: String
        var mgenre: String
        var mprice: String
        var mPicture: String

        if (cursor.moveToFirst()) {
            do {
                mId = cursor.getInt(cursor.getColumnIndex("mId"))
                mtrackName = cursor.getString(cursor.getColumnIndex("mTrackname"))
                martistName = cursor.getString(cursor.getColumnIndex("mArtistname"))
                mgenre = cursor.getString(cursor.getColumnIndex("mGenre"))
                mprice = cursor.getString(cursor.getColumnIndex("mPrice"))
                mPicture = cursor.getString(cursor.getColumnIndex("mPicture"))
                val mango= OffDBlist(mid = mId ,trackName = mtrackName, artistName = martistName, primaryGenreName = mgenre, trackPrice = mprice, urlPicture = mPicture)
                mangoList.add(mango)
            } while (cursor.moveToNext())
        }
        return mangoList
    }

    //Delete data from table DataMango
    fun deleteFavorie(num: Int): Int{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(mid, num)
        val success = db.delete(MANGOtable,"mId="+num,null)
        db.close()
        return success
    }
}