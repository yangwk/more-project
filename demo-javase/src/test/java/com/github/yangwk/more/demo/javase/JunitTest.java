package com.github.yangwk.more.demo.javase;


import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JunitTest {
	static Logger log = LoggerFactory.getLogger(JunitTest.class);

	@BeforeClass
	public static void beforeClass() throws Exception {
		log.debug("beforeClass");
	}

	@AfterClass
	public static void afterClass() throws Exception {
		log.debug("afterClass");
	}

	@Before
	public void before() throws Exception {
		log.debug("before");
	}

	@After
	public void after() throws Exception {
		log.debug("after");
	}

	@Test
	public void test() {
		log.debug("test");
		assertEquals("a", "a");
	}

}
