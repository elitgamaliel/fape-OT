package com.inretailpharma.digital.ordertracker.dto.factory;

public abstract class CanonicalFactory {

    private static final TrackerCanonicalFactory TRACKER_CANONICAL_FACTORY = new TrackerCanonicalFactory();

    public static TrackerCanonicalFactory instanceOfTracker() {
        return TRACKER_CANONICAL_FACTORY;
    }

    public interface ParseCanonicalFactory<E, C> {
        C toCanonical(E entity);
    }

    public interface CreatedCanonicalFactory<E, C> {
        E toEntity(C canonical);
    }

    public interface UpdatedCanonicalFactory<E, C> {
        void toUpdated(C canonical, E entity);
    }
}
