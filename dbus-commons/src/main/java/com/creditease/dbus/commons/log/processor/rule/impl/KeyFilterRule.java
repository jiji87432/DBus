package com.creditease.dbus.commons.log.processor.rule.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creditease.dbus.commons.Constants;
import com.creditease.dbus.commons.log.processor.parse.ParseResult;
import com.creditease.dbus.commons.log.processor.parse.ParseRuleGrammar;
import com.creditease.dbus.commons.log.processor.parse.RuleGrammar;
import com.creditease.dbus.commons.log.processor.rule.IRule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class KeyFilterRule implements IRule {
    public List<String> transform(List<String> data, List<RuleGrammar> grammar, Rules ruleType) throws Exception{
        List<ParseResult> prList = ParseRuleGrammar.parse(grammar, data.size(), ruleType);
        boolean isOk = false;
        for (ParseResult pr : prList) {
            JSONObject json = JSON.parseObject(data.get(0));
            String val = json.getString(pr.getFilterKey());
            if(StringUtils.equals(pr.getRuleType(), Constants.RULE_TYPE_STRING)) {
                if (pr.getEq()) {
                    if (StringUtils.contains(val, pr.getParamter())) {
                        isOk = true;
                    }
                } else {
                    if (!StringUtils.contains(val, pr.getParamter())) {
                        isOk = true;
                    }
                }
            } else if(StringUtils.equals(pr.getRuleType(), Constants.RULE_TYPE_REGEX)) {
                if(pr.getEq()) {
                    if(Pattern.compile(pr.getParamter()).matcher(val).find()) {
                        isOk = true;
                    }
                } else {
                    if(!Pattern.compile(pr.getParamter()).matcher(val).find()) {
                        isOk = true;
                    }
                }

            }
        }
        return (isOk) ? data : new ArrayList<>();
    }


    public static void main(String[] args) {

    }
}
