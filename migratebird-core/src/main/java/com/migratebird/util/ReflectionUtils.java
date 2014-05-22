/**
 * Copyright 2014 Turgay Kivrak
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
package com.migratebird.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility methods that use reflection for instance creation or class inspection.
 *
*/
public class ReflectionUtils {


    /**
     * Creates an instance of the class with the given name.
     * The class's no argument constructor is used to create an instance.
     *
     * @param className           The name of the class, not null
     * @param bypassAccessibility If true, no exception is thrown if the parameterless constructor is not public
     * @return An instance of this class
     * @throws MigrateBirdException if the class could not be found or no instance could be created
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T createInstanceOfType(String className, boolean bypassAccessibility) {
        return (T) createInstanceOfType(className, bypassAccessibility, new Class<?>[0], new Object[0]);
    }


    /**
     * Creates an instance of the class with the given name.
     * The class's no argument constructor is used to create an instance.
     *
     * @param className           The name of the class, not null
     * @param bypassAccessibility If true, no exception is thrown if the parameterless constructor is not public
     * @param parameterTypes      Types of the constructor arguments
     * @param parameters          Constructor arguments
     * @return An instance of this class
     * @throws MigrateBirdException if the class could not be found or no instance could be created
     */
    @SuppressWarnings({"unchecked"})
    private static <T> T createInstanceOfType(String className, boolean bypassAccessibility, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Class<?> type = Class.forName(className);
            return (T) createInstanceOfType(type, bypassAccessibility, parameterTypes, parameters);

        } catch (ClassCastException e) {
            throw new MigrateBirdException("Class " + className + " is not of expected type.", e);

        } catch (NoClassDefFoundError e) {
            throw new MigrateBirdException("Unable to load class " + className, e);

        } catch (ClassNotFoundException e) {
            throw new MigrateBirdException("Class " + className + " not found", e);

        } catch (MigrateBirdException e) {
            throw e;

        } catch (Exception e) {
            throw new MigrateBirdException("Error while instantiating class " + className, e);
        }
    }


    /**
     * Creates an instance of the given type
     *
     * @param <T>                 The type of the instance
     * @param type                The type of the instance
     * @param bypassAccessibility If true, no exception is thrown if the parameterless constructor is not public
     * @param parameterTypes      Types of the constructor arguments
     * @param parameters          Constructor arguments
     * @return An instance of this type
     * @throws MigrateBirdException If an instance could not be created
     */
    public static <T> T createInstanceOfType(Class<T> type, boolean bypassAccessibility, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(parameterTypes);
            if (bypassAccessibility) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(parameters);

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof MigrateBirdException) {
                throw (MigrateBirdException) cause;
            }
            throw new MigrateBirdException("Error while trying to create object of class " + type.getName(), cause);
        } catch (Exception e) {
            throw new MigrateBirdException("Error while trying to create object of class " + type.getName(), e);
        }
    }


    /**
     * Gets the class for the given name.
     * An DbMaintainException is thrown when the class could not be loaded.
     *
     * @param className The name of the class, not null
     * @return The class, not null
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassWithName(String className) {
        try {
            return (Class<T>) Class.forName(className);

        } catch (Throwable t) {
            throw new MigrateBirdException("Could not load class with name " + className, t);
        }
    }

}
