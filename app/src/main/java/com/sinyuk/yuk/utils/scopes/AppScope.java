package com.sinyuk.yuk.utils.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Sinyuk on 16/7/8.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
