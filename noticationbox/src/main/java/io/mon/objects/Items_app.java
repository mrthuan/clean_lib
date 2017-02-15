package io.mon.objects;

/**
 * Created by son-tu on 7/10/2016.
 */
public class Items_app {
    String package_name, cachesize, datasize;

    public Items_app(String package_name, String cachesize, String datasize) {
        this.cachesize = cachesize;
        this.package_name = package_name;
        this.datasize = datasize;
    }

    public String getPackage() {
        return package_name;
    }

    public void setPackage(String package_name) {
        this.package_name = package_name;
    }

}
