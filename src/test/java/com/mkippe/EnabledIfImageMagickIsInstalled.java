package com.mkippe;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(EnabledIfImageMagickIsInstalledCondition.class)
public @interface EnabledIfImageMagickIsInstalled {

}
