package scot.wildcamping.wildscotland.Objects;

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
