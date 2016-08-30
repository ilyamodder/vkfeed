package ru.ilyamodder.vkfeed.rx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ilyamodder.vkfeed.model.Attachment;
import ru.ilyamodder.vkfeed.model.Group;
import ru.ilyamodder.vkfeed.model.Newsfeed;
import ru.ilyamodder.vkfeed.model.NewsfeedItem;
import ru.ilyamodder.vkfeed.model.Profile;
import ru.ilyamodder.vkfeed.model.VKResponse;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.model.local.JoinedPostsResponse;
import ru.ilyamodder.vkfeed.model.local.LocalPhoto;
import rx.Observable;

/**
 * Created by ilya on 25.08.16.
 */

public class Converters {
    public static Observable<JoinedPostsResponse> toJoinedPost(VKResponse<Newsfeed> source) {
        Observable<Newsfeed> serverData = Observable.just(source.getResponse());
        return Observable.zip(serverData.map(Newsfeed::getItems), serverData.map(Newsfeed::getProfiles),
                serverData.map(Newsfeed::getGroups), serverData, (items, profiles, groups, response) -> {
                    List<JoinedPost> posts = new ArrayList<>();
                    for (NewsfeedItem item : items) {
                        String name = "";
                        String photoUrl = "";
                        if (item.getSourceId() > 0) {
                            for (Profile profile : profiles) {
                                if (profile.getUserId() == item.getSourceId()) {
                                    name = profile.getFirstName() + " " + profile.getLastName();
                                    photoUrl = profile.getPhotoUrl();
                                    break;
                                }
                            }
                        } else {
                            for (Group group : groups) {
                                if (group.getGroupId() == -1 * item.getSourceId()) {
                                    name = group.getName();
                                    photoUrl = group.getPhotoUrl();
                                    break;
                                }
                            }
                        }

                        posts.add(new JoinedPost(item.getPostId(), item.getSourceId(), name,
                                new Date(item.getDate() * 1000),
                                photoUrl, item.getText(), toPhotoUrls(item.getAttachments()),
                                item.getLikesCount()));
                    }
                    return new JoinedPostsResponse(posts, response.getNextFrom());
                });
    }

    private static List<String> toPhotoUrls(List<Attachment> attachments) {
        if (attachments == null) {
            return new ArrayList<>();
        }
        return Observable.from(attachments)
                .filter(attachment -> attachment.getType().equals("photo"))
                .map(attachment -> attachment.getPhoto().getPhoto604())
                .toList()
                .toBlocking()
                .first();
    }

    public static Observable<LocalPhoto> toLocalPhotos(NewsfeedItem item) {
        long postId = item.getPostId();
        long sourceId = item.getSourceId();
        return Observable.just(item)
                .map(NewsfeedItem::getAttachments)
                .filter(list -> list != null)
                .flatMap(Observable::from)
                .filter(attachment -> attachment.getType().equals("photo"))
                .map(Attachment::getPhoto)
                .map(photo -> new LocalPhoto(photo.getId(), postId, sourceId,
                        photo.getPhoto75(), photo.getPhoto130(), photo.getPhoto604()));
    }
}
