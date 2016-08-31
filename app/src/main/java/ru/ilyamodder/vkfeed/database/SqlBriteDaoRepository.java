package ru.ilyamodder.vkfeed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.hannesdorfmann.sqlbrite.dao.Dao;
import com.squareup.sqlbrite.BriteDatabase;
import com.vk.sdk.VKSdk;

import java.util.Date;
import java.util.List;

import ru.ilyamodder.vkfeed.model.Newsfeed;
import ru.ilyamodder.vkfeed.model.VKResponse;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.model.local.JoinedPostMapper;
import ru.ilyamodder.vkfeed.model.local.LocalGroup;
import ru.ilyamodder.vkfeed.model.local.LocalGroupMapper;
import ru.ilyamodder.vkfeed.model.local.LocalNewsfeedItem;
import ru.ilyamodder.vkfeed.model.local.LocalNewsfeedItemMapper;
import ru.ilyamodder.vkfeed.model.local.LocalPhoto;
import ru.ilyamodder.vkfeed.model.local.LocalPhotoMapper;
import ru.ilyamodder.vkfeed.model.local.LocalProfile;
import ru.ilyamodder.vkfeed.model.local.LocalProfileMapper;
import ru.ilyamodder.vkfeed.repository.LocalRepository;
import ru.ilyamodder.vkfeed.rx.Converters;
import rx.Observable;

/**
 * Created by ilya on 20.08.16.
 */

public class SqlBriteDaoRepository extends Dao implements LocalRepository {
    public static final String NEXT_OFFSET_KEY = "next_offset";

    private Context mContext;

    public SqlBriteDaoRepository(Context context) {
        mContext = context;
    }

