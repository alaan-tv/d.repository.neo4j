/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package media.dee.dcms.db.neo4j.impl;

import media.dee.dcms.core.GraphNode;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;

import java.util.*;

public class Neo4jGraphNode implements GraphNode {
    private Node node;


    Neo4jGraphNode(Node node){
        this.node = node;
    }

    @Override
    public Collection<String> getLabels() {
        LinkedList<String> labels = new LinkedList<>();
        this.node.labels().iterator()
                .forEachRemaining(labels::addFirst);
        return labels;
    }

    @Override
    public long getRawId() {
        return this.node.id();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        Value value = this.node.get(key);
        if( value == null )
            return null;
        return (T) value.asObject();
    }

    @Override
    public boolean contains(String key) {
        return this.node.containsKey(key);
    }

    @Override
    public int size() {
        return this.node.size();
    }

    @Override
    public boolean isEmpty() {
        return this.node.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (String s : this.node.keys()) {
            if (key.equals(s))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(Object v : this.node.values() ){
            if( value.equals( v ))
                return true;
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        return get(key.toString());
    }

    @Override
    public Object put(String key, Object value) {
        throw new RuntimeException("Not Allowed");
    }

    @Override
    public Object remove(Object key) {
        throw new RuntimeException("Not Allowed");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new RuntimeException("Not Allowed");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not Allowed");
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        this.node.keys().iterator()
                .forEachRemaining(keys::add);
        return keys;
    }

    @Override
    public Collection<Object> values() {
        LinkedList<Object> values = new LinkedList<>();
        this.node.labels().iterator()
                .forEachRemaining(values::addLast);
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }
}
