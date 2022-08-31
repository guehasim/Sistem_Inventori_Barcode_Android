package com.example.ahmadhasim.ilabinventory.pinjam;

/**
 * Created by AHMAD HASIM on 8/27/2016.
 */
public class PinjamData {

    private String id, sub_id, tgl_pinjam, tgl_kembali, barang, serial, peminjam, catatan;

    public PinjamData() {
    }

    public PinjamData(String id, String sub_id, String tgl_pinjam, String tgl_kembali, String barang, String serial, String peminjam, String catatan) {
        this.id = id;
        this.sub_id = sub_id;
        this.tgl_pinjam = tgl_pinjam;
        this.tgl_kembali = tgl_kembali;
        this.barang = barang;
        this.serial = serial;
        this.peminjam = peminjam;
        this.catatan = catatan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id){
        this.sub_id = sub_id;
    }

    public String getTgl_pinjam(){
        return tgl_pinjam;
    }

    public void setTgl_pinjam(String tgl_pinjam){
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getTgl_kembali(){
        return tgl_kembali;
    }

    public void setTgl_kembali(String tgl_kembali){
        this.tgl_kembali = tgl_kembali;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public String getSerial(){
        return serial;
    }

    public void setSerial(String serial){
        this.serial = serial;
    }

    public String getPeminjam() {
        return peminjam;
    }

    public void setPeminjam(String peminjam) {
        this.peminjam = peminjam;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

}
