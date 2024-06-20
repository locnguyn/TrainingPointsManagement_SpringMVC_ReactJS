/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.dto.ReportMissingDTO;
import com.pbthnxl.pojo.ReportMissing;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ReportMissingService {
    List<ReportMissing> getReportMissings();
    List<ReportMissingDTO> getStudentReportMissings(int studentId);
    void confirmReportMissingById(int id);
    void rejectReportMissingById(int id);
    void save(ReportMissing r);
    ReportMissingDTO convertToDTO(ReportMissing r);
    ReportMissing findByStudentIdAndActivityParticipationTypeId(int studentId, int activityParticipationTypeId);
}
