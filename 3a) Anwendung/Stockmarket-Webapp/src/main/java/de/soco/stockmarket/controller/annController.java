package de.soco.stockmarket.controller;

import org.json.JSONObject;
import org.springframework.data.rest.webmvc.BasePathAwareController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by bthofrichter on 08.11.15.
 */
@RestController
@RequestMapping(value = "/stock")
public class annController {

    @RequestMapping(value= "/dist_data", method = RequestMethod.GET)
    public String distData() {
        try {
//            AnnPerformer annPerformer = new AnnPerformer("resources/ann/4-09-1_Sigmoid_Bias.nnet", 4, 1);
            String s = "[{\"x\": 1,  \"y\": 70}";
//            JSONObject response = new JSONObject(s);
//            response.toString();
            return s;

        } catch (Exception e) {
            e.getMessage();
        }
        return "hello";
    }
}
