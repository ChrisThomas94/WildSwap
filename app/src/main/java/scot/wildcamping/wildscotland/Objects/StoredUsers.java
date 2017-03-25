package scot.wildcamping.wildscotland.Objects;

import android.util.SparseArray;

/**
 * Created by Chris on 25-Mar-17.
 */

public class StoredUsers {

    private User keyUser = new User();
    private SparseArray<User> otherUsers = new SparseArray<>();

    public User getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(User keyUser) {
        this.keyUser = keyUser;
    }

    public SparseArray<User> getOtherUsers() {
        return otherUsers;
    }

    public void setOtherUsers(SparseArray<User> otherUsers) {
        this.otherUsers = otherUsers;
    }
}
