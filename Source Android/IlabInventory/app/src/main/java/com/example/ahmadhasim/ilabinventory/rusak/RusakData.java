package com.example.ahmadhasim.ilabinventory.rusak;

/**
 * Created by AHMAD HASIM on 9/9/2016.
 */
public class RusakData {

    private String id, sub_id, nama, serial, rusak, lokasi, tgl_kerusakan ,tgl_perbaikan, yg_perbaiki, tgl_selesai, stl_perbaikan, catatan, broken_id;

    public RusakData() {
    }

    public RusakData(String id, String sub_id, String nama, String serial, String rusak, String lokasi, String tgl_kerusakan,
                     String tgl_perbaikan, String yg_perbaiki, String tgl_selesai, String stl_perbaikan, String catatan, String broken_id) {
        this.id             = id;
        this.sub_id         = sub_id;
        this.nama           = nama;
        this.serial         = serial;
        this.rusak          = rusak;
        this.lokasi         = lokasi;
        this.tgl_kerusakan  = tgl_kerusakan;
        this.tgl_perbaikan  = tgl_perbaikan;
        this.yg_perbaiki    = yg_perbaiki;
        this.tgl_selesai    = tgl_selesai;
        this.stl_perbaikan  = stl_perbaikan;
        this.catatan        = catatan;
        this.broken_id      = broken_id;
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

    public String getRusak(){
        return rusak;
    }

    public void setRusak(String rusak){
        this.rusak = rusak;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTglRusak() {
        return tgl_kerusakan;
    }

    public void setTglRusak(String tgl_kerusakan) {
        this.tgl_kerusakan = tgl_kerusakan;
    }

    public String getTglPerbaiki() {
        return tgl_perbaikan;
    }

    public void setTglPerbaiki(String tgl_perbaikan) {
        this.tgl_perbaikan = tgl_perbaikan;
    }

    public String getYgPerbaiki(){
        return yg_perbaiki;
    }

    public void setYgPerbaiki(String yg_perbaiki){
        this.yg_perbaiki = yg_perbaiki;
    }

    public String getTglSelesai(){
        return tgl_selesai;
    }

    public void setTglSelesai(String tgl_selesai){
        this.tgl_selesai = tgl_selesai;
    }

    public String getStlPerbaikan(){
        return stl_perbaikan;
    }

    public void setStlPerbaikan(String stl_perbaikan){
        this.stl_perbaikan = stl_perbaikan;
    }

    public String getNote(){
        return catatan;
    }

    public void setNote(String catatan){
        this.catatan = catatan;
    }

    public String getBrokenId(){
        return broken_id;
    }

    public void setBrokenID(String broken_id){
        this.broken_id = broken_id;
    }
}
