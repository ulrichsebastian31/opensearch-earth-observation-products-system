//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.09 at 11:07:48 AM BST 
//


package com.astrium.hmas.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="multiExtentOf" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.opengis.net/gml/3.2}MultiSurface" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.opengis.net/gml/3.2}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "multiExtentOf"
})
@XmlRootElement(name = "Footprint", namespace = "http://www.opengis.net/eop/2.0")
public class Footprint {

    @XmlElement(namespace = "http://www.opengis.net/eop/2.0")
    protected Footprint.MultiExtentOf multiExtentOf;
    @XmlAttribute(name = "id", namespace = "http://www.opengis.net/gml/3.2")
    protected String id;

    /**
     * Gets the value of the multiExtentOf property.
     * 
     * @return
     *     possible object is
     *     {@link Footprint.MultiExtentOf }
     *     
     */
    public Footprint.MultiExtentOf getMultiExtentOf() {
        return multiExtentOf;
    }

    /**
     * Sets the value of the multiExtentOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Footprint.MultiExtentOf }
     *     
     */
    public void setMultiExtentOf(Footprint.MultiExtentOf value) {
        this.multiExtentOf = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }


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
     *         &lt;element ref="{http://www.opengis.net/gml/3.2}MultiSurface" minOccurs="0"/>
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
        "multiSurface"
    })
    public static class MultiExtentOf {

        @XmlElement(name = "MultiSurface", namespace = "http://www.opengis.net/gml/3.2")
        protected MultiSurface multiSurface;

        /**
         * Gets the value of the multiSurface property.
         * 
         * @return
         *     possible object is
         *     {@link MultiSurface }
         *     
         */
        public MultiSurface getMultiSurface() {
            return multiSurface;
        }

        /**
         * Sets the value of the multiSurface property.
         * 
         * @param value
         *     allowed object is
         *     {@link MultiSurface }
         *     
         */
        public void setMultiSurface(MultiSurface value) {
            this.multiSurface = value;
        }

    }

}
