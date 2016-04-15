package org.gooru.nucleus.handlers.copier.bootstrap.shutdown;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gooru.nucleus.handlers.copier.app.components.DataSourceRegistry;

public class Finalizers implements Iterable<Finalizer> {

    private List<Finalizer> finalizers = null;
    private final Iterator<Finalizer> internalIterator;

    @Override
    public Iterator<Finalizer> iterator() {
        return new Iterator<Finalizer>() {

            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Finalizer next() {
                return internalIterator.next();
            }

        };
    }

    public Finalizers() {
        finalizers = new ArrayList<>();
        finalizers.add(DataSourceRegistry.getInstance());
        internalIterator = finalizers.iterator();
    }

}
