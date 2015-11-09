package com.springapp.mvc.controller;

import com.springapp.mvc.AnnPerformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by bthofrichter on 08.11.15.
 */
@Controller
@RequestMapping("/")
public class annController {
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        try {
            AnnPerformer annPerformer = new AnnPerformer("resources/ann/4-09-1_Sigmoid_Bias.nnet", 4, 1);
//            annPerformer.getspecialdatafordiagram
//            ModelMap modelMap = new ModelMap();
//            ModelAndView mv = new ModelAndView("Digagramm.jsp", modelMap)
        } catch (Exception e) {
            e.getMessage();
        }
        return "hello";
    }
}
