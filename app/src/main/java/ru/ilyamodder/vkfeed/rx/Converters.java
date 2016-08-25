package ru.ilyamodder.vkfeed.rx;

import java.util.Date;
import java.util.List;

import ru.ilyamodder.vkfeed.model.Attachment;
import ru.ilyamodder.vkfeed.model.Group;
import ru.ilyamodder.vkfeed.model.Newsfeed;
import ru.ilyamodder.vkfeed.model.Profile;
import ru.ilyamodder.vkfeed.model.VKResponse;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import rx.Observable;

/**
 * Created by ilya on 25.08.16.
 */

public class Converters {
    public static Observable<List<JoinedPost>> toJoinedPost(Observable<VKResponse<Newsfeed>> source) {
        Observable<Newsfeed> serverData = source.map(VKResponse::getResponse);
        return Observable.zip(serverData.map(Newsfeed::getItems).flatMap(Observable::from), serverData.map(Newsfeed::getProfiles),
                serverData.map(Newsfeed::getGroups), (item, profiles, groups) -> {
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
                    return new JoinedPost(item.getPostId(), name, new Date(item.getDate()),
                            photoUrl, item.getText(), toPhotoUrls(item.getAttachments()),
                            item.getLikesCount());
                }).toList();
    }

    private static List<String> toPhotoUrls(List<Attachment> attachments) {
        return Observable.from(attachments)
                .filter(attachment -> attachment.getType().equals("photo"))
                .map(attachment -> attachment.getPhoto().getPhoto604())
                .toList()
                .toBlocking()
                .first();
    }
}
