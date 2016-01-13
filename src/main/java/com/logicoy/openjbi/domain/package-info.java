
/**
 *
 * @author srinivasan.govindaraj
 */
/**
 * All service beans used in your application.
 */


@XmlJavaTypeAdapters( 
 @XmlJavaTypeAdapter(value=TimestampAdapter.class,type=Timestamp.class))
package com.logicoy.openjbi.domain;
import java.sql.Timestamp;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import com.logicoy.openjbi.domain.TimestampAdapter;