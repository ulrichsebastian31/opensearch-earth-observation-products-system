//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.12 at 11:15:35 AM BST 
//


package com.astrium.hmas.bean.catalogue;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/eop/2.0}Footprint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "footprint"
})
@XmlRootElement(name = "featureOfInterest")
public class FeatureOfInterest {

    @XmlElement(name = "Footprint", namespace = "http://www.opengis.net/eop/2.0")
    protected Footprint footprint;

    /**
     * Gets the value of the footprint property.
     * 
     * @return
     *     possible object is
     *     {@link Footprint }
     *     
     */
    public Footprint getFootprint() {
        return footprint;
    }

    /**
     * Sets the value of the footprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Footprint }
     *     
     */
    public void setFootprint(Footprint value) {
        this.footprint = value;
    }

}
