package media.dee.dcms.db.neo4j.impl;

import media.dee.dcms.core.db.Record;
import org.neo4j.driver.v1.Value;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GraphRecord implements Record {

    private Function<Value, Object> mapValue = Value::asObject;

    private class RecordEntry implements Entry<String, Object>{
        private String key;
        private Object value;

        RecordEntry(String key, Value value){
            this.key = key;
            this.value = mapValue.apply(value);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            return this.value = value;
        }
    }

    private org.neo4j.driver.v1.Record record;

    public GraphRecord(org.neo4j.driver.v1.Record record){
        this.record = record;
    }

    @Override
    public int size() {
        return record.size();
    }

    @Override
    public boolean isEmpty() {
        return record.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return record.containsKey((String)key);
    }

    @Override
    public boolean containsValue(Object value) {
        return record.values().contains(value);
    }

    @Override
    public Object get(Object key) {
        return record.get((String)key);
    }

    @Override
    public Object put(String key, Object value) {
        throw new IllegalArgumentException("Read only record");
    }

    @Override
    public Object remove(Object key) {
        throw new IllegalArgumentException("Read only record");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new IllegalArgumentException("Read only record");
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException("Read only record");
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(record.keys());
    }

    @Override
    public Collection<Object> values() {
        return record.values().
                stream().
                map( mapValue )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.record.keys().stream()
                .map( v -> new RecordEntry(v, this.record.get(v)) )
                .collect(Collectors.toSet() );
    }
}
