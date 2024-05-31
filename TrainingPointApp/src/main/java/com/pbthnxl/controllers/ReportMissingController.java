/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.services.ReportMissingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/report-missing")
public class ReportMissingController {
    
    @Autowired
    private ReportMissingService reportMissingService;
    
    
    // Confirm report missing by ID
    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.GET)
    public String confirmReportMissing(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            reportMissingService.confirmReportMissingById(id);
            redirectAttributes.addFlashAttribute("message", "Xác nhận báo thiếu thành công.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xác nhận báo thiếu thất bại.");
        }
        return "redirect:/report-missing/list";
    }

    // Reject report missing by ID
    @RequestMapping(value = "/reject/{id}", method = RequestMethod.GET)
    public String rejectReportMissing(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            reportMissingService.rejectReportMissingById(id);
            redirectAttributes.addFlashAttribute("message", "Từ chối báo thiếu thành công.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Từ chối báo thiếu thất bại.");
        }
        return "redirect:/report-missing/list";
    }
    
    @GetMapping("/list")
    public String listReports(Model model) {
        model.addAttribute("reports", reportMissingService.getReportMissings());
        return "report-missing-list";
    }
    
}
