package org.acme.daos;

import org.acme.generated.AbstractDTO;
import org.jooq.Field;
import org.jooq.Record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordToViewMapper<E extends AbstractDTO, K extends Serializable> {

    private Class<E> clazz;
    private Field<K> groupField;
    private List<Field<?>> uniqueFields;

        public RecordToViewMapper(
            Class<E> clazz,
            Field<K> groupField,
            List<Field<?>> uniqueFields) {
        this.clazz = clazz;
        this.groupField = groupField;
        this.uniqueFields = uniqueFields;
    }

    /**
     * Helper-function that filters the given records to return a list,
     * that contains each record that fulfills the unique-criteria only once.
     *
     * @param records records
     * @return filtered records.
     */
    private Map<K, List<Record>> filterRecordsByUniqueFieldsAndGroupByIdField(List<Record> records) {
        final Map<String, Record> uniqueRecordMap = new LinkedHashMap<>();
        for (Record record : records) {
            String key = "";
            for (Field<?> uniqueField : uniqueFields) {
                String test = record.getValue(uniqueField).toString();
                key += test;
            }
            uniqueRecordMap.put(key, record);
        }
        final List<Record> uniqueRecords = uniqueRecordMap.values().stream().toList();
        final Map<K, List<Record>> result = new LinkedHashMap<>();
        for (Record record : uniqueRecords) {
            K key = record.getValue(groupField);
            if(result.containsKey(key)){
                List<Record> list = result.get(key);
                list.add(record);
            } else {
                List<Record> list = new ArrayList<Record>();
                list.add(record);
                result.put(key, list);
            }
        }
        return result;
    }

    /**
     * Convert the given records to the expected DTO class type
     * @param records records
     * @return dtos
     */
    private List<E> convertRecordsToView(List<Record> records) {
        List<E> viewDTOs = new ArrayList<>();
        for (Record record : records) {
            try {
                viewDTOs.add(record.into(clazz.newInstance()));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return viewDTOs;
    }

    public List<E> extractRecords(List<Record> records) throws RuntimeException {
        Map<K, List<Record>> map = filterRecordsByUniqueFieldsAndGroupByIdField(records);
        List<Record> filteredRecords = new ArrayList<>();
        for (List<Record> v : map.values()) {
            // we expect that we always have exactly 1 record in this case per key.
            filteredRecords.add(v.get(0));
        }
        return convertRecordsToView(filteredRecords);
    }
    public Map<K, List<E>> extractRecordsGroupedBy(List<Record> records) throws RuntimeException {
        Map<K, List<Record>> map = filterRecordsByUniqueFieldsAndGroupByIdField(records);
        Map<K, List<E>> convertedMap = new LinkedHashMap<>();
        for (Map.Entry<K, List<Record>> entry : map.entrySet() ) {
            List<E> viewDTOs = convertRecordsToView(entry.getValue());
            convertedMap.put(entry.getKey(), viewDTOs);
        }
        return convertedMap;
    }
}