/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicoy.openjbi.domain;

/**
 *
 * @author logicoy
 */
import java.sql.Timestamp;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;
public class TimestampAdapter extends XmlAdapter<Date, Timestamp> {
      public Date marshal(Timestamp v) {
          return new Date(v.getTime());
      }
      public Timestamp unmarshal(Date v) {
          return new Timestamp(v.getTime());
      }
  }