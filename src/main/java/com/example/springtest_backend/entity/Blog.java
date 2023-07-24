package com.example.springtest_backend.entity;

public class Blog {


    // 数据库字段
    private int id;
    private String title;
    private String headImg;
    private String content;
    private String summary;
    private int isFinished;
    private int isTop;
    private String createTime;
    private String updateTime;
    private int ownerId;
    private int updateUserId;
    private int categoryId;
    private String tabs;
//    private String tabs;



    // 为JSON保留字段
//    private String contentRaw;


    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public String getContentRaw() {
//        return contentRaw;
//    }
//
//    public void setContentRaw(String contentRaw) {
//        this.contentRaw = contentRaw;
//    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headImg='" + headImg + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", isFinished=" + isFinished +
                ", isTop=" + isTop +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", ownerId=" + ownerId +
                ", updateUserId=" + updateUserId +
                ", categoryId=" + categoryId +
//                ", contentRaw='" + contentRaw + '\'' +
                ", tabs=" + tabs +
                '}';
    }
}
