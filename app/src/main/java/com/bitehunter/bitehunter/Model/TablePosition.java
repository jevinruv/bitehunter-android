package com.bitehunter.bitehunter.Model;

/**
 * Created by Jevin on 23-Oct-17.
 */

public class TablePosition {
    private String table_id;
    private String status;
    private int left_Margin;
    private int top_Margin;

    public TablePosition(String table_id, int left_Margin, int top_Margin) {
        this.table_id = table_id;
        this.left_Margin = left_Margin;
        this.top_Margin = top_Margin;
    }

    public TablePosition() {
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public int getLeft_Margin() {
        return left_Margin;
    }

    public void setLeft_Margin(int left_Margin) {
        this.left_Margin = left_Margin;
    }

    public int getTop_Margin() {
        return top_Margin;
    }

    public void setTop_Margin(int top_Margin) {
        this.top_Margin = top_Margin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
