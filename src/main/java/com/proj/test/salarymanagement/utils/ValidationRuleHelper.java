package com.proj.test.salarymanagement.utils;

import com.proj.test.salarymanagement.dto.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ValidationRuleHelper {

    public List<ValidationRule> getRules(String operation){
        switch (operation){
            case "create":
                return getRulesForCreate();
            case "upload":
                return getRulesForUpload();
            case "update":
                return getRulesForUpdate();
            case "fetch":
                return getRulesForFetch();
        }
        return Collections.EMPTY_LIST;
    }

    private List<ValidationRule> getRulesForFetch(){
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule(Arrays.asList("minSalary","maxSalary"),"salary"));
        return validationRules;
    }
    private List<ValidationRule> getRulesForUpload(){
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"),
                new ValidationRule("all","notBlank"),new ValidationRule(Arrays.asList("id","login"),"unique"),
                new ValidationRule("startDate","date_format"));
        return validationRules;
    }

    private List<ValidationRule> getRulesForCreate(){
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"),
                new ValidationRule("all","notBlank"),new ValidationRule(Arrays.asList("id","login"),"unique"),
                new ValidationRule("startDate","date_format"));
        return validationRules;
    }

    private List<ValidationRule> getRulesForUpdate(){
        final List<ValidationRule> validationRules = Arrays.asList(new ValidationRule("salary","gteq","0.0"),
                new ValidationRule("all","notBlank"), new ValidationRule("startDate","date_format"));
        return validationRules;
    }
}
