/**
 * Copyright 2014 www.migratebird.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
