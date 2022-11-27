package hu.petrik.varganorbert_javafxrestclientdolgozat;

import com.google.gson.annotations.Expose;

public class Orszag {
    private int id;
    @Expose
    private String nev;
    @Expose
    private int lakossag;
    @Expose
    private boolean Eu_tag;

    public Orszag(int id, String nev, int lakossag, String tagallam) {
        this.id = id;
        this.nev = nev;
        this.lakossag = lakossag;
        if (tagallam=="igen"){
            Eu_tag=true;
        }else{
            Eu_tag=false;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getLakossag() {
        return lakossag;
    }

    public void setLakossag(int lakossag) {
        this.lakossag = lakossag;
    }

    public String getEu_tag() {
        if (this.Eu_tag==true){
            return "igen";
        }else {
            return "nem";
        }
    }

    public void setEu_tag(boolean eu_tag) {
        Eu_tag = eu_tag;
    }

    public boolean tagallamConverter(String tagallam){
        if (tagallam=="igen"){
            return true;
        }else{
            return false;
        }
    }
}
