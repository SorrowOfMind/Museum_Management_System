package com.museum.client;

import com.museum.DataModels.Age;
import com.museum.Enums.DatabaseQuery;
import com.museum.Enums.Table;

public  class QueryHelper<T> {

    private String ConstructWhereClauseForTable(Table table, T params){

        switch(table){
            case Age:
                final Age ageParams = (Age)params;
                return " where Age.name like " + ageParams.name + "%";
            default: return "";
        }
    }
    public String ConstructQuery(DatabaseQuery type, Table table, T params  ) {
        switch(type){
            case Select: {
                return "select * from " + table + " " + this.ConstructWhereClauseForTable(table, params);
            }
            default: return "";
        }
    }
}
