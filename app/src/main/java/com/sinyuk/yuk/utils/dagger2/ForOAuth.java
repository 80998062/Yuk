package com.sinyuk.yuk.utils.dagger2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Sinyuk on 16/8/23.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ForOAuth {
}
