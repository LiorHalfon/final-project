package bing;

import feign.Contract;
import feign.MethodMetadata;
import feign.Util;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class FeignContract extends Contract.Default {
    public FeignContract() {
    }

    protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
        boolean isSuperHttpAnnotation = super.processAnnotationsOnParameter(data, annotations, paramIndex);
        if(isSuperHttpAnnotation) {
            return true;
        } else {
            boolean isHttpAnnotation = false;
            Annotation[] var6 = annotations;
            int var7 = annotations.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Annotation annotation = var6[var8];
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if(annotationType == Named.class) {
                    String name = ((Named)annotation).value();
                    Util.checkState(Util.emptyToNull(name) != null, "Named annotation was empty on param %s.", new Object[]{Integer.valueOf(paramIndex)});
                    this.nameParam(data, name, paramIndex);
                    isHttpAnnotation = true;
                    String varName = '{' + name + '}';
                    if(!data.template().url().contains(varName) && !searchMapValuesContainsExact(data.template().queries(), varName) && !searchMapValuesContainsSubstring(data.template().headers(), varName)) {
                        data.formParams().add(name);
                    }
                }
            }

            return isHttpAnnotation;
        }
    }

    static <K, V> boolean searchMapValuesContainsExact(Map<K, Collection<V>> map, V search) {
        Collection<Collection<V>> values = map.values();
        Iterator var3 = values.iterator();

        Collection entry;
        do {
            if(!var3.hasNext()) {
                return false;
            }

            entry = (Collection)var3.next();
        } while(!entry.contains(search));

        return true;
    }

    static <K, V> boolean searchMapValuesContainsSubstring(Map<K, Collection<String>> map, String search) {
        Collection<Collection<String>> values = map.values();
        Iterator var3 = values.iterator();

        while(var3.hasNext()) {
            Collection<String> entry = (Collection)var3.next();
            Iterator var5 = entry.iterator();

            while(var5.hasNext()) {
                String value = (String)var5.next();
                if(value.contains(search)) {
                    return true;
                }
            }
        }

        return false;
    }
}

