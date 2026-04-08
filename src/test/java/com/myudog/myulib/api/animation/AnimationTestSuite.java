package com.myudog.myulib.api.animation;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

// 告訴 JUnit 這是一個測試套件，並選擇要包含的測試類別
@Suite
@SelectClasses({
        EasingTest.class,
        AnimatorComponentTest.class,
        com.myudog.myulib.client.internal.ui.system.AnimationSystemTest.class
})
public final class AnimationTestSuite {
    // 裡面不需要任何程式碼！
    // 注意：要使用此功能，請確保 build.gradle 中有加入以下依賴：
    // testImplementation "org.junit.platform:junit-platform-suite-engine:1.10.0"
}