package com.devopspulse.controller;


import com.devopspulse.service.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("/api")


public class SystemController {
        private final SystemService systemService;

        public SystemController(SystemService systemService){
            this.systemService = systemService;
        }


        @GetMapping("/system")
        public Map<String, Object> getSystemInfo() {
            return systemService.getSystemInfo();
        }

}
