package com.androidgame.jingfu.fjturtle.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsomemark on 6/14/16.
 */
public class Pool<T> { //pool 是用于资源管理的一个容器。T的类型取决于使用环境

    public interface PoolObjectFactory<T> { // factory pattern
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize){
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject() {
        T object = null;
        if (freeObjects.size() == 0) // if pool has nothing left, create a new one
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1); // fetch a free object from the pool
        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
