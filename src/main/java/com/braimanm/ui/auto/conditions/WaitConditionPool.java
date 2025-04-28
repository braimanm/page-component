/*
Copyright 2010-2024 Michael Braiman braimanm@gmail.com
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.braimanm.ui.auto.conditions;

import java.util.*;
import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class WaitConditionPool {
    private final long timeoutMillis;
    private final long pollIntervalMillis;
    private final Map<String, WaitCondition> conditions = new LinkedHashMap<>();
    private final List<String> fulfilledConditions = new ArrayList<>();

    public WaitConditionPool(long timeoutMillis) {
        this(timeoutMillis, 300); // default 300ms poll interval
    }

    public WaitConditionPool(long timeoutMillis, long pollIntervalMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollIntervalMillis = pollIntervalMillis;
    }

    public WaitCondition add(String name, WaitCondition condition) {
        conditions.put(name, condition);
        return condition;
    }

    public List<String> getFulfilledConditions() {
        return Collections.unmodifiableList(fulfilledConditions);
    }

    public boolean waitForFirst() {
        return waitUntil(() -> {
            for (Map.Entry<String, WaitCondition> entry : conditions.entrySet()) {
                try {
                    if (entry.getValue().evaluate()) {
                        fulfilledConditions.add(entry.getKey());
                        return true;
                    }
                } catch (Exception ignored) {
                    // ignored
                }
            }
            return false;
        });
    }

    public boolean waitForAll() {
        return waitUntil(() -> {
            for (Map.Entry<String, WaitCondition> entry : conditions.entrySet()) {
                try {
                    if (!fulfilledConditions.contains(entry.getKey()) && entry.getValue().evaluate()) {
                        fulfilledConditions.add(entry.getKey());
                    }
                } catch (Exception ignored) {
                    // ignored
                }
            }
            return fulfilledConditions.size() == conditions.size();
        });
    }

    public boolean waitCustom(BiFunction<Map<String, WaitCondition>, List<String>, Boolean> customLogic) {
        return waitUntil(() -> customLogic.apply(conditions, fulfilledConditions));
    }

    private boolean waitUntil(WaitCondition combinedCondition) {
        if (conditions.isEmpty()) {
            throw new EmptyConditionPoolException();
        }

        long endTime = System.currentTimeMillis() + timeoutMillis;

        while (System.currentTimeMillis() < endTime) {
            if (combinedCondition.evaluate()) {
                return true;
            }
            sleep(pollIntervalMillis);
        }
        return false;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Waiting was interrupted", e);
        }
    }
}