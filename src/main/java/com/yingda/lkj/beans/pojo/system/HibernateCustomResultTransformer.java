package com.yingda.lkj.beans.pojo.system;

import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class HibernateCustomResultTransformer extends AliasedTupleSubsetResultTransformer {

    // IMPL NOTE : due to the delayed population of setters (setters cached
    // 		for performance), we really cannot properly define equality for
    // 		this transformer

    private final Class resultClass;
    private boolean isInitialized;
    private String[] aliases;
    private Setter[] setters;

    public HibernateCustomResultTransformer(Class resultClass) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        isInitialized = false;
        this.resultClass = resultClass;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;

        try {
            if ( ! isInitialized ) {
                initialize( aliases );
            }

            result = resultClass.getDeclaredConstructor().newInstance();

            for ( int i = 0; i < aliases.length; i++ ) {
                if ( setters[i] != null ) {
                    setters[i].set( result, tuple[i], null );
                }
            }
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;
    }

    private void initialize(String[] aliases) {
        PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
                PropertyAccessStrategyBasicImpl.INSTANCE,
                PropertyAccessStrategyFieldImpl.INSTANCE,
                PropertyAccessStrategyMapImpl.INSTANCE
        );
        this.aliases = new String[ aliases.length ];
        setters = new Setter[ aliases.length ];
        for ( int i = 0; i < aliases.length; i++ ) {
            String alias = aliases[ i ];
            if ( alias != null ) {
                alias = convertToCamelCase(alias);
                this.aliases[ i ] = alias;
                setters[ i ] = propertyAccessStrategy.buildPropertyAccess( resultClass, alias ).getSetter();
            }
        }
        isInitialized = true;
    }

    private void check(String[] aliases) {
        if ( ! Arrays.equals( aliases, this.aliases ) ) {
            throw new IllegalStateException(
                    "aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
                            " cached=" + Arrays.asList( this.aliases ) );
        }
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        HibernateCustomResultTransformer that = ( HibernateCustomResultTransformer ) o;

        if ( ! resultClass.equals( that.resultClass ) ) {
            return false;
        }
        if ( ! Arrays.equals( aliases, that.aliases ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = resultClass.hashCode();
        result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
        return result;
    }

    private static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // quick check
        if (name == null || name.isEmpty()) {
            return "";
        } else if (!name.contains("_")) {
            return name;
        }

        // Split the original string with underscore
        String[] camels = name.split("_");
        for (int i = 0; i < camels.length; i++) {
            String camel = camels[i];

            // Skip the underline or double underline at the beginning and end of the original string
            if (camel.isEmpty()) {
                continue;
            }
            // 除了首字母外，Capitalize the first letter
            if (i != 0)
                result.append(camel.substring(0, 1).toUpperCase());
            if (i == 0)
                result.append(camel.charAt(0));
            result.append(camel.substring(1));
        }
        return result.toString();
    }
}
