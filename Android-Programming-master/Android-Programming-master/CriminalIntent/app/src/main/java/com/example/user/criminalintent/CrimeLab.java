package com.example.user.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.criminalintent.database.CrimeBaseHelper;
import com.example.user.criminalintent.database.CrimeCursorWrapper;
import com.example.user.criminalintent.database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.user.criminalintent.database.CrimeDbSchema.*;

/**
 * Created by user on 2019/9/8.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    // private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        //  mCrimes = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }

    /*
    向数据库写入数据
     */
    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
        // mCrimes.add(c);
    }


    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
        //return new ArrayList<>();
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }


    public Crime getCrime(UUID id) {
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(id))
//                return crime;
//        }
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + "=?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime)
    {
        File filesDir=mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }


    /**
     * 更新数据库记录
     *
     * @param crime
     */
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + "=?", new String[]{uuidString});
    }

    /**
     * 这是query 语言，见p236
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    // private Cursor queryCrimes(String whereClause, String[] whereArgs) {
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null,//colums -null selects all columns
                whereClause,
                whereArgs,
                null,//string groupby
                null,//string having
                null);//string orderby
        return new CrimeCursorWrapper(cursor);
        // return cursor;
    }

//    public void deleteCrime(CrimeDec crime)
//    {
//        mDatabase.delete(CrimeTable.TABLE_NAME, )
//    }
//public void deleteItem(UUID crimeID) {
//    Crime crime = getCrime(crimeID);
//    mCrimes.remove(crime);
//}



}

