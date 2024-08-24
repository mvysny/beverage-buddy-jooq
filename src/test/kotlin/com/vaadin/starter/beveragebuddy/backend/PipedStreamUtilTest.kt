package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.kaributesting.v10.expectList
import org.junit.jupiter.api.Test

class PipedStreamUtilTest {
    @Test fun pipedStreamUtil() {
        val i = PipedStreamUtil.getInputStream { o -> o.write(byteArrayOf(1, 2, 3)) }
        expectList(1, 2, 3) { i.readBytes().toList() }
    }
}
