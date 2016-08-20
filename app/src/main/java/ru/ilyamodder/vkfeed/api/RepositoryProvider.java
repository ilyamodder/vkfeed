package ru.ilyamodder.vkfeed.api;

import ru.ilyamodder.vkfeed.repository.LocalRepository;
import ru.ilyamodder.vkfeed.repository.RemoteRepository;

/**
 * Created by ilya on 20.08.16.
 */

public class RepositoryProvider {

    private static LocalRepository sLocalRepository;

    private static RemoteRepository sRemoteRepository;

    public static LocalRepository getLocalRepository() {
        return sLocalRepository;
    }

    public static RemoteRepository getRemoteRepository() {
        return sRemoteRepository;
    }

    public static void setLocalRepository(LocalRepository localRepository) {
        sLocalRepository = localRepository;
    }

    public static void setRemoteRepository(RemoteRepository remoteRepository) {
        sRemoteRepository = remoteRepository;
    }
}
