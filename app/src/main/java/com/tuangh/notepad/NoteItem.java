package com.tuangh.notepad;

import java.util.ArrayList;

public class NoteItem {
    private String content;
    private String createdDate;

    public NoteItem() {
    }

    public NoteItem(String content, String title, String createdDate) {
        this.content = content;
        this.createdDate = createdDate;
        this.title = title;
    }


    private  String  title;

    public NoteItem(MainActivity mainActivity, int dong_noi_dung, ArrayList<NoteItem> arrayNote) {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
