package com.migratebird;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
//import static org.hamcrest.MatcherAssert.*;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;

public class AssertUtils {

	public static <T> void assertPropertyLenientEquals(String name,
			List<?> expected, Collection<T> actual) {
		for (Object expectedItem : expected) {
			assertThat(
					actual,
					hasItem(Matchers.<T> hasProperty(name, equalTo(expectedItem))));
		}
	}

	public static void assertLenientEquals(List<Integer> expected,
			List<Long> actual) {
		for (Integer expectedItem : expected) {
			Long val = expectedItem == null?null:expectedItem.longValue();
			assertThat(actual, hasItem(val));
		}
	}

}
