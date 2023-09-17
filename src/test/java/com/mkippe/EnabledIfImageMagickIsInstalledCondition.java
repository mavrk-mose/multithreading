package com.mkippe;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public class EnabledIfImageMagickIsInstalledCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        //safeguard for checking the context with which the annotation has been called
        return findAnnotation(extensionContext.getElement(), EnabledIfImageMagickIsInstalled.class)
                //if we found the annotation we wall the detectVersion method
                .map((annotation) ->(new ImageMagick().detectVersion() != ImageMagick.Version.NA) //if this evaluates to true
                    ? ConditionEvaluationResult.enabled("ImageMagick installed") //this is called
                    : ConditionEvaluationResult.disabled("No ImageMagick installation found."))//if it's not true it calls this
                .orElse(ConditionEvaluationResult.disabled("By default, Imagemagick test is diabled")); // if annotation is not found this is called
    }
}
