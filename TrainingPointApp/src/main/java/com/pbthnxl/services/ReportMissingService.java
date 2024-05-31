/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.ReportMissing;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ReportMissingService {
    List<ReportMissing> getReportMissings();
    void confirmReportMissingById(int id);
    void rejectReportMissingById(int id);
}
