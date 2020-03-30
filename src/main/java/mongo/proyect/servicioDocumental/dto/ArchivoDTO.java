/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.dto;


/**
 *
 * @author junpa
 */
public class ArchivoDTO {
    
    private String nombreArchivo;
    private String textoCompleto;
    private String URL;
    private boolean OCR;
    
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTextoCompleto() {
        return textoCompleto;
    }

    public void setTextoCompleto(String textoCompleto) {
        this.textoCompleto = textoCompleto;
    }

    public boolean isOCR() {
        return OCR;
    }

    public void setOCR(boolean OCR) {
        this.OCR = OCR;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    
}
