package io.mon.noticationbox.objects;

/**
 * Created by son-tu on 7/10/2016.
 */
public class Items_notification {
    String package_name, id, title, content, time;

    public Items_notification(String id, String package_name, String title, String content, String time) {
        this.id = id;
        this.package_name = package_name;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getPackage() {
        return package_name;
    }

    public void setPackage(String package_name) {
        this.package_name = package_name;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }


}
