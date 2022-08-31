package com.example.ahmadhasim.ilabinventory.hilang;

/**
 * Created by AHMAD HASIM on 9/13/2016.
 */
public class HilangData {

    private String id, sub_id, nama, serial, tgl_hilang ,tgl_ketemu, catatan;

    public HilangData() {
    }

    public HilangData(String id, String sub_id, String nama, String serial, String tgl_hilang, String tgl_ketemu, String catatan) {
        this.id             = id;
        this.sub_id         = sub_id;
        this.nama           = nama;
        this.serial         = serial;
        this.tgl_hilang     = tgl_hilang;
        this.tgl_ketemu     = tgl_ketemu;
        this.catatan        = catatan;
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
        return nama;
    }

    public void setName(String nama){
        this.nama = nama;
    }

    public String getSerial(){
        return serial;
    }

    public void setSerial(String serial){
        this.serial = serial;
    }

    public String getTglHilang() {
        return tgl_hilang;
    }

    public void setTglHilang(String tgl_hilang) {
        this.tgl_hilang = tgl_hilang;
    }

    public String getTglKetemu() {
        return tgl_ketemu;
    }

    public void setTglKetemu(String tgl_ketemu) {
        this.tgl_ketemu = tgl_ketemu;
    }

    public String getNote(){
        return catatan;
    }

    public void setNote(String catatan){
        this.catatan = catatan;
    }
}