    @Override
    public void createTable(SQLiteDatabase database) {
        CREATE_TABLE(LocalNewsfeedItem.TABLE_NAME,
                LocalNewsfeedItem.COL_ID + " LONG NOT NULL",
                LocalNewsfeedItem.COL_SOURCE_ID + " LONG NOT NULL",
                LocalNewsfeedItem.COL_DATE + " LONG NOT NULL",
                LocalNewsfeedItem.COL_TEXT + " TEXT NOT NULL",
                LocalNewsfeedItem.COL_LIKES_COUNT + " INTEGER NOT NULL",
                "PRIMARY KEY (" + LocalNewsfeedItem.COL_ID + ", "
                        + LocalNewsfeedItem.COL_SOURCE_ID + ")")
                .execute(database);
        CREATE_TABLE(LocalGroup.TABLE_NAME,
                LocalGroup.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalGroup.COL_NAME + " VARCHAR NOT NULL",
                LocalGroup.COL_PHOTO_URL + " VARCHAR NOT NULL")
                .execute(database);
        CREATE_TABLE(LocalPhoto.TABLE_NAME,
                LocalPhoto.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalPhoto.COL_POST_ID + " LONG NOT NULL",
                LocalPhoto.COL_POST_SRC_ID + " LONG NOT NULL",
                LocalPhoto.COL_PHOTO_75 + " VARCHAR NOT NULL",
                LocalPhoto.COL_PHOTO_130 + " VARCHAR NOT NULL",
                LocalPhoto.COL_PHOTO_604 + " VARCHAR")
                .execute(database);
        CREATE_TABLE(LocalProfile.TABLE_NAME,
                LocalProfile.COL_ID + " LONG PRIMARY KEY NOT NULL",
                LocalProfile.COL_FIRST_NAME + " VARCHAR NOT NULL",
                LocalProfile.COL_LAST_NAME + " VARCHAR NOT NULL",
                LocalProfile.COL_PHOTO_URL + " VARCHAR NOT NULL")
                .execute(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Observable<List<JoinedPost>> getNewsfeed(int offset, int count) {
        return query(SELECT(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_ID,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_DATE,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_TEXT,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_LIKES_COUNT,
                "coalesce(" + LocalProfile.TABLE_NAME + "." + LocalProfile.COL_FIRST_NAME +
                        " || ' ' ||  " +
                        LocalProfile.TABLE_NAME + "." + LocalProfile.COL_LAST_NAME +
                        ", " + LocalGroup.TABLE_NAME + "." + LocalGroup.COL_NAME + ") AS name",
                "COALESCE(" + LocalGroup.TABLE_NAME + "." + LocalGroup.COL_PHOTO_URL +
                        ", " + LocalProfile.TABLE_NAME + "." + LocalProfile.COL_PHOTO_URL + ") AS avatar")
                .FROM(LocalNewsfeedItem.TABLE_NAME)
                .LEFT_OUTER_JOIN(LocalProfile.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID + " >0 AND " +
                        LocalProfile.TABLE_NAME + "." + LocalProfile.COL_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .LEFT_OUTER_JOIN(LocalGroup.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID + " < 0 AND " +
                        LocalGroup.TABLE_NAME + "." + LocalGroup.COL_ID + " = (-1)*" +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .LIMIT(offset + ", " + count))
                .run()
                .mapToList(JoinedPostMapper.MAPPER);
    }

    @Override
    public Observable<JoinedPost> getPost(long id, long sourceId) {
        return query(SELECT(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_ID,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_DATE,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_TEXT,
                LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_LIKES_COUNT,
                "coalesce(" + LocalProfile.TABLE_NAME + "." + LocalProfile.COL_FIRST_NAME +
                        " || ' ' ||  " +
                        LocalProfile.TABLE_NAME + "." + LocalProfile.COL_LAST_NAME +
                        ", " + LocalGroup.TABLE_NAME + "." + LocalGroup.COL_NAME + ") AS name",
                "COALESCE(" + LocalGroup.TABLE_NAME + "." + LocalGroup.COL_PHOTO_URL +
                        ", " + LocalProfile.TABLE_NAME + "." + LocalProfile.COL_PHOTO_URL + ") AS avatar",
                "(SELECT GROUP_CONCAT(" + LocalPhoto.TABLE_NAME + "." + LocalPhoto.COL_PHOTO_604 +
                        ") FROM " + LocalPhoto.TABLE_NAME +
                        " WHERE " + LocalPhoto.TABLE_NAME + "." + LocalPhoto.COL_POST_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_ID + " AND " +
                        LocalPhoto.TABLE_NAME + "." + LocalPhoto.COL_POST_SRC_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID + ") AS photos")
                .FROM(LocalNewsfeedItem.TABLE_NAME)
                .LEFT_OUTER_JOIN(LocalProfile.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID + " >0 AND " +
                        LocalProfile.TABLE_NAME + "." + LocalProfile.COL_ID + " = " +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .LEFT_OUTER_JOIN(LocalGroup.TABLE_NAME)
                .ON(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID +
                        " < 0 AND " + LocalGroup.TABLE_NAME + "." + LocalGroup.COL_ID + " = (-1)*" +
                        LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_SOURCE_ID)
                .WHERE(LocalNewsfeedItem.TABLE_NAME + "." + LocalNewsfeedItem.COL_ID + "=" + id +
                        " AND " +
                        LocalNewsfeedItem.TABLE_NAME + "." +
                        LocalNewsfeedItem.COL_SOURCE_ID + "=" + sourceId))
                .run()
                .mapToOne(JoinedPostMapper.MAPPER);
    }

    @Override
    public void insertPosts(VKResponse<Newsfeed> newsfeedResponse) {
        Newsfeed newsfeed = newsfeedResponse.getResponse();
        BriteDatabase.Transaction transaction = newTransaction();
        Observable<Long> groupsObservable = Observable.from(newsfeed.getGroups())
                .flatMap(group -> insert(LocalGroup.TABLE_NAME,
                        LocalGroupMapper.contentValues()
                                .mId(group.getGroupId())
                                .mName(group.getName())
                                .mPhotoUrl(group.getPhotoUrl())
                                .build(), SQLiteDatabase.CONFLICT_REPLACE)
                );
        Observable<Long> profilesObservable = Observable.from(newsfeed.getProfiles())
                .flatMap(profile -> insert(LocalProfile.TABLE_NAME,
                        LocalProfileMapper.contentValues()
                                .mId(profile.getUserId())
                                .mFirstName(profile.getFirstName())
                                .mLastName(profile.getLastName())
                                .mPhotoUrl(profile.getPhotoUrl())
                                .build(), SQLiteDatabase.CONFLICT_REPLACE)
                );
        Observable<Long> postsObservable = Observable.from(newsfeed.getItems())
                .flatMap(newsfeedItem -> insert(LocalNewsfeedItem.TABLE_NAME,
                        LocalNewsfeedItemMapper.contentValues()
                                .mId(newsfeedItem.getPostId())
                                .mSourceId(newsfeedItem.getSourceId())
                                .mText(newsfeedItem.getText())
                                .mDate(new Date(newsfeedItem.getDate() * 1000))
                                .mLikesCount(newsfeedItem.getLikesCount())
                                .build(), SQLiteDatabase.CONFLICT_REPLACE)
                );
        Observable<Long> photosObservable = Observable.from(newsfeed.getItems())
                .flatMap(Converters::toLocalPhotos)
                .flatMap(photo -> insert(LocalPhoto.TABLE_NAME,
                        LocalPhotoMapper.contentValues()
                                .mId(photo.getId())
                                .mPostId(photo.getPostId())
                                .mPostSrcId(photo.getPostSrcId())
                                .mPhoto75(photo.getPhoto75())
                                .mPhoto130(photo.getPhoto130())
                                .mPhoto604(photo.getPhoto604())
                                .build(), SQLiteDatabase.CONFLICT_REPLACE));
        Observable.concat(groupsObservable, profilesObservable, postsObservable, photosObservable)
                .subscribe(id -> {
                        }, throwable -> {
                            throwable.printStackTrace();
                            transaction.end();
                        },
                        () -> {
                            transaction.markSuccessful();
                            transaction.end();
                        });

    }

    @Override
    public String getNextOffset() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(NEXT_OFFSET_KEY, "");
    }

    @Override
    public void setNextOffset(String nextOffset) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit().putString(NEXT_OFFSET_KEY, nextOffset).apply();
    }

    @Override
    public void clearAll() {
        BriteDatabase.Transaction transaction = newTransaction();
        delete(LocalPhoto.TABLE_NAME)
                .concatWith(delete(LocalGroup.TABLE_NAME))
                .concatWith(delete(LocalNewsfeedItem.TABLE_NAME))
                .concatWith(delete(LocalProfile.TABLE_NAME))
                .subscribe(integer -> {
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            transaction.end();
                        },
                        () -> {
                            transaction.markSuccessful();
                            transaction.end();
                        });
    }

    @Override
    public void logout() {
        VKSdk.logout();
    }

    @Override
    public boolean isLoggedIn() {
        return VKSdk.isLoggedIn();
    }
}
