/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.ReportMissing;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ReportMissingRepository {
    List<ReportMissing> getReportMissings();
    void confirmReportMissingById(int id);
    void rejectReportMissingById(int id);
}
