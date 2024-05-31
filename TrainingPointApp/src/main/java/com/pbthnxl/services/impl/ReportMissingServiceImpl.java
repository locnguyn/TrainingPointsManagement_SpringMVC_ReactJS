/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.ReportMissing;
import com.pbthnxl.repositories.ReportMissingRepository;
import com.pbthnxl.services.ReportMissingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ReportMissingServiceImpl implements ReportMissingService{
    @Autowired
    private ReportMissingRepository reportMissingRepository;

    @Override
    public List<ReportMissing> getReportMissings() {
        return this.reportMissingRepository.getReportMissings();
    }

    @Override
    public void confirmReportMissingById(int id) {
        this.reportMissingRepository.confirmReportMissingById(id);
    }

    @Override
    public void rejectReportMissingById(int id) {
        this.reportMissingRepository.rejectReportMissingById(id);
    }
    
}
