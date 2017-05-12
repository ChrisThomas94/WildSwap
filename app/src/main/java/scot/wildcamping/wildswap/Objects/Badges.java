package scot.wildcamping.wildswap.Objects;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

/**
 * Created by Chris on 09-Apr-16.
 *
 */
public class Badges {

    private SparseBooleanArray collection = new SparseBooleanArray();
    private SparseIntArray badgesResource = new SparseIntArray();
    private SparseArray<Badge> badges = new SparseArray<>();

    public SparseArray<Badge> getBadges() {
        return badges;
    }

    public void setBadges(SparseArray<Badge> badges) {
        this.badges = badges;
    }

    public SparseIntArray getBadgesResource() {
        return badgesResource;
    }

    public void setBadgesResource(SparseIntArray badgesResource) {
        this.badgesResource = badgesResource;
    }

    public void setCollection(SparseBooleanArray collection){
        this.collection = collection;
    }

    public SparseBooleanArray getCollection(){
        return collection;
    }



}
