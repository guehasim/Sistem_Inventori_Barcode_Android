package com.example.ahmadhasim.ilabinventory.inventaris;

/**
 * Created by AHMAD HASIM on 8/20/2016.
 */
public class InventData {

    private String id, sub_id, parent, name, merk, model,serial,kondisi, sedia,tahun;

    public InventData() {
    }

    public InventData(String id, String sub_id, String parent, String name, String merk, String model, String serial, String kondisi,
                       String sedia, String tahun) {
        this.id = id;
        this.sub_id = sub_id;
        this.parent = parent;
        this.name = name;
        this.merk = merk;
        this.model = model;
        this.serial = serial;
        this.kondisi = kondisi;
        this.sedia = sedia;
        this.tahun = tahun;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub_id(){
        return sub_id;
    }

    public void setSub_id(String sub_id){
        this.sub_id = sub_id;
    }

    public String getName(){
        return name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial(){
        return serial;
    }

    public void setSerial(String serial){
        this.serial = serial;
    }

    public String getKondisi(){
        return kondisi;
    }

    public void setKondisi(String kondisi){
        this.kondisi = kondisi;
    }

    public String getSedia(){
        return sedia;
    }

    public void setSedia(String sedia){
        this.sedia = sedia;
    }

    public String getTahun(){
        return tahun;
    }

    public void setTahun(String tahun){
        this.tahun = tahun;
    }
}
