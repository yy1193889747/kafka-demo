//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.kafka.clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Metadata {
    private static final Logger log = LoggerFactory.getLogger(Metadata.class);
    private final long refreshBackoffMs;
    private final long metadataExpireMs;
    private int version;
    private long lastRefreshMs;
    private long lastSuccessfulRefreshMs;
    private Cluster cluster;
    private boolean needUpdate;
    private final Set<String> topics;
    private final List<Metadata.Listener> listeners;
    private boolean needMetadataForAllTopics;

    public Metadata() {
        this(100L, 3600000L);
    }

    public Metadata(long refreshBackoffMs, long metadataExpireMs) {
        this.refreshBackoffMs = refreshBackoffMs;
        this.metadataExpireMs = metadataExpireMs;
        this.lastRefreshMs = 0L;
        this.lastSuccessfulRefreshMs = 0L;
        this.version = 0;
        this.cluster = Cluster.empty();
        this.needUpdate = false;
        this.topics = new HashSet();
        this.listeners = new ArrayList();
        this.needMetadataForAllTopics = false;
    }

    public synchronized Cluster fetch() {
        return this.cluster;
    }

    public synchronized void add(String topic) {
        this.topics.add(topic);
    }

    public synchronized long timeToNextUpdate(long nowMs) {
        long timeToExpire = this.needUpdate ? 0L : Math.max(this.lastSuccessfulRefreshMs + this.metadataExpireMs - nowMs, 0L);
        long timeToAllowUpdate = this.lastRefreshMs + this.refreshBackoffMs - nowMs;
        return Math.max(timeToExpire, timeToAllowUpdate);
    }

    public synchronized int requestUpdate() {
        this.needUpdate = true;
        return this.version;
    }

    public synchronized boolean updateRequested() {
        return this.needUpdate;
    }

    public synchronized void awaitUpdate(int lastVersion, long maxWaitMs) throws InterruptedException {
        if (maxWaitMs < 0L) {
            throw new IllegalArgumentException("Max time to wait for metadata updates should not be < 0 milli seconds");
        } else {
            long begin = System.currentTimeMillis();

            long elapsed;
            for(long remainingWaitMs = maxWaitMs; this.version <= lastVersion; remainingWaitMs = maxWaitMs - elapsed) {
                if (remainingWaitMs != 0L) {
                    this.wait(remainingWaitMs);
                }

                elapsed = System.currentTimeMillis() - begin;
                if (elapsed >= maxWaitMs) {
                    throw new TimeoutException("Failed to update metadata after " + maxWaitMs + " ms.");
                }
            }

        }
    }

    public synchronized void setTopics(Collection<String> topics) {
        if (!this.topics.containsAll(topics)) {
            this.requestUpdate();
        }

        this.topics.clear();
        this.topics.addAll(topics);
    }

    public synchronized Set<String> topics() {
        return new HashSet(this.topics);
    }

    public synchronized boolean containsTopic(String topic) {
        return this.topics.contains(topic);
    }

    public synchronized void update(Cluster cluster, long now) {
        this.needUpdate = false;
        this.lastRefreshMs = now;
        this.lastSuccessfulRefreshMs = now;
        ++this.version;
        Iterator i$ = this.listeners.iterator();

        while(i$.hasNext()) {
            Metadata.Listener listener = (Metadata.Listener)i$.next();
            listener.onMetadataUpdate(cluster);
        }

        this.cluster = this.needMetadataForAllTopics ? this.getClusterForCurrentTopics(cluster) : cluster;
        this.notifyAll();
        log.debug("Updated cluster metadata version {} to {}", this.version, this.cluster);
    }

    public synchronized void failedUpdate(long now) {
        this.lastRefreshMs = now;
    }

    public synchronized int version() {
        return this.version;
    }

    public synchronized long lastSuccessfulUpdate() {
        return this.lastSuccessfulRefreshMs;
    }

    public long refreshBackoff() {
        return this.refreshBackoffMs;
    }

    public synchronized void needMetadataForAllTopics(boolean needMetadataForAllTopics) {
        this.needMetadataForAllTopics = needMetadataForAllTopics;
    }

    public synchronized boolean needMetadataForAllTopics() {
        return this.needMetadataForAllTopics;
    }

    public synchronized void addListener(Metadata.Listener listener) {
        this.listeners.add(listener);
    }

    public synchronized void removeListener(Metadata.Listener listener) {
        this.listeners.remove(listener);
    }

    private Cluster getClusterForCurrentTopics(Cluster cluster) {
        Set<String> unauthorizedTopics = new HashSet();
        Collection<PartitionInfo> partitionInfos = new ArrayList();
        List<Node> nodes = Collections.emptyList();
        if (cluster != null) {
            unauthorizedTopics.addAll(cluster.unauthorizedTopics());
            unauthorizedTopics.retainAll(this.topics);
            Iterator i$ = this.topics.iterator();

            while(i$.hasNext()) {
                String topic = (String)i$.next();
                partitionInfos.addAll(cluster.partitionsForTopic(topic));
            }

            nodes = cluster.nodes();
        }

        return new Cluster(nodes, partitionInfos, unauthorizedTopics);
    }

    public interface Listener {
        void onMetadataUpdate(Cluster var1);
    }
}
