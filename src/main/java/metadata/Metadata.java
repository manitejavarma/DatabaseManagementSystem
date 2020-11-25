package metadata;

import attributetype.AttributeType;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private String tableName;
    private Map<String, AttributeType> columns;

    public Metadata() {
        columns = new HashMap<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, AttributeType> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, AttributeType> columns) {
        this.columns = columns;
    }
}
