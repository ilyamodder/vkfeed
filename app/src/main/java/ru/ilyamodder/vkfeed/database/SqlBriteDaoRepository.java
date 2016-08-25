package ru.ilyamodder.vkfeed.database;

import android.database.sqlite.SQLiteDatabase;

import com.hannesdorfmann.sqlbrite.dao.Dao;

import java.util.List;

import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.model.local.JoinedPostMapper;
import ru.ilyamodder.vkfeed.model.local.LocalGroup;
import ru.ilyamodder.vkfeed.model.local.LocalNewsfeedItem;
import ru.ilyamodder.vkfeed.model.local.LocalPhoto;
import ru.ilyamodder.vkfeed.model.local.LocalProfile;
import ru.ilyamodder.vkfeed.repository.LocalRepository;
import rx.Observable;

/**
 * Created by ilya on 20.08.16.
 */

public class SqlBriteDaoRepository extends Dao implements LocalRepository {
    @Override
    public void createTable(SQLiteDatabase database) {
        CREATE_TABLE(LocalNewsfeedItem.TABLE_NAME,
                LocalNewsfeedItem.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalNewsfeedItem.COL_DATE + " LONG",
                LocalNewsfeedItem.COL_IS_PROFILE_SRC + " BOOLEAN",
                LocalNewsfeedItem.COL_SOURCE_ID + " LONG",
                LocalNewsfeedItem.COL_TEXT + " TEXT",
                LocalNewsfeedItem.COL_LIKES_COUNT + " INTEGER").execute(database);
        CREATE_TABLE(LocalGroup.TABLE_NAME,
                LocalGroup.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalGroup.COL_NAME + " VARCHAR",
                LocalGroup.COL_PHOTO_URL + " VARCHAR");
        CREATE_TABLE(LocalPhoto.TABLE_NAME,
                LocalPhoto.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalPhoto.COL_PHOTO_75 + " VARCHAR",
                LocalPhoto.COL_PHOTO_130 + " VARCHAR",
                LocalPhoto.COL_PHOTO_604 + " VARCHAR");
        CREATE_TABLE(LocalProfile.TABLE_NAME,
                LocalProfile.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalProfile.COL_FIRST_NAME + " VARCHAR",
                LocalProfile.COL_LAST_NAME + " VARCHAR",
                LocalProfile.COL_PHOTO_URL + " VARCHAR");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Observable<List<JoinedPost>> getNewsfeed(int offset, int count) {
        return query(SELECT(LocalNewsfeedItem.COL_ID, LocalNewsfeedItem.COL_DATE,
                LocalNewsfeedItem.COL_TEXT, "coalesce(p." + LocalProfile.COL_FIRST_NAME + " || p."
                        + LocalProfile.COL_LAST_NAME + ", g." + LocalGroup.COL_NAME + ") AS name",
                "COALESCE(" + LocalGroup.COL_PHOTO_URL + ", " + LocalProfile.COL_PHOTO_URL + " AS avatar")
                .FROM(LocalNewsfeedItem.TABLE_NAME)
                .LEFT_OUTER_JOIN(LocalProfile.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_IS_PROFILE_SRC + " AND " +
                        LocalProfile.TABLE_NAME + "." + LocalProfile.COL_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .LEFT_OUTER_JOIN(LocalGroup.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_IS_PROFILE_SRC + " = 0 AND " +
                        LocalGroup.TABLE_NAME + "." + LocalGroup.COL_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .LIMIT(offset + ", " + count))
                .run()
                .mapToList(JoinedPostMapper.MAPPER);
    }
}
