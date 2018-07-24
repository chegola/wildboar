package com.wildboar.cucumber.stepdefs;

import com.wildboar.WildboarApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WildboarApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
