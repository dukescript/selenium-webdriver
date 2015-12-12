/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.test.selenium.webdriver;

import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Property;

@Model(className = "TestModel",properties = {
    @Property(name = "text", type = String.class),
    @Property(name = "input", type = String.class)
}, targetId = "")
public class TestModelDef {
    
    @Function
    public static void commit(TestModel model){
        System.out.println("commit called "+model.getInput());
        model.setText(model.getInput());
    }
}
