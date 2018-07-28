package com.m_Sallam.mahmoudmostafa.myapplication.store;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.R.attr.id;

/**
 * Created by Mahmoud Mostafa on 10/2/2017.
 */

public class MoviesProvider extends ContentProvider
{

    private static final String PROVIDER_NAME=
            "example.mahmoudmostafa.myapplication.store.movies";
    public static final Uri CONTENT_URL=
        Uri.parse("content://"+PROVIDER_NAME+"/movies");

    private static final int MOVIES=1;
    private static final int MOVIE_ID=2;

    private static final UriMatcher uriMatcher=getUriMatcher();

    private MoviesDatabase moviesDatabase = null ;


    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIES);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIE_ID);
        return uriMatcher;
    }

    //methods of content provider
    @Override
    public boolean onCreate() {
        moviesDatabase=new MoviesDatabase(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String id = null ;
            if (uriMatcher.match(uri)==MOVIE_ID)
            {
                //query for one single movie
                id=uri.getPathSegments().get(1);

            }

        return moviesDatabase.getMovies(id,projection,selection,selectionArgs,sortOrder);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri))
        {
            case MOVIES:
                return "vnd.android.cursor.dir/vnd" +
                        ".com.example.mahmoudmostafa.myapplication.store.myapplication.store.movies";
            case MOVIE_ID:
                return "vnd.android.cursor.item/vnd" +
                        ".com.example.mahmoudmostafa.myapplication.store.myapplication.store.movies";

        }
        return  "" ;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try
        {
            moviesDatabase.addMovie(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URL,id);
            return  returnUri;
        }
        catch (Exception e)
        {
            return null ;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        String id =null ;
        if (uriMatcher.match(uri)==MOVIE_ID)
        {

            //deleting for one single movie
            id=uri.getPathSegments().get(0);

        }
        return moviesDatabase.delteMovie(id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    /*-----------------------------------------------------------------------------*/
    public class MoviesDatabase extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME="MoviesDatabase.db";
        private static final String TABLE_NAME="MovieData";
        private static final String SQL_CREATE=

                "CREATE TABLE "+TABLE_NAME+"(_id text primary key , title text , releaseDate text , " +
                        "review text  , posterPath text , voteAverage text )";

        private static final String SQL_DROP=
                "DROP TABLE IF EXISTS " +TABLE_NAME ;

        public MoviesDatabase(Context context) {
            super(context,DATABASE_NAME,null,3);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(SQL_DROP);
            onCreate(db);
        }

        public Cursor getMovies (String id, String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder)
        {
            SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();
            sqLiteQueryBuilder.setTables(TABLE_NAME);
         /*   if(id !=null)
            {
                sqLiteQueryBuilder.appendWhere("_id" + "=" + id);

            }
            if(sortOrder==null || sortOrder=="")
            {
                sortOrder="title";

            }*/
            Cursor cursor =sqLiteQueryBuilder.query(getReadableDatabase(),
                    projection,selection,selectionArgs,null,null,sortOrder);
            return cursor;
        }


        public long addMovie (ContentValues values )throws SQLException
        {
            long id = getWritableDatabase().insert(TABLE_NAME,null,values );
            if(id<0)
            {
                throw new SQLException("failed to add ");
            }

            return id ;
        }

        public int delteMovie (String id )
        {
            if (id==null)
            {
                    return getWritableDatabase().delete(TABLE_NAME,null,null);

            }
            else
            {
                return getWritableDatabase().delete(TABLE_NAME , "_id=?" , new String  []{id}  );
            }
        }
    }


}
