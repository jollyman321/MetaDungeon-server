package sbs.immovablerod.metaDungeon.util;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadSqlTable {
    public static HashMap<String, HashMap<String, String>> loadTable(String table, String columnKey) {
        SQL database = null;
        HashMap<String, HashMap<String, String>> output = new HashMap<>();


        try {
            database = new SQL("plugins" + File.separator + "skillfulhacks" + File.separator + "database.sqlite");

            try (ResultSet query = database.execute_query("SELECT * FROM " + table)) {
                ResultSetMetaData resultSetMetaData = query.getMetaData();
                final int columnCount = resultSetMetaData.getColumnCount();

                while(query.next())
                {
                    output.put(query.getString(columnKey), new HashMap<>());
                    for (int i = 1; i <= columnCount; i++) {
                        output.get(query.getString(columnKey)).put(resultSetMetaData.getColumnName(i),
                                query.getString(i)
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        } finally {
            database.Close();
        }
        return output;
    }

    public static HashMap<String, HashMap<String,HashMap<String, List<String>>>> tableToNestedHashMap(
            HashMap<String, HashMap<String, String>> table,
            String key1,
            String key2,
            String key3) {
        HashMap<String, HashMap<String,HashMap<String, List<String>>>> output = new HashMap<>();
        for (String key : table.keySet()) {
            if (!output.containsKey(table.get(key).get(key1))) {
                output.put(table.get(key).get(key1), new HashMap<>());
            }
            if (!output.get(table.get(key).get(key1)).containsKey(table.get(key).get(key2))) {
                output.get
                        (table.get(key).get(key1)).put(table.get(key).get(key2), new HashMap<>()
                );
            }
            if (!output.get(table.get(key).get(key1)).get(table.get(key).get(key2)).containsKey(table.get(key).get(key3))) {
                output.get(
                        table.get(key).get(key1)).get(table.get(key).get(key2)).put(table.get(key).get(key3), new ArrayList<>()
                );
            }
            output.get(
                    table.get(key).get(key1)).get(
                    table.get(key).get(key2)).get(
                    table.get(key).get(key3)).add(key);
        }

        return output;


    }
}
