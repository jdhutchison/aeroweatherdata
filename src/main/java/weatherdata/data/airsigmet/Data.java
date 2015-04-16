//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.11 at 10:05:20 PM SGT 
//


package weatherdata.data.airsigmet;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}AIRSIGMET"/>
 *       &lt;/sequence>
 *       &lt;attribute name="num_results" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "airsigmet"
})
@XmlRootElement(name = "weatherdata/data")
public class Data {

    @XmlElement(name = "AIRSIGMET")
    protected List<AIRSIGMET> airsigmet;
    @XmlAttribute(name = "num_results")
    protected Integer numResults;

    /**
     * Gets the value of the airsigmet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the airsigmet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAIRSIGMET().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AIRSIGMET }
     * 
     * 
     */
    public List<AIRSIGMET> getAIRSIGMET() {
        if (airsigmet == null) {
            airsigmet = new ArrayList<AIRSIGMET>();
        }
        return this.airsigmet;
    }

    /**
     * Gets the value of the numResults property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumResults() {
        return numResults;
    }

    /**
     * Sets the value of the numResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumResults(Integer value) {
        this.numResults = value;
    }

}
